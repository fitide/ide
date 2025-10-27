package org.main.ide.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Editor() {
    Box(modifier = Modifier
        .background(Color.Gray)
        .fillMaxSize()) {
            Text("Editor")
    }
}
