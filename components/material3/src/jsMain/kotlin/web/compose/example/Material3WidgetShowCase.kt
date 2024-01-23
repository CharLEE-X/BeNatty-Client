package web.compose.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.attributes.TextAreaWrap
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.attributes.wrap
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle.Companion.Flex
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.JustifyContent.Companion.SpaceEvenly
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.boxSizing
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.outline
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import org.w3c.files.File
import theme.MaterialTheme
import theme.SysColorScheme
import theme.TypeScaleTokens.Companion.applyStyle
import theme.appDarkColorScheme
import theme.appLightColorScheme
import theme.defaultFontScheme
import theme.roleStyle
import web.compose.example.components.BadgeShowcase
import web.compose.example.components.ButtonShowcase
import web.compose.example.components.CardShowcase
import web.compose.example.components.CheckboxShowcase
import web.compose.example.components.DialogShowcase
import web.compose.example.components.DividerShowcase
import web.compose.example.components.ElevationShowcase
import web.compose.example.components.FieldsShowcase
import web.compose.example.components.FocusRingShowcase
import web.compose.example.components.IconButtonsShowcase
import web.compose.example.components.ListboxShowcase
import web.compose.example.components.MenuShowcase
import web.compose.example.components.RippleShowcase
import web.compose.example.components.SwitchShowcase
import web.compose.example.components.TextFieldShowcase
import web.compose.extras.fileupload.FilledFileInput
import web.compose.extras.fileupload.OutlinedFileDragDropArea
import web.compose.extras.layout.BorderLayout
import web.compose.extras.panel.CollapsiblePanel
import web.compose.extras.text.LargeBody
import web.compose.extras.text.LargeDisplay
import web.compose.extras.text.LargeHeadline
import web.compose.extras.text.LargeLabel
import web.compose.extras.text.LargeTitle
import web.compose.extras.text.MediumBody
import web.compose.extras.text.MediumDisplay
import web.compose.extras.text.MediumHeadline
import web.compose.extras.text.MediumLabel
import web.compose.extras.text.MediumTitle
import web.compose.extras.text.SmallBody
import web.compose.extras.text.SmallDisplay
import web.compose.extras.text.SmallHeadline
import web.compose.extras.text.SmallLabel
import web.compose.extras.text.SmallTitle
import web.compose.material3.common.slot
import web.compose.material3.component.Fab
import web.compose.material3.component.FilledField
import web.compose.material3.component.FilledSelect
import web.compose.material3.component.Icon
import web.compose.material3.component.ModalNavigationDrawer
import web.compose.material3.component.NavigationBar
import web.compose.material3.component.NavigationDrawer
import web.compose.material3.component.NavigationTab
import web.compose.material3.component.OutlinedButton
import web.compose.material3.component.OutlinedField
import web.compose.material3.component.OutlinedSelect
import web.compose.material3.component.PrimaryTab
import web.compose.material3.component.Radio
import web.compose.material3.component.SelectOption
import web.compose.material3.component.Slider
import web.compose.material3.component.Switch
import web.compose.material3.component.Tabs
import web.compose.material3.theming.MaterialTheme

