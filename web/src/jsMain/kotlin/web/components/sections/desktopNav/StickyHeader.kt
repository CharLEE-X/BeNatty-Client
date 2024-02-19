//package web.components.sections.desktopNav
//
//import com.varabyte.kobweb.browser.dom.observers.IntersectionObserver
//import kotlinx.browser.document
//import kotlinx.browser.window
//import org.w3c.dom.*
//import org.w3c.dom.events.EventListener
//import kotlin.js.Date
//
//class StickyHeader : HTMLElement() {
//    private var mouseInMenu = false
//    private var menuOpened = false
//    private var menuAttr = ""
//    private lateinit var header: Element
//    private var headerBounds: DOMRect = DOMRect()
//    private var currentScrollTop = 0.0
//    private var preventReveal = false
//    private var predictiveSearch: Element? = null
//    private var isScrolling: Int? = null
//
//    init {
//        val navs = this.querySelectorAll("nav")
//        navs.forEach { nav ->
//            nav.addEventListener("mouseenter", { mouseEnterMenu() })
//            nav.addEventListener("mouseleave", { mouseLeaveMenu() })
//        }
//        window.addEventListener("forStickyHeader", { forStickyHeader() }, false)
//    }
//
//    fun forStickyHeader() {
//        val scrollTop = window.pageYOffset
//        if (scrollTop > headerBounds.bottom + checkVerticalMenuPosition() && !this.closest(".shopify-section-header-sticky")) {
//            hide()
//        }
//    }
//
//    fun mouseEnterMenu() {
//        mouseInMenu = true
//    }
//
//    fun mouseLeaveMenu() {
//        mouseInMenu = false
//    }
//
//    fun connectedCallback() {
//        header = document.querySelector(".section-header")!!
//        headerBounds = DOMRect()
//        currentScrollTop = 0.0
//        preventReveal = false
//        predictiveSearch = this.querySelector("predictive-search")
//
//        val onScrollHandler = { onScroll() }
//        val hideHeaderOnScrollUp = { preventReveal = true }
//
//        this.addEventListener("preventHeaderReveal", hideHeaderOnScrollUp)
//        window.addEventListener("scroll", onScrollHandler, false)
//
//        createObserver()
//    }
//
//    fun disconnectedCallback() {
//        this.removeEventListener("preventHeaderReveal", {})
//        window.removeEventListener("scroll", {})
//    }
//
//    fun createObserver() {
//        val observer = IntersectionObserver.Options({ entries, observer ->
//            headerBounds = entries[0].intersectionRect
//            observer.disconnect()
//        })
//        observer.observe(header)
//    }
//
//    fun onScroll() {
//        val scrollTop = window.pageYOffset
//
//        if (mouseInMenu && !menuOpened) return
//
//        if (scrollTop > currentScrollTop && scrollTop > headerBounds.bottom + checkVerticalMenuPosition()) {
//            if (!preventReveal) hide() else reveal()
//        } else if (scrollTop <= headerBounds.top) {
//            reset()
//        }
//
//        currentScrollTop = scrollTop
//    }
//
//    fun hide() {
//        // Implement hide logic here
//    }
//
//    fun reveal() {
//        // Implement reveal logic here
//    }
//
//    fun reset() {
//        // Implement reset logic here
//    }
//
//    fun checkVerticalMenuPosition(): Double {
//        val menu = this.querySelector(".vertical-menu") ?: return 0.0
//        if (!menu.hasAttribute("data-menu-opened") && !menu.classList.contains("menu-opening")) return 0.0
//        return (menu.querySelector("vertical-menu") as? HTMLElement)?.offsetHeight?.toDouble() ?: 0.0
//    }
//}
