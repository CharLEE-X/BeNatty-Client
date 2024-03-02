package component.localization.languages

import component.localization.Strings
import core.util.enumCapitalized
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import localization.BuildKonfig

internal fun englishLanguage(
    key: Strings,
    appName: String = BuildKonfig.appName,
    companyName: String = BuildKonfig.orgName,
    currentYear: Int = Clock.System.todayIn(TimeZone.currentSystemDefault()).year,
): String = when (key) {
    Strings.ShopName -> appName
    Strings.AppMotto -> "Here you become different from others"
    Strings.CompanyName -> companyName
    Strings.AppCopyright -> "© $currentYear $companyName"
    Strings.AppVersion -> "0.1.0"
    Strings.ContinueWithGoogle -> "Continue with Google"
    Strings.ContinueWithFacebook -> "Continue with Facebook"
    Strings.Or -> "OR"
    Strings.ForgotPassword -> "Forgot your password?"
    Strings.DontHaveAccount -> "Don't have an account?"
    Strings.SignUp -> "Sign Up"
    Strings.SignUpWithGoogle -> "Sign up with Google"
    Strings.SignUpWithFacebook -> "Sign up with Facebook"
    Strings.Newsletter -> "Sign up for news about sales and new arrivals"
    Strings.AlreadyHaveAnAccount -> "Already have an account?"
    Strings.PrivacyPolicy -> "Privacy Policy"
    Strings.And -> "and"
    Strings.TermsOfService -> "Terms of Service"
    Strings.BySigningUpAgree -> "By signing up, you agree to our"
    Strings.ForgotPasswordDescription -> "Wo worries, we'll send recovery link to your email."
    Strings.CheckEmailDescription -> "We have sent a password reset link to"
    Strings.PromoText -> "Free shipping on orders over £50"
    Strings.HelpAndFaq -> "Help & FAQ"
    Strings.CurrencyEnUs -> "EN, $"
    Strings.AboutUsSmall -> "About us"
    Strings.AboutUs -> "About Us"
    Strings.ContactUs -> "Contact Us"
    Strings.CyberSecurity -> "Cyber Security"
    Strings.FAQs -> "FAQs"
    Strings.TrackOrder -> "Track Order"
    Strings.IsFeaturedDesc -> "Indicates whether or not the product should be featured."
    Strings.AllowReviewsDesc -> "Indicates whether or not a product allows reviews."
    Strings.CatalogVisibilityDesc -> "Indicates whether or not the product should be visible in the catalog."
    Strings.CategoriesDesc -> "The categories this product is in"
    Strings.DescriptionDesc -> "The product's full description."
    Strings.ParentCategoryDesc -> "The parent category of the product."
    Strings.ToStart -> "to start"
    Strings.TagsDesc -> "The tags this product has"
    Strings.PriceDesc -> "The current price of the product."
    Strings.StockStatusDesc -> "The product's stock status."
    Strings.PostStatusDesc -> "The product's current post status."
    Strings.IsPurchasableDesc -> "Indicates whether or not the product is currently able to be purchased."
    Strings.OnePerOrderDesc -> "Indicates that only one of a product may be held in the order at a time."
    Strings.UnsavedChanges -> "Careful! You have unsaved changes!"
    Strings.ImagesDesc -> "The images for the product."
    Strings.BackorderStatusDesc -> "The status of back-ordering for a product."
    Strings.CanBackorderDesc -> "Indicates whether or not a product can be back-ordered."
    Strings.IsOnBackorderDesc -> "Indicates whether or not a product is on backorder."
    Strings.LowStockThresholdDesc -> "Indicates the threshold for when the low stock notification will be sent to the merchant."
    Strings.RemainingStockDesc -> "The number of inventory units remaining for this product."
    Strings.TrackQuantityDesc -> "Indicates that a product should use the inventory system."
    Strings.RegularPriceDesc -> "The regular price of the product when not discounted."
    Strings.SalePriceDesc -> "The price of the product when on sale."
    Strings.OnSaleDesc -> "Indicates whether or not the product is currently on sale."
    Strings.SaleStartDesc -> "The GMT datetime when the product should start to be on sale."
    Strings.SaleEndDesc -> "The GMT datetime when the product should no longer be on sale."
    Strings.LengthDesc -> "The length of the product in the store's current units."
    Strings.WeightDesc -> "The weight of the product in the store's current units."
    Strings.WidthDesc -> "The width of the product in the store's current units."
    Strings.RequiresShippingDesc -> "Indicates that the product must be shipped."
    Strings.ShippingPreset -> "Choose preset from currently selected category, or pass custom values."
    Strings.Kg -> "kg"
    Strings.Cm -> "cm"
    Strings.CreatedByDesc -> "The user who created the product."
    Strings.ShippingPresetDesc -> "The shipping preset for the product, coming from category shipping settings"
    Strings.ImproveWithAi -> "Improve with AI"
    Strings.DeleteExplain -> "Are you sure you want to delete this? You cannot undo this action."
    Strings.ChargeTaxDesc -> "Indicates whether or not the product should have tax charged on it."
    Strings.UseGlobalTrackingDesc -> "Indicates whether or not the product should use the global tracking settings."
    Strings.NoInsights -> "Insights will display when the product has had recent sales"
    Strings.AddressDesc -> "The primary address of this customer"
    Strings.MarketingEmailsAgreed -> "Customer agreed to receive marketing emails."
    Strings.MarketingSMSAgreed -> "Customer agreed to receive SMS marketing text messages."
    Strings.DiscardAllUnsavedChangesDesc -> "If you discard changes, you’ll delete any edits you made since you last saved."
    Strings.LanguageDesc -> "This customer will receive notifications in this language."
    Strings.MarketingDesc -> "You should ask your customers for permission before you subscribe them to your marketing emails or SMS."
    Strings.LeavePageWithUnsavedChanges -> "Leave page with unsaved changes?"
    Strings.LeavingThisPageWillDiscardAllUnsavedChanges -> "Leaving this page will discard all unsaved changes."
    Strings.Ticker -> "ECO-FRIENDLY CLOTHING. 100% COTTON"
    Strings.UnitedKingdom -> "United Kingdom"
    Strings.ShippingReturns -> "Shipping & Returns"
    Strings.WeWillReply -> "We'll reply ASAP."
    Strings.HeightDesc -> "The height of the product in the store's current units."
    Strings.BeFirstToGetLatestOffers -> "Be the first to get the latest news about trends, promotions and much more!"
    Strings.ByAgreeing -> "By clicking the button you agree to the"
    Strings.EcoFriendlyClothing -> "Eco-Friendly clothing"
    else -> key.name.enumCapitalized()
}