@Composable
fun Material3WidgetShowCase(
    lightScheme: SysColorScheme = appLightColorScheme,
    darkScheme: SysColorScheme = appDarkColorScheme,
) {
    var currentColorScheme by remember { mutableStateOf(lightScheme) }

    MaterialTheme(currentColorScheme, defaultFontScheme) {

        var westOpened by remember { mutableStateOf(true) }
        var eastOpened by remember { mutableStateOf(true) }

        BorderLayout({ style { backgroundColor(currentColorScheme.sysColorSurfaceContainer) } }) {
            North {
                Div({
                    style {
                        display(Flex)
                        alignItems(AlignItems.Center)
                        justifyContent(JustifyContent.Center)
                        gap(10.px)
                        padding(5.px)
                    }
                }) {
                    Text("Switch theme")
                    Switch(
                        selected = currentColorScheme != lightScheme,
                        onClick = {
                            currentColorScheme =
                                if (currentColorScheme == lightScheme)
                                    darkScheme
                                else
                                    lightScheme
                        },
                    )
                }
            }
            West {
                CollapsiblePanel(westOpened) {
                    LargeTitle("Press Home to show/hide")
                    LargeBody(loremIpsum.take(200))
                }
            }
            Center({
                style {
                    backgroundColor(currentColorScheme.sysColorSurface)
                    borderRadius(25.px)
                }
            }) {
                ShowcaseContent()
            }
            East {
                CollapsiblePanel(eastOpened, reverse = true) {
                    LargeTitle("Press Edit to show/hide")
                    LargeBody(loremIpsum.take(200))
                }
            }
            South {
                NavigationBar {
                    NavigationTab(
                        label = "Home",
                        onClick = { westOpened = !westOpened }
                    ) {
                        Icon(
                            modifier = Modifier.attrsModifier {
                                slot = "activeIcon"
                            },
                            iconIdentifier = "home"
                        )
                        Icon(
                            modifier = Modifier.attrsModifier {
                                slot = "inactiveIcon"
                            },
                            iconIdentifier = "home"
                        )
                    }
                    NavigationTab(label = "Explore") {
                        Icon(
                            modifier = Modifier.attrsModifier {
                                slot = "activeIcon"
                            },
                            iconIdentifier = "public"
                        )
                        Icon(
                            modifier = Modifier.attrsModifier {
                                slot = "inactiveIcon"
                            },
                            iconIdentifier = "public"
                        )
                    }
                    NavigationTab(
                        label = "Edit",
                        onClick = { eastOpened = !eastOpened }
                    ) {
                        Icon(
                            modifier = Modifier.attrsModifier { slot = "activeIcon" },
                            iconIdentifier = "edit_note"
                        )
                        Icon(
                            modifier = Modifier.attrsModifier { slot = "inactiveIcon" },
                            iconIdentifier = "edit_note"
                        )
                    }
                    Fab {
                        Icon(
                            modifier = Modifier.attrsModifier {
                                slot = "icon"
                            },
                            iconIdentifier = "add"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowcaseContent() {
    var activeTab by remember { mutableStateOf(0) }

    Tabs(
        activeTabIndex = activeTab,
        onChange = {
            console.info(it)
            activeTab = it.target.asDynamic().activeTabIndex
        }
    ) {
        PrimaryTab {
            Icon("check_circle")
            Text("Stable widgets")
        }
        PrimaryTab {
            Icon("circle")
            Text("Beta widgets")
        }
        PrimaryTab {
            Icon("bolt")
            Text("Alfa widgets")
        }
        PrimaryTab {
            Icon("add_circle")
            Text("Extra widgets")
        }
    }

    Div {
        when (activeTab) {
            0 -> WidgetGroup("Stable widgets") {
                Column {
                    LargeHeadline("Material Design 3 Web Components Examples")

                    LargeBody {
                        Text(
                            """
                            This pages shows examples of the Material Design 3 Components in Kotlin which wrap the web components
                            being developed at
                        """.trimIndent()
                        )

                        A(href = "https://github.com/material-components/material-web") { Text("https://github.com/material-components/material-web") }
                    }

                    Row {
                        Column(modifier = Modifier.flex(1)) {
                            DividerShowcase()
                        }
                        Column(modifier = Modifier.flex(1)) {
                            ElevationShowcase()
                        }

                        Column(modifier = Modifier.flex(1)) {
                            FocusRingShowcase()
                        }
                        Column(modifier = Modifier.flex(1)) {
                            RippleShowcase()
                        }
                        Column(modifier = Modifier.flex(1)) {
                            MenuShowcase()
                        }
                    }
                }
            }

            1 -> WidgetGroup("Beta widgets") {
                Row {
                    Column {
                        FieldsShowcase()
                    }

                    Column {
                        ButtonShowcase()
                    }

                    Column {
                        IconButtonsShowcase()
                    }

                    Column {
                        DialogShowcase()
                    }

                    Column {
                        TextFieldShowcase()
                    }

                    Column {
                        LargeTitle("Misc ")

                        LargeTitle("Icons")
                        Row {
                            Icon(
                                modifier = Modifier.attrsModifier {
                                    slot = "start"
                                },
                                iconIdentifier = "edit"
                            )
                            Icon(
                                modifier = Modifier.attrsModifier {
                                    slot = "start"
                                },
                                iconIdentifier = "delete"
                            )
                            Icon(
                                modifier = Modifier.attrsModifier {
                                    slot = "end"
                                },
                                iconIdentifier = "close"
                            )
                        }

                        CheckboxShowcase()

                        SwitchShowcase()

                        LargeTitle("Slider")
                        var sliderValue by remember { mutableStateOf(0f) }
                        Slider(
                            min = 100f,
                            max = 200f,
                            step = 2f,
                            value = sliderValue,
                            withLabel = true,
                            onChange = { sliderValue = it },
                        )
                        Slider(
                            min = 100f,
                            max = 200f,
                            step = 2f,
                            value = sliderValue,
                            withLabel = true,
                            disabled = true,
                        )

                        LargeTitle("Radio")
                        var radioValue by remember { mutableStateOf<String?>(null) }
                        val radioGroupName = "radio-group"
                        Label(null, { style { display(Flex); alignItems(AlignItems.Center) } }) {
                            Radio(
                                name = radioGroupName,
                                value = "o1",
                                checked = radioValue == "o1",
                                onChange = { radioValue = "o1" }
                            )
                            LargeLabel("Option 1", inline = true)
                        }
                        Label(null, { style { display(Flex); alignItems(AlignItems.Center) } }) {
                            Radio(
                                name = radioGroupName,
                                value = "o2",
                                checked = radioValue == "o2",
                                onChange = { radioValue = "o2" }
                            )
                            LargeLabel("Option 2", inline = true)
                        }
                        Label(null, { style { display(Flex); alignItems(AlignItems.Center) } }) {
                            Radio(
                                name = radioGroupName,
                                value = "o3",
                                checked = radioValue == "o3",
                                onChange = { radioValue = "o3" }
                            )
                            LargeLabel("Option 3", inline = true)
                        }
                        Label(null, { style { display(Flex); alignItems(AlignItems.Center) } }) {
                            Radio(
                                name = radioGroupName,
                                value = "o4",
                                disabled = true,
                                checked = radioValue == "o4",
                                onChange = { radioValue = "o4" },
                            )
                            LargeLabel("Option 4", inline = true)
                        }
                        LargeBody("Radio button selected: $radioValue")

                        LargeTitle("Select")
                        var selectedValue by remember { mutableStateOf("") }

                        FilledSelect(
                            label = "Select label",
                            supportingText = "Selected value $selectedValue",
                            onChange = {
                                selectedValue = it.currentTarget?.asDynamic()?.value ?: ""
                            }
                        ) {
                            SelectOption()
                            SelectOption(
                                value = "MD1",
                                headline = "Material Design 1"
                            )
                            SelectOption(
                                value = "MD2",
                                headline = "Material Design 2"
                            )
                            SelectOption(
                                value = "MD3",
                                headline = "Material Design 3"
                            )
                        }

                        OutlinedSelect(
                            label = "Select label",
                            supportingText = "Selected value $selectedValue",
                            onChange = {
                                selectedValue = it.currentTarget?.asDynamic()?.value ?: ""
                            }
                        ) {
                            SelectOption()
                            SelectOption(
                                value = "MD1",
                                headline = "Material Design 1"
                            )
                            SelectOption(
                                value = "MD2",
                                headline = "Material Design 2"
                            )
                            SelectOption(
                                value = "MD3",
                                headline = "Material Design 3"
                            )
                        }
                    }

                    Column {
                        ListboxShowcase()
                    }
                }
            }

            2 -> WidgetGroup("Alpha widgets") {
                Column {
                    LargeTitle("Navigation Drawer")
                    var opened by remember { mutableStateOf(false) }

                    OutlinedButton(
                        onClick = { opened = !opened },
                        modifier = Modifier.padding(5.px)
                    ) {
                        Text("${if (opened) "close" else "open"} drawer")
                    }

                    NavigationDrawer(opened = opened) {
                        LargeBody("Navigation drawer")
                    }
                }

                Column {
                    LargeTitle("Modal Navigation Drawer")

                    var opened by remember { mutableStateOf(false) }

                    OutlinedButton(
                        onClick = { opened = !opened },
                        modifier = Modifier.padding(5.px)
                    ) {
                        Text("${if (opened) "close" else "open"} drawer")
                    }

                    ModalNavigationDrawer(
                        opened = opened,
                    ) {
                        LargeBody("Modal navigation drawer")
                    }
                }

                Column {
                    BadgeShowcase()
                }

                Column(
                    modifier = Modifier.gap(1.em)
                ) {
                    CardShowcase()
                }
            }

            3 -> WidgetGroup("Extra widgets") {
                Column {
                    LargeTitle("Typography")

                    Row(modifier = Modifier.justifyContent(SpaceEvenly)) {
                        Column {
                            LargeDisplay("Large Display")
                            LargeHeadline("Large Headline")
                            LargeTitle("Large Title")
                            LargeBody("Large Body")
                            LargeLabel("Large Label")
                        }
                        Column {
                            MediumDisplay("Medium Display")
                            MediumHeadline("Medium Headline")
                            MediumTitle("Medium Title")
                            MediumBody("Medium Body")
                            MediumLabel("Medium Label")
                        }
                        Column {
                            SmallDisplay("Small Display")
                            SmallHeadline("Small Headline")
                            SmallTitle("Small Title")
                            SmallBody("Small Body")
                            SmallLabel("Small Label")
                        }
                    }
                }

                Column {
                    LargeTitle("Text area")

                    FilledField {
                        TextArea(attrs = {
                            defaultValue("Some Default Text")
                            style {
                                outline("none")
                                background("transparent")
                                boxSizing("border-box")
                                border { style = LineStyle.None }
                                width(100.percent)
                                applyStyle(MaterialTheme.typography.bodyLarge)
                            }
                            wrap(TextAreaWrap.Off)
                            rows(5)
                        }
                        )
                        Span({ slot = "supporting-text" }) { Text("Some longer supporting text") }
                    }

                    OutlinedField {
                        TextArea(attrs = Modifier
                            .roleStyle(MaterialTheme.typography.bodyLarge)
                            .toAttrs {
                                defaultValue("Some Default Text")
                                style {
                                    outline("none")
                                    background("transparent")
                                    boxSizing("border-box")
                                    border { style = LineStyle.None }
                                    width(100.percent)
                                }
                                wrap(TextAreaWrap.Off)
                                rows(5)
                            }
                        )
                        Span({ slot = "supporting-text" }) { Text("Some longer supporting text") }
                    }
                }

                var selectedFiles by remember { mutableStateOf<List<File>>(emptyList()) }
                Column {
                    LargeTitle("File input button")
                    FilledFileInput({
                        if (it == null) return@FilledFileInput
                        selectedFiles = it
                    }) { Text("Upload files...") }

                    FileList(selectedFiles)
                }
                Column {
                    LargeTitle("File drag/drop area")

                    OutlinedFileDragDropArea(
                        onDrop = { list ->
                            selectedFiles = list
                        }
                    ) {
                        if (selectedFiles.isEmpty())
                            LargeBody("Drag & drop files here!")
                        else
                            FileListInfo(selectedFiles)
                    }
                }
            }
        }
    }
}


@Composable
fun FileList(files: List<File>) {
    Column() {
        files.forEach {
            LargeLabel("Filename: ${it.name} [${it.size}, ${it.type}], ${it.lastModified}")
        }
    }
}

@Composable
fun FileListInfo(files: List<File>) {
    val totalFileSize = files.sumOf { it.size.toInt() }
    LargeLabel("Number of files: ${files.size}")
    LargeLabel("Total amount of bytes: ${formatBytes(totalFileSize)}")
}

const val loremIpsum = """
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque vel sodales leo. Morbi in ultricies ante, sit amet venenatis elit. Curabitur justo nunc, sagittis quis ullamcorper a, efficitur quis risus. Praesent at tempor libero. Maecenas tincidunt viverra justo, a finibus risus rutrum et. Donec vitae posuere tellus. Sed luctus, nulla sit amet aliquam lacinia, diam felis sagittis enim, nec vestibulum risus ipsum ac augue. Praesent faucibus ac dui in luctus. Nam vitae ex ac dui consequat ultricies ac et risus. Donec fringilla purus eget sem placerat, a faucibus mi lacinia. Sed metus tellus, iaculis ut sem id, pharetra porttitor ipsum. Morbi laoreet pellentesque dolor eu laoreet. Nam semper consectetur metus, non porttitor ex imperdiet sed. Etiam laoreet vitae ligula finibus vulputate. Nam sed sollicitudin enim, eu mollis nunc. Vestibulum facilisis mollis est ornare tincidunt.

Suspendisse potenti. In at eleifend arcu, in aliquam leo. Nunc feugiat diam sed tortor imperdiet dignissim. Praesent rutrum, lacus sed ornare luctus, eros dui placerat felis, ut condimentum ante felis vel nisl. Suspendisse varius mi ac condimentum egestas. Praesent consequat interdum ex, at dapibus dui vestibulum tempus. Phasellus finibus sollicitudin felis, id placerat urna condimentum at.

Vivamus faucibus, est commodo placerat efficitur, dolor nulla accumsan justo, at gravida elit purus non nulla. Fusce maximus imperdiet consequat. Maecenas vehicula, sem non congue cursus, lectus felis dapibus eros, at interdum mi mi sed leo. Cras suscipit efficitur faucibus. Aenean sit amet eros quis quam laoreet consequat tincidunt in ligula. Sed tincidunt libero eget massa suscipit, quis pretium justo sodales. Pellentesque aliquet, massa quis laoreet commodo, nulla justo elementum ante, eget condimentum sapien ligula at arcu. Integer feugiat at enim nec ultrices. Morbi ornare nisi in aliquet congue. Pellentesque placerat nunc vitae aliquet mollis. Vestibulum dapibus nisl vitae nisl faucibus viverra.

Pellentesque porttitor dapibus vehicula. Proin pretium nibh in condimentum condimentum. Pellentesque fringilla metus ac nulla sodales sagittis. Ut molestie nisi erat, imperdiet consectetur risus dapibus quis. Etiam a convallis libero, in tincidunt ligula. Nam volutpat elementum est sed semper. Praesent in pulvinar purus. Vivamus at nisi ac tortor pulvinar commodo et vel neque. Vivamus tempus a nulla eget facilisis. Morbi pretium porttitor mattis. Phasellus eleifend ex odio, eget pharetra diam varius quis. Donec eu nulla viverra turpis sodales sollicitudin eu consectetur dolor. In consequat leo in tincidunt consectetur.

Sed ac orci eget elit fringilla faucibus. Duis in ex sed est ornare condimentum. Vestibulum sit amet lorem ultrices, rhoncus purus ac, consectetur tortor. Suspendisse tempor cursus lacus, nec dapibus libero eleifend eu. Quisque rhoncus mauris sed porta suscipit. Vestibulum blandit dolor vel justo tristique, vitae consectetur massa vestibulum. Sed maximus lectus ex, pellentesque vestibulum quam blandit vulputate. Donec ut vulputate dolor. Vestibulum nec malesuada purus. Aenean justo velit, dictum id dapibus id, viverra in sapien. Praesent dapibus risus consequat orci consectetur ullamcorper. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Phasellus egestas nec orci non placerat. Etiam vitae erat tempor sapien tincidunt tempus vitae efficitur neque.
"""
