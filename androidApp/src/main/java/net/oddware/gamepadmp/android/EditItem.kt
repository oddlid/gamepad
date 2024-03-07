package net.oddware.gamepadmp.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EditItem(
    modifier: Modifier = Modifier,
    value: String = "",
    title: String = stringResource(R.string.eiTitleDefault),
    onCancel: () -> Unit = {},
    onSave: (String) -> Unit = {},
) {
    var input by rememberSaveable { mutableStateOf(value) }

    Column {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Text(
                text = title,
                modifier = modifier,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = if (input.isBlank()) value else input,
                onValueChange = { input = it },
                singleLine = true,
                modifier = modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.eiLblName))
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    autoCorrect = false,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onSave(input) },
                ),
            )
        }
        Row {
            Spacer(modifier = modifier.weight(weight = 1F))
            OutlinedButton(onClick = onCancel, modifier = modifier) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = stringResource(R.string.btnTxtCancel)
                )
                Text(text = stringResource(R.string.btnTxtCancel))
            }
            FilledTonalButton(
                onClick = { onSave(input) },
                modifier = modifier,
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = stringResource(R.string.btnTxtSave)
                )
                Text(text = stringResource(R.string.btnTxtSave))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditItemPreview() {
    MyApplicationTheme {
        Surface {
            EditItem(
                value = "Testing",
                modifier = Modifier.padding(all = 4.dp),
            )
        }
    }
}