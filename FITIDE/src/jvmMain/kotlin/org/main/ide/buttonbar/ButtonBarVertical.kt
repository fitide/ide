package org.main.ide.buttonbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ButtonBarVertical() {
    Box(modifier = Modifier
        .background(Color.Gray)
        .fillMaxSize()) {
        Button(onClick = {  }){
            Text("Run")
        }
        Button(onClick = {  }) {
            Text(text = "Terminal")
        }
        Button(onClick = {  }) {
            Text("Problems")
        }
    }
}