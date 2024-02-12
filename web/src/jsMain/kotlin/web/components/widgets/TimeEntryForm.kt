//package web.components.widgets
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import com.varabyte.kobweb.compose.foundation.layout.Arrangement
//import com.varabyte.kobweb.compose.foundation.layout.Column
//import com.varabyte.kobweb.compose.foundation.layout.Row
//import com.varabyte.kobweb.compose.ui.Modifier
//import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
//import com.varabyte.kobweb.compose.ui.modifiers.width
//import com.varabyte.kobweb.silk.components.forms.Input
//import com.varabyte.kobweb.silk.components.forms.Label
//import com.varabyte.kobweb.silk.components.text.SpanText
//import org.jetbrains.compose.web.attributes.InputType
//import org.jetbrains.compose.web.css.px
//
//
//@Composable
//fun TimeEntryEditForm(
//    onNavigateBack: () -> Unit,
//) {
//    val date by viewModel.date.collectAsState()
//    val startTime = viewModel.startTime.collectAsState()
//    val endTime = viewModel.endTime.collectAsState()
//
//    val duration = viewModel.duration.collectAsState()
//
//    val project = viewModel.projectName.collectAsState()
//    val selectedProject = viewModel.projectId.collectAsState()
//    val descriptionStateFlow = viewModel.description.collectAsState()
//
//    var description by remember { mutableStateOf(descriptionStateFlow.value ?: "") }
//
//    LaunchedEffect(key1 = "edit") {
//        viewModel.getTimeEntryById()
//    }
//
//    Column(modifier = Modifier.fillMaxHeight()) {
//        Row(modifier = Modifier.width(450.px), horizontalArrangement = Arrangement.SpaceBetween) {
//            Column {
//                Label {
//                    SpanText("Date")
//                }
//                Input(
//                    InputType.Date,
//                    attrs = listOf(InputFieldStyleSmall).toAttrs {
//                            name("date")
//                            value(date.toString())
//                            onChange {
//                                viewModel.dateChanged(it.value.toLocalDate())
//                            }
//                        }
//                )
//            }
//            Column {
//                Label {
//                    SpanText("Duration")
//                }
//                Input(
//                    InputType.Time,
//                    attrs = listOf(InputFieldStyleSmall)
//                        .toAttrs {
//                            name("duration")
//                            value(duration.value.toString())
//                            onChange {
//                                viewModel.durationChanged(it.value.toLocalTime())
//                            }
//                        }
//                )
//            }
//        }
//
//
//        Row(modifier = Modifier.width(450.px), horizontalArrangement = Arrangement.SpaceBetween) {
//            Column {
//                Label {
//                    Text("Start Time")
//                }
//                Input(
//                    InputType.Time,
//                    attrs = listOf(InputFieldStyleSmall)
//                        .toAttrs {
//                            name("starttime")
//                            value(startTime.value.toString())
//                            onChange {
//                                viewModel.startTimeChanged(it.value.toLocalTime())
//                            }
//                        }
//                )
//
//            }
//            Column {
//                Label {
//                    Text("End Time")
//                }
//                Input(
//                    InputType.Time,
//                    attrs = listOf(InputFieldStyleSmall)
//                        .toAttrs {
//                            name("endtime")
//                            value(endTime.value.toString())
//                            onChange {
//                                viewModel.endTimeChanged(it.value.toLocalTime())
//                            }
//                        }
//                )
//            }
//        }
//        Column(modifier = Modifier.width(450.px), horizontalAlignment = Alignment.Start) {
//            Label {
//                Text("Description")
//            }
//            Input(
//                InputType.Text,
//                attrs = listOf(InputFieldStyleBig)
//                    .toAttrs {
//                        name("description")
//                        value(description)
//                        onChange {
//                            description = it.value
//                        }
//                    }
//            )
//
//            Label {
//                Text("Project")
//            }
//            Input(
//                InputType.Text,
//                attrs = listOf(InputFieldStyleBig)
//                    .toAttrs {
//                        name("project")
//                        value(project.value.toString())
//                        onChange {
//                            //onProjectChanged(it.value)
//                        }
//                    }
//            )
//            Button(
//                modifier = Modifier.width(450.px),
//                onClick = {
//                    viewModel.descriptionChanged(description)
//                    viewModel.updateTimeEntry()
//                    onNavigateBack()
//                }) {
//                Text("Update")
//            }
//            Button(
//                modifier = Modifier.width(450.px),
//                onClick = {
//                    viewModel.deleteTimeEntry()
//                    onNavigateBack()
//                }) {
//                Text("Delete")
//            }
//        }
//    }
//}
