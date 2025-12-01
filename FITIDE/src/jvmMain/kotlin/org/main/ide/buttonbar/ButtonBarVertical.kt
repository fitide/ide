package org.main.ide.buttonbar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ide.fitide.generated.resources.*
import org.main.ide.uistate.UIState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ButtonBarVertical(uiState: UIState) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(60.dp)
            .background(Color.Gray)
            .padding(4.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconWithTooltip(
            Res.drawable.terminal,
            "Terminal",
            onClick = {
                uiState.toggleTerminal()
            }
        )
    }
}