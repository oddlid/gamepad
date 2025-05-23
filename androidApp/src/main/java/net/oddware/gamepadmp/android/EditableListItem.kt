package net.oddware.gamepadmp.android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun EditableListItem(
    value: String,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    onClick: () -> Unit = {},
    onSelection: () -> Boolean = { false },
    itemIcon: (@Composable () -> Unit)? = null,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth(),
        ) {
            itemIcon?.let { it() }
            Box(
                modifier = modifier
                    .weight(weight = 1F)
                    .selectable(
                        selected = onSelection(),
                        onClick = onClick,
                    )
            ) {
                Text(
                    text = value,
                    modifier = modifier
                )
                if (onSelection()) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = stringResource(R.string.contentDescSelected),
                        modifier = modifier.align(Alignment.CenterEnd),
                        tint = MaterialTheme.colorScheme.surfaceTint,
                    )
                }
            }
            IconButton(onClick = onEdit) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = stringResource(R.string.eiLblEdit),
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.eiLblDelete),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}


@Preview(showBackground = true, apiLevel = 35)
@Composable
fun PreviewEditableListItem() {
    MyApplicationTheme {
        Surface {
            Column {
                EditableListItem(
                    value = "Sandra",
                    itemIcon = {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "PlayerIcon",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    },
                    onSelection = {true},
                )
            }
        }
    }
}