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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ButtonBarHorizontal(
    onConfigClick: () -> Unit = {},
    onRunClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.Transparent)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconWithTooltip(
            Res.drawable.config,
            "Config",
            onClick = { onConfigClick() }
        )
        Spacer(Modifier.width(4.dp))
        IconWithTooltip(Res.drawable.run,
            "Run",
            onClick = {onRunClick()})
    }
}