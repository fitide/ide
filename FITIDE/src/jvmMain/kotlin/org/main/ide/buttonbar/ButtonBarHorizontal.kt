package org.main.ide.buttonbar

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ButtonBarHorizontal() {
    Box(){
        Button(onClick = {  }) {
            Text("Config")
        }
        Button(onClick = {  }) {
            Text("Run")
        }
        Button(onClick = {  }) {
            Text("Debug")
        }
    }
}