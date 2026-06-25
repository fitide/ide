package org.ide.editor

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object UIUpdater {
    private val mainScope = MainScope()

    fun runOnMain(action: () -> Unit) {
        println("UiDispatcher: runOnMain called, current thread: ${Thread.currentThread().name}")
        mainScope.launch {
            println("UiDispatcher: inside launch, thread: ${Thread.currentThread().name}")
            action()
        }
    }
}