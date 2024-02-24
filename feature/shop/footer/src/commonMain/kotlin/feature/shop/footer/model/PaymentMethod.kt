package feature.shop.footer.model

data class PaymentMethod(
    val id: String,
    val name: String,
    val imageUrl: String,
)

val dummyPaymentMethods = listOf(
    PaymentMethod(
        id = "1",
        imageUrl = "https://cdn-icons-png.flaticon.com/128/196/196578.png",
        name = "Visa"
    ),
    PaymentMethod(
        id = "2",
        imageUrl = "https://cdn-icons-png.flaticon.com/128/14062/14062982.png",
        name = "Mastercard"
    ),
    PaymentMethod(
        id = "3",
        imageUrl = "https://cdn-icons-png.flaticon.com/128/349/349228.png",
        name = "American Express"
    ),
    PaymentMethod(
        id = "4",
        imageUrl = "https://cdn-icons-png.flaticon.com/128/196/196565.png",
        name = "Paypal"
    )
)
