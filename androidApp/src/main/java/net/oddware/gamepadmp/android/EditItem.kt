package net.oddware.gamepadmp.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EditItem(
    modifier: Modifier = Modifier,
    name: String = "",
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {},
) {
    Surface (modifier = modifier) {
        Column {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Name:", modifier = modifier)
                TextField(value = name, onValueChange = {}, modifier = modifier.fillMaxWidth())
            }
            Row {
                Spacer(modifier = modifier.weight(weight = 1F))
                Button(onClick = onCancel, modifier = modifier) {
                    Text(text = "Cancel")
                }
                ElevatedButton(onClick = onSave, modifier = modifier) {
                    Text(text = "Save")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditItemPreview() {
    MyApplicationTheme {
        EditItem(
            name = "Testing",
            modifier = Modifier.padding(all = 4.dp),
        )
    }
}