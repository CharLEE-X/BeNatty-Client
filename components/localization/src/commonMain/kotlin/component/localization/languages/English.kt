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
    Strings.AppName -> appName
    Strings.AppMotto -> "Here you become different from others"
    Strings.CompanyName -> companyName
    Strings.AppCopyright -> "© $currentYear $companyName"
    Strings.AppVersion -> "0.1.0"
    Strings.Unknown -> key.name.enumCapitalized()
    Strings.Save -> key.name.enumCapitalized()
    Strings.Add -> key.name.enumCapitalized()
    Strings.NoImage -> key.name.enumCapitalized()
    Strings.CloseButton -> key.name.enumCapitalized()
    Strings.Apply -> key.name.enumCapitalized()
    Strings.ShowMore -> key.name.enumCapitalized()
    Strings.RemoveFilter -> key.name.enumCapitalized()
    Strings.AddFilter -> key.name.enumCapitalized()
    Strings.Logo -> key.name.enumCapitalized()
    Strings.Login -> key.name.enumCapitalized()
    Strings.ContinueWithGoogle -> "Continue with Google"
    Strings.ContinueWithFacebook -> "Continue with Facebook"
    Strings.Or -> "OR"
    Strings.Email -> key.name.enumCapitalized()
    Strings.Password -> key.name.enumCapitalized()
    Strings.ForgotPassword -> "Forgot your password?"
    Strings.DontHaveAccount -> "Don't have an account?"
    Strings.SignUp -> "Sign Up"
    Strings.SignUpWithGoogle -> "Sign up with Google"
    Strings.SignUpWithFacebook -> "Sign up with Facebook"
    Strings.FirstName -> key.name.enumCapitalized()
    Strings.RepeatPassword -> key.name.enumCapitalized()
    Strings.Newsletter -> "Sign up for news about sales and new arrivals"
    Strings.AlreadyHaveAnAccount -> "Already have an account?"
    Strings.PrivacyPolicy -> "Privacy Policy"
    Strings.And -> "and"
    Strings.TermsOfService -> "Terms of Service"
    Strings.BySigningUpAgree -> "By signing up, you agree to our"
    Strings.ForgotPasswordDescription -> "Wo worries, we'll send recovery link to your email."
    Strings.GetAResetLink -> key.name.enumCapitalized()
    Strings.CheckYourEmail -> key.name.enumCapitalized()
    Strings.CheckEmailDescription -> "We have sent a password reset link to"
    Strings.OpenGmail -> key.name.enumCapitalized()
    Strings.OpenOutlook -> key.name.enumCapitalized()
    Strings.BackTo -> key.name.enumCapitalized()
    Strings.Phone -> key.name.enumCapitalized()
    Strings.PersonalDetails -> key.name.enumCapitalized()
    Strings.Profile -> key.name.enumCapitalized()
    Strings.OldPassword -> key.name.enumCapitalized()
    Strings.NewPassword -> key.name.enumCapitalized()
    Strings.Address -> key.name.enumCapitalized()
    Strings.Company -> key.name.enumCapitalized()
    Strings.PostCode -> key.name.enumCapitalized()
    Strings.City -> key.name.enumCapitalized()
    Strings.Apartment -> key.name.enumCapitalized()
    Strings.Country -> key.name.enumCapitalized()
    Strings.PromoText -> "Free shipping on orders over £50"
    Strings.HelpAndFaq -> "Help & FAQ"
    Strings.CurrencyEnUs -> "EN, $"
    Strings.Search -> key.name.enumCapitalized()
    Strings.Orders -> key.name.enumCapitalized()
    Strings.Returns -> key.name.enumCapitalized()
    Strings.Wishlist -> key.name.enumCapitalized()
    Strings.Logout -> key.name.enumCapitalized()
    Strings.Subscribe -> "Subscribe to be first to know about our special offers"
    Strings.Accessibility -> key.name.enumCapitalized()
    Strings.AboutUsSmall -> "About us"
    Strings.AboutUs -> "About Us"
    Strings.Career -> key.name.enumCapitalized()
    Strings.ContactUs -> "Contact Us"
    Strings.CyberSecurity -> "Cyber Security"
    Strings.DeliverTo -> key.name.enumCapitalized()
    Strings.English -> key.name.enumCapitalized()
    Strings.FAQs -> "FAQs"
    Strings.Language -> key.name.enumCapitalized()
    Strings.Press -> key.name.enumCapitalized()
    Strings.Shipping -> key.name.enumCapitalized()
    Strings.TrackOrder -> "Track Order"
    Strings.Help -> key.name.enumCapitalized()
    Strings.Users -> key.name.enumCapitalized()
    Strings.Home -> key.name.enumCapitalized()
    Strings.Stats -> key.name.enumCapitalized()
    Strings.Products -> key.name.enumCapitalized()
    Strings.Edit -> key.name.enumCapitalized()
    Strings.Discard -> key.name.enumCapitalized()
    Strings.NewUser -> key.name.enumCapitalized()
    Strings.Delete -> key.name.enumCapitalized()
    Strings.ResetPassword -> key.name.enumCapitalized()
    Strings.Role -> key.name.enumCapitalized()
    Strings.Verified -> key.name.enumCapitalized()
    Strings.NotVerified -> key.name.enumCapitalized()
    Strings.OtherInfo -> key.name.enumCapitalized()
    Strings.LastActive -> key.name.enumCapitalized()
    Strings.CreatedBy -> key.name.enumCapitalized()
    Strings.CreatedAt -> key.name.enumCapitalized()
    Strings.LastUpdatedAt -> key.name.enumCapitalized()
    Strings.Never -> key.name.enumCapitalized()
    Strings.Registered -> key.name.enumCapitalized()
    Strings.NewProduct -> key.name.enumCapitalized()
    Strings.Details -> key.name.enumCapitalized()
    Strings.Name -> key.name.enumCapitalized()
    Strings.Create -> key.name.enumCapitalized()
    Strings.ProductShortDescription -> key.name.enumCapitalized()
    Strings.IsFeatured -> key.name.enumCapitalized()
    Strings.IsFeaturedDesc -> "Indicates whether or not the product should be featured."
    Strings.AllowReviews -> key.name.enumCapitalized()
    Strings.AllowReviewsDesc -> "Indicates whether or not a product allows reviews."
    Strings.CatalogVisibility -> key.name.enumCapitalized()
    Strings.CatalogVisibilityDesc -> "Indicates whether or not the product should be visible in the catalog."
    Strings.Categories -> key.name.enumCapitalized()
    Strings.CategoriesDesc -> "The categories this product is in"
    Strings.NewCategory -> key.name.enumCapitalized()
    Strings.NewCustomer -> key.name.enumCapitalized()
    Strings.User -> key.name.enumCapitalized()
    Strings.CreateProduct -> key.name.enumCapitalized()
    Strings.Product -> key.name.enumCapitalized()
    Strings.CreateCategory -> key.name.enumCapitalized()
    Strings.Category -> key.name.enumCapitalized()
    Strings.Display -> key.name.enumCapitalized()
    Strings.Description -> key.name.enumCapitalized()
    Strings.DescriptionDesc -> "The product's full description."
    Strings.ParentCategory -> key.name.enumCapitalized()
    Strings.ParentCategoryDesc -> "The parent category of the product."
    Strings.ToStart -> "to start"
    Strings.Previous -> key.name.enumCapitalized()
    Strings.Next -> key.name.enumCapitalized()
    Strings.Tags -> key.name.enumCapitalized()
    Strings.TagsDesc -> "The tags this product has"
    Strings.Show -> key.name.enumCapitalized()
    Strings.Filter -> key.name.enumCapitalized()
    Strings.Price -> key.name.enumCapitalized()
    Strings.PriceDesc -> "The current price of the product."
    Strings.Sold -> key.name.enumCapitalized()
    Strings.StockStatus -> key.name.enumCapitalized()
    Strings.StockStatusDesc -> "The product's stock status."
    Strings.Image -> key.name.enumCapitalized()
    Strings.None -> key.name.enumCapitalized()
    Strings.NoCategories -> key.name.enumCapitalized()
    Strings.Status -> key.name.enumCapitalized()
    Strings.PostStatusDesc -> "The product's current post status."
    Strings.Data -> key.name.enumCapitalized()
    Strings.IsPurchasable -> key.name.enumCapitalized()
    Strings.IsPurchasableDesc -> "Indicates whether or not the product is currently able to be purchased."
    Strings.Inventory -> key.name.enumCapitalized()
    Strings.OnePerOrder -> key.name.enumCapitalized()
    Strings.OnePerOrderDesc -> "Indicates that only one of a product may be held in the order at a time."
    Strings.UnsavedChanges -> "Careful! You have unsaved changes!"
    Strings.SaveChanges -> key.name.enumCapitalized()
    Strings.Dismiss -> key.name.enumCapitalized()
    Strings.Id -> key.name.enumCapitalized()
    Strings.Images -> key.name.enumCapitalized()
    Strings.ImagesDesc -> "The images for the product."
    Strings.NoImages -> key.name.enumCapitalized()
    Strings.BackorderStatus -> key.name.enumCapitalized()
    Strings.BackorderStatusDesc -> "The status of back-ordering for a product."
    Strings.CanBackorder -> key.name.enumCapitalized()
    Strings.CanBackorderDesc -> "Indicates whether or not a product can be back-ordered."
    Strings.IsOnBackorder -> key.name.enumCapitalized()
    Strings.IsOnBackorderDesc -> "Indicates whether or not a product is on backorder."
    Strings.LowStockThreshold -> key.name.enumCapitalized()
    Strings.LowStockThresholdDesc -> "Indicates the threshold for when the low stock notification will be sent to the merchant."
    Strings.RemainingStock -> key.name.enumCapitalized()
    Strings.RemainingStockDesc -> "The number of inventory units remaining for this product."
    Strings.TrackQuantity -> key.name.enumCapitalized()
    Strings.TrackQuantityDesc -> "Indicates that a product should use the inventory system."
    Strings.RegularPrice -> key.name.enumCapitalized()
    Strings.RegularPriceDesc -> "The regular price of the product when not discounted."
    Strings.SalePrice -> key.name.enumCapitalized()
    Strings.SalePriceDesc -> "The price of the product when on sale."
    Strings.OnSale -> key.name.enumCapitalized()
    Strings.OnSaleDesc -> "Indicates whether or not the product is currently on sale."
    Strings.SaleStart -> key.name.enumCapitalized()
    Strings.SaleStartDesc -> "The GMT datetime when the product should start to be on sale."
    Strings.SaleEnd -> key.name.enumCapitalized()
    Strings.SaleEndDesc -> "The GMT datetime when the product should no longer be on sale."
    Strings.Height -> key.name.enumCapitalized()
    Strings.HeightDesc -> "The height of the product in the store's current units."
    Strings.Length -> key.name.enumCapitalized()
    Strings.LengthDesc -> "The length of the product in the store's current units."
    Strings.Weight -> key.name.enumCapitalized()
    Strings.WeightDesc -> "The weight of the product in the store's current units."
    Strings.Width -> key.name.enumCapitalized()
    Strings.WidthDesc -> "The width of the product in the store's current units."
    Strings.IsPhysicalProduct -> key.name.enumCapitalized()
    Strings.RequiresShippingDesc -> "Indicates that the product must be shipped."
    Strings.NoTags -> key.name.enumCapitalized()
    Strings.ShippingPreset -> "Choose preset from currently selected category, or pass custom values."
    Strings.Kg -> "kg"
    Strings.Cm -> "cm"
    Strings.CreateTag -> key.name.enumCapitalized()
    Strings.CreatedByDesc -> "The user who created the product."
    Strings.ShippingPresetDesc -> "The shipping preset for the product, coming from category shipping settings"
    Strings.ImproveWithAi -> "Improve with AI"
    Strings.NoOtherTagsToChooseFrom -> key.name.enumCapitalized()
    Strings.Tag -> key.name.enumCapitalized()
    Strings.InProducts -> key.name.enumCapitalized()
    Strings.AddImage -> key.name.enumCapitalized()
    Strings.DeleteExplain -> "Are you sure you want to delete this? You cannot undo this action."
    Strings.ChargeTax -> key.name.enumCapitalized()
    Strings.ChargeTaxDesc -> "Indicates whether or not the product should have tax charged on it."
    Strings.Title -> key.name.enumCapitalized()
    Strings.UseGlobalTracking -> key.name.enumCapitalized()
    Strings.UseGlobalTrackingDesc -> "Indicates whether or not the product should use the global tracking settings."
    Strings.Media -> key.name.enumCapitalized()
    Strings.Info -> key.name.enumCapitalized()
    Strings.Insights -> key.name.enumCapitalized()
    Strings.NoInsights -> "Insights will display when the product has had recent sales"
    Strings.ProductOrganization -> key.name.enumCapitalized()
    Strings.CategoryOrganization -> key.name.enumCapitalized()
    Strings.AddressDesc -> "The primary address of this customer"
    Strings.CollectTax -> key.name.enumCapitalized()
    Strings.MarketingEmailsAgreed -> "Customer agreed to receive marketing emails."
    Strings.MarketingSMSAgreed -> "Customer agreed to receive SMS marketing text messages."
    Strings.LastName -> key.name.enumCapitalized()
    Strings.DiscardAllUnsavedChanges -> key.name.enumCapitalized()
    Strings.DiscardAllUnsavedChangesDesc -> "If you discard changes, you’ll delete any edits you made since you last saved."
    Strings.ContinueEditing -> key.name.enumCapitalized()
    Strings.DiscardChanges -> key.name.enumCapitalized()
    Strings.LanguageDesc -> "This customer will receive notifications in this language."
    Strings.MarketingDesc -> "You should ask your customers for permission before you subscribe them to your marketing emails or SMS."
    Strings.LeavePageWithUnsavedChanges -> "Leave page with unsaved changes?"
    Strings.LeavingThisPageWillDiscardAllUnsavedChanges -> "Leaving this page will discard all unsaved changes."
    Strings.LeavePage -> key.name.enumCapitalized()
    Strings.Stay -> key.name.enumCapitalized()
    Strings.Ticker -> "ECO-FRIENDLY CLOTHING. 100% COTTON"
    Strings.UnitedKingdom -> "United Kingdom"
    Strings.Admin -> key.name.enumCapitalized()
    Strings.Store -> key.name.enumCapitalized()
    Strings.Woman -> key.name.enumCapitalized()
    Strings.Man -> key.name.enumCapitalized()
    Strings.ShippingReturns -> "Shipping & Returns"
    Strings.About -> key.name.enumCapitalized()
    Strings.Sale -> key.name.enumCapitalized()
    Strings.ShopNow -> key.name.enumCapitalized()
    Strings.FollowUs -> key.name.enumCapitalized()
    Strings.CanWeHelpYou -> key.name.enumCapitalized()
    Strings.StartChat -> key.name.enumCapitalized()
    Strings.From -> key.name.enumCapitalized()
    Strings.To -> key.name.enumCapitalized()
    Strings.Tel -> key.name.enumCapitalized()
    Strings.SendEmail -> key.name.enumCapitalized()
    Strings.WeWillReply -> "We'll reply ASAP."
}
