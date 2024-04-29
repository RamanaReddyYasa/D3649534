package uk.ac.tees.mad.d3649534.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckboxList(
    items: List<String>,
    onSelectionChange: (List<String>) -> Unit
) {
    val selectedItems = remember { mutableStateListOf<String>() }

    Column {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = selectedItems.contains(item),
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                selectedItems.add(item)
                            } else {
                                selectedItems.remove(item)
                            }
                            onSelectionChange(selectedItems)
                        }
                    )
                    Text(text = item)
                }
            }
        }
    }
}
