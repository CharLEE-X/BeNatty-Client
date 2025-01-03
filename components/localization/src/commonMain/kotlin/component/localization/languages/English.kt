package component.localization.languages

import component.localization.Strings
import core.util.enumCapitalized
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Suppress("CyclomaticComplexMethod", "LongMethod")
internal fun englishLanguage(
    key: Strings,
    appName: String = "BeNatty",
    companyName: String = "CharLEE X",
    currentYear: Int = Clock.System.todayIn(TimeZone.currentSystemDefault()).year,
): String =
    when (key) {
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
        Strings.LowStockThresholdDesc ->
            "Indicates the threshold for when the low stock notification will be sent to the merchant."

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
        Strings.DiscardAllUnsavedChangesDesc ->
            "If you discard changes, you’ll delete any edits you made since you last saved."

        Strings.LanguageDesc -> "This customer will receive notifications in this language."
        Strings.MarketingDesc ->
            "You should ask your customers for permission before you subscribe them to your marketing emails or SMS."

        Strings.LeavePageWithUnsavedChanges -> "Leave page with unsaved changes?"
        Strings.LeavingThisPageWillDiscardAllUnsavedChanges -> "Leaving this page will discard all unsaved changes."
        Strings.Ticker -> "ECO-FRIENDLY CLOTHING. 100% COTTON"
        Strings.UnitedKingdom -> "United Kingdom"
        Strings.ShippingReturns -> "Shipping & Returns"
        Strings.WeWillReply -> "We'll reply ASAP."
        Strings.HeightDesc -> "The height of the product in the store's current units."
        Strings.NoSpamUnsubscribeAnytime -> "No spam, unsubscribe anytime!"
        Strings.YouAreSigningUpToReceiveEmails ->
            "*You're signing up to receive our emails and can unsubscribe at any time."

        Strings.EcoFriendlyClothing -> "Eco-Friendly clothing"
        Strings.CategoryNameDescription -> "The name of the category."
        Strings.HandmadeTraitDescription ->
            "Handmade products are made by hand, not by machine, and typically by an individual or a small group of people."

        Strings.OrganicTraitDescription ->
            "Organic products are made from materials that are grown without the use of synthetic chemicals or pesticides."

        Strings.EcoFriendlyTraitDescription ->
            "Eco-friendly products are made in a way that is not harmful to the environment."

        Strings.VeganTraitDescription -> "Vegan products are made without the use of animal products or by-products."
        Strings.SociallyResponsibleTraitDescription ->
            "Socially responsible products are made in a way that takes into account the social and environmental " +
                "impact of their production."

        Strings.CharitableTraitDescription -> "Charitable products are made in a way that supports a charitable cause."
        Strings.CustomTraitDescription -> "Custom products are made to order according to the customer's specifications."
        Strings.UniqueTraitDescription -> "Unique products are one-of-a-kind and cannot be found anywhere else."
        Strings.TrendingTraitDescription -> "Trending products are currently popular and in high demand."
        Strings.PopularTraitDescription -> "Popular products are well-liked and frequently purchased by customers."
        Strings.FeaturedTraitDescription -> "Featured products are highlighted and promoted by the store."
        Strings.RecommendedTraitDescription ->
            "Recommended products are suggested to customers based on their preferences and purchase history."

        Strings.SpecialTraitDescription ->
            "Special products are unique or limited-edition items that are not typically found in the store."

        Strings.ExclusiveTraitDescription -> "Exclusive products are only available to a select group of customers."
        Strings.LimitedTraitDescription -> "Limited products are only available in limited quantities for a limited time."
        Strings.NewArrivalTraitDescription -> "New arrival products are recently added to the store's inventory."
        Strings.SeasonalTraitDescription -> "Seasonal products are only available during a specific season or time of year."
        Strings.VintageTraitDescription ->
            "Vintage products are old or second-hand items that are considered to be of high quality or value."

        Strings.LuxuryTraitDescription ->
            "Luxury products are high-end and expensive items that are associated with luxury and sophistication."

        Strings.CasualTraitDescription ->
            "Casual products are comfortable and informal items that are suitable for everyday wear."

        Strings.FormalTraitDescription ->
            "Formal products are elegant and sophisticated items that are suitable for special occasions."

        Strings.BusinessCasualTraitDescription ->
            "Business casual products are professional and stylish items that are suitable for a business casual " +
                "dress code."

        Strings.AthleticTraitDescription -> "Athletic products are designed for sports and physical activity."
        Strings.OutdoorTraitDescription -> "Outdoor products are designed for outdoor activities and adventures."
        Strings.WaterResistantTraitDescription ->
            "Water-resistant products are designed to repel water and keep the wearer dry."

        Strings.InsulatedTraitDescription ->
            "Insulated products are designed to provide warmth and protection from the cold."

        Strings.BreathableTraitDescription ->
            "Breathable products are made from materials that allow air to pass through and moisture to evaporate."

        Strings.StretchTraitDescription ->
            "Stretch products are made from stretchy materials that provide flexibility and freedom of movement."

        Strings.NonIronTraitDescription ->
            "Non-iron products are made from materials that do not wrinkle or crease easily and require little to " +
                "no ironing."

        Strings.EasyCareTraitDescription ->
            "Easy-care products are made from materials that are easy to clean and maintain."

        Strings.MachineWashableTraitDescription ->
            "Machine-washable products can be safely washed in a washing machine."

        Strings.DryCleanOnlyTraitDescription ->
            "Dry-clean-only products must be professionally dry cleaned and cannot be washed in a washing machine."

        Strings.OnAllOrdersOver -> "On all orders over \$100"
        Strings.OnAllUnopenedUnusedItems -> "On all unopened, unused items"
        Strings.ShopWithConfidence -> "Shop with confidence"
        Strings.PercentSatisfaction -> "99.9% Satisfaction"
        Strings.RatedExcellentByCustomers -> "Rated excellent by our customers"
        Strings.PlaysuitsAndRompers -> "Playsuits & Rompers"
        Strings.LatestLooksDescription1 -> "We are delighted to announce our latest range."

        else -> key.name.enumCapitalized()
    }
