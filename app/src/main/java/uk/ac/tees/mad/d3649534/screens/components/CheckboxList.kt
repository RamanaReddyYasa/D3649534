package uk.ac.tees.mad.d3649534.screens.components

//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ExperimentalLayoutApi
//import androidx.compose.foundation.layout.FlowRow
//import androidx.compose.foundation.layout.Row
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.unit.dp
//import uk.ac.tees.mad.d3649534.domain.DayOfWeek
//
//
//@OptIn(ExperimentalLayoutApi::class)
//@Composable
//fun CheckboxList(
//    items: List<DayOfWeek>,
//    onSelectionChange: (List<DayOfWeek>) -> Unit
//) {
//    val selectedItems = remember { mutableStateListOf<DayOfWeek>() }
//
//    Column {
//        FlowRow(
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items.forEach { item ->
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//                    Checkbox(
//                        checked = selectedItems.contains(item),
//                        onCheckedChange = { isChecked ->
//                            if (isChecked) {
//                                selectedItems.add(item)
//                            } else {
//                                selectedItems.remove(item)
//                            }
//                            onSelectionChange(selectedItems)
//                        }
//                    )
//                    Text(text = item.name.substring(0,3))
//                }
//            }
//        }
//    }
//}
