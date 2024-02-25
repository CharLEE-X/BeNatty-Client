package feature.admin.config

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.GetConfigQuery
import data.service.ConfigService
import data.type.CollageItemInput
import data.type.DayOfWeek
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

val SHAKE_ANIM_DURATION = (.25).seconds
private typealias InputScope = InputHandlerScope<AdminConfigContract.Inputs, AdminConfigContract.Events, AdminConfigContract.State>

internal class AdminProductPageInputHandler :
    KoinComponent,
    InputHandler<AdminConfigContract.Inputs, AdminConfigContract.Events, AdminConfigContract.State> {

    private val configService: ConfigService by inject()
    private val inputValidator by inject<InputValidator>()

    override suspend fun InputScope.handleInput(input: AdminConfigContract.Inputs) = when (input) {
        is AdminConfigContract.Inputs.Init -> handleInit()
        AdminConfigContract.Inputs.FetchConfig -> handleFetchConfig()

        AdminConfigContract.Inputs.OnDiscardClick -> updateState { it.copy(current = it.original).wasEdited() }
        AdminConfigContract.Inputs.OnSaveClick -> handleSave()
        is AdminConfigContract.Inputs.OnOpenDayFromSelected -> handleOnOpenDayFromSelected(input.day)
        is AdminConfigContract.Inputs.OnOpenDayToSelected -> handleOnOpenDayToSelected(input.day)

        is AdminConfigContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminConfigContract.Inputs.SetOriginalConfig -> updateState { it.copy(original = input.config).wasEdited() }
        is AdminConfigContract.Inputs.SetCurrentConfig -> updateState { it.copy(current = input.config).wasEdited() }
        is AdminConfigContract.Inputs.SetEmail -> handleSetEmail(input.email)
        is AdminConfigContract.Inputs.SetEmailShake -> updateState { it.copy(emailShake = input.shake) }
        is AdminConfigContract.Inputs.SetPhone -> handleSetPhone(input.phone)
        is AdminConfigContract.Inputs.SetPhoneShake -> updateState { it.copy(phoneShake = input.shake) }
        is AdminConfigContract.Inputs.SetCompanyWebsite -> handleSetCompanyWebsite(input.companyWebsite)
        is AdminConfigContract.Inputs.SetCompanyWebsiteShake -> updateState { it.copy(companyWebsiteShake = input.shake) }
        is AdminConfigContract.Inputs.SetCloseTime -> handleSetCloseTime(input.closeTime)
        is AdminConfigContract.Inputs.SetCloseTimeShake -> updateState { it.copy(closeTimeShake = input.shake) }
        is AdminConfigContract.Inputs.SetOpenTime -> handleSetOpenTime(input.openTime)
        is AdminConfigContract.Inputs.SetOpenTimeShake -> updateState { it.copy(openTimeShake = input.shake) }
        is AdminConfigContract.Inputs.SetPaymentMethods -> handleSetPaymentMethods(input.paymentMethods)
        is AdminConfigContract.Inputs.SetCreatedAt -> handleSetCreatedAt(input.createdAt)
        is AdminConfigContract.Inputs.SetUpdatedAt -> handleSetUpdatedAt(input.updatedAt)
    }

    private suspend fun InputScope.handleOnOpenDayFromSelected(day: DayOfWeek) {
        updateState {
            val newDayOfWeek = if (it.current.companyInfo.openingTimes.dayTo == day) null else day
            println("day before: ${it.current.companyInfo.openingTimes.dayTo?.name}, passed day: $day, newDayOfWeek: $newDayOfWeek")
            it.copy(
                current = it.current.copy(
                    companyInfo = it.current.companyInfo.copy(
                        openingTimes = it.current.companyInfo.openingTimes.copy(
                            dayFrom = newDayOfWeek
                        )
                    )
                ),
            ).wasEdited()
        }

    }

    private suspend fun InputScope.handleOnOpenDayToSelected(day: DayOfWeek) {
        updateState {
            it.copy(
                current = it.current.copy(
                    companyInfo = it.current.companyInfo.copy(
                        openingTimes = it.current.companyInfo.openingTimes.copy(
                            dayTo = if (it.current.companyInfo.openingTimes.dayFrom == day) null else day
                        )
                    )
                ),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetCompanyWebsite(companyWebsite: String) {
        updateState {
            val isValidated = inputValidator.validateText(companyWebsite)
            it.copy(
                current = it.current.copy(
                    companyInfo = it.current.companyInfo.copy(
                        contactInfo = it.current.companyInfo.contactInfo.copy(companyWebsite = companyWebsite)
                    )
                ),
                companyWebsiteError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleInit() {
        sideJob("handleInit") {
            postInput(AdminConfigContract.Inputs.SetLoading(isLoading = true))
            postInput(AdminConfigContract.Inputs.FetchConfig)
            postInput(AdminConfigContract.Inputs.SetLoading(isLoading = false))
        }
    }

    private suspend fun InputScope.handleFetchConfig() {
        sideJob("handleFetchConfig") {
            configService.getConfig().fold(
                onSuccess = {
                    postInput(AdminConfigContract.Inputs.SetOriginalConfig(config = it.getConfig))
                    postInput(AdminConfigContract.Inputs.SetCurrentConfig(config = it.getConfig))
                },
                onFailure = {
                    postEvent(AdminConfigContract.Events.OnError(it.message ?: "Error while fetching product details"))
                },
            )
        }
    }

    private suspend fun InputScope.handleSetCloseTime(closeTime: String) {
        updateState {
            val isValidated = inputValidator.validateText(closeTime)
            it.copy(
                current = it.current.copy(
                    companyInfo = it.current.companyInfo.copy(
                        openingTimes = it.current.companyInfo.openingTimes.copy(close = closeTime)
                    )
                ),
                closeTimeError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetOpenTime(openTime: String) {
        updateState {
            val isValidated = inputValidator.validateText(openTime)
            it.copy(
                current = it.current.copy(
                    companyInfo = it.current.companyInfo.copy(
                        openingTimes = it.current.companyInfo.openingTimes.copy(open = openTime)
                    )
                ),
                openTimeError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetOpenDayTo(dayOpenTo: DayOfWeek?) {
        updateState {
            it.copy(
                current = it.current.copy(
                    companyInfo = it.current.companyInfo.copy(
                        openingTimes = it.current.companyInfo.openingTimes.copy(dayTo = dayOpenTo)
                    )
                ),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetPhone(phone: String) {
        updateState {
            val isValidated = inputValidator.validateText(phone)
            it.copy(
                current = it.current.copy(
                    companyInfo = it.current.companyInfo.copy(
                        contactInfo = it.current.companyInfo.contactInfo.copy(phone = phone)
                    )
                ),
                phoneError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }


    private suspend fun InputScope.handleSetPaymentMethods(paymentMethods: List<GetConfigQuery.PaymentMethod>) {
        updateState {
            it.copy(
                current = it.current.copy(
                    companyInfo = it.current.companyInfo.copy(paymentMethods = paymentMethods)
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetUpdatedAt(updatedAt: String) {
        updateState { it.copy(current = it.current.copy(updatedAt = updatedAt)).wasEdited() }
    }

    private suspend fun InputScope.handleSetCreatedAt(createdAt: String) {
        updateState { it.copy(current = it.current.copy(createdAt = createdAt)).wasEdited() }
    }

    private suspend fun InputScope.handleUpdateConfig() {
        with(getCurrentState()) {
            sideJob("handleUpdateDetails") {
                configService.updateConfig(
                    configId = original.id,
                    companyWebsite = if (current.companyInfo.contactInfo.companyWebsite != original.companyInfo.contactInfo.companyWebsite)
                        current.companyInfo.contactInfo.companyWebsite else null,
                    email = if (current.companyInfo.contactInfo.email != original.companyInfo.contactInfo.email)
                        current.companyInfo.contactInfo.email else null,
                    phone = if (current.companyInfo.contactInfo.phone != original.companyInfo.contactInfo.phone)
                        current.companyInfo.contactInfo.phone else null,
                    dayFrom = if (current.companyInfo.openingTimes.dayFrom != original.companyInfo.openingTimes.dayFrom)
                        current.companyInfo.openingTimes.dayFrom else null,
                    dayTo = if (current.companyInfo.openingTimes.dayTo != original.companyInfo.openingTimes.dayTo)
                        current.companyInfo.openingTimes.dayTo else null,
                    open = if (current.companyInfo.openingTimes.open != original.companyInfo.openingTimes.open)
                        current.companyInfo.openingTimes.open else null,
                    close = if (current.companyInfo.openingTimes.close != original.companyInfo.openingTimes.close)
                        current.companyInfo.openingTimes.close else null,
                    collageItems = if (current.landingConfig.collageItems != original.landingConfig.collageItems)
                        current.landingConfig.collageItems.map {
                            CollageItemInput(
                                id = it.id,
                                title = it.title,
                                description = it.description,
                                imageUrl = it.imageUrl,
                                alt = it.alt,
                            )
                        } else null,
                ).fold(
                    onSuccess = { data ->
                        val config = GetConfigQuery.GetConfig(
                            id = data.updateConfig.id,
                            createdAt = data.updateConfig.createdAt,
                            updatedAt = data.updateConfig.updatedAt,
                            companyInfo = GetConfigQuery.CompanyInfo(
                                contactInfo = GetConfigQuery.ContactInfo(
                                    email = data.updateConfig.companyInfo.contactInfo.email,
                                    phone = data.updateConfig.companyInfo.contactInfo.phone,
                                    companyWebsite = data.updateConfig.companyInfo.contactInfo.companyWebsite,
                                ),
                                openingTimes = GetConfigQuery.OpeningTimes(
                                    close = data.updateConfig.companyInfo.openingTimes.close,
                                    dayFrom = data.updateConfig.companyInfo.openingTimes.dayFrom,
                                    dayTo = data.updateConfig.companyInfo.openingTimes.dayTo,
                                    open = data.updateConfig.companyInfo.openingTimes.open
                                ),
                                paymentMethods = data.updateConfig.companyInfo.paymentMethods.map {
                                    GetConfigQuery.PaymentMethod(
                                        id = it.id,
                                        name = it.name,
                                        imageUrl = it.imageUrl
                                    )
                                }
                            ),
                            landingConfig = GetConfigQuery.LandingConfig(
                                collageItems = data.updateConfig.landingConfig.collageItems.map {
                                    GetConfigQuery.CollageItem(
                                        id = it.id,
                                        title = it.title,
                                        description = it.description,
                                        imageUrl = it.imageUrl,
                                        alt = it.alt
                                    )
                                }
                            )
                        )
                        postInput(AdminConfigContract.Inputs.SetOriginalConfig(config = config))
                        postInput(AdminConfigContract.Inputs.SetCurrentConfig(config = config))
                    },
                    onFailure = {
                        postEvent(
                            AdminConfigContract.Events.OnError(
                                it.message ?: "Error while updating product details"
                            )
                        )
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleSetEmail(email: String) {
        updateState {
            val isValidated = inputValidator.validateText(email)
            it.copy(
                current = it.current.copy(
                    companyInfo = it.current.companyInfo.copy(
                        contactInfo = it.current.companyInfo.contactInfo.copy(email = email)
                    )
                ),
                emailError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSave() {
        with(getCurrentState()) {
            val isTitleError = emailError != null
            val isPhoneError = phoneError != null
            val isOpenTimeError = openTimeError != null
            val isCloseTimeError = closeTimeError != null
            val isNoError = !isTitleError && !isPhoneError && !isOpenTimeError && !isCloseTimeError

            if (!isNoError) {
                shakeErrors(
                    isTitleError,
                    isPhoneError,
                    isOpenTimeError,
                    isCloseTimeError
                )
            } else {
                handleUpdateConfig()
            }
        }
    }
}

private fun AdminConfigContract.State.wasEdited(): AdminConfigContract.State =
    copy(wasEdited = current != original)

private fun InputScope.shakeErrors(
    isEmailError: Boolean,
    isPhoneError: Boolean,
    isOpenTimeError: Boolean,
    isCloseTimeError: Boolean,
) {
    sideJob("handleShakeErrors") {
        if (isEmailError) postInput(AdminConfigContract.Inputs.SetEmailShake(shake = true))
        if (isPhoneError) postInput(AdminConfigContract.Inputs.SetPhoneShake(shake = true))
        if (isOpenTimeError) postInput(AdminConfigContract.Inputs.SetOpenTimeShake(shake = true))
        if (isCloseTimeError) postInput(AdminConfigContract.Inputs.SetCloseTimeShake(shake = true))

        delay(SHAKE_ANIM_DURATION)

        if (isEmailError) postInput(AdminConfigContract.Inputs.SetEmailShake(shake = false))
        if (isPhoneError) postInput(AdminConfigContract.Inputs.SetPhoneShake(shake = false))
        if (isOpenTimeError) postInput(AdminConfigContract.Inputs.SetOpenTimeShake(shake = false))
        if (isCloseTimeError) postInput(AdminConfigContract.Inputs.SetCloseTimeShake(shake = false))
    }
}
