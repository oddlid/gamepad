package net.oddware.gamepadmp.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoadingError(
    modifier: Modifier = Modifier,
    text: String = "Loading error",
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            modifier = modifier,
        )
        Icon(
            Icons.Default.Info,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.error,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLoadingError() {
    MyApplicationTheme {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp)
        ) {
            LoadingError(
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoadingIndicator() {
    MyApplicationTheme {
        Surface {
            Row(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            ) {
                LoadingIndicator()
            }
        }
    }
}