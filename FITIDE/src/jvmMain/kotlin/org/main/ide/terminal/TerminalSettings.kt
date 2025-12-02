package org.main.ide.terminal

import com.jediterm.terminal.TextStyle
import com.jediterm.terminal.TerminalColor
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider

class TerminalSettings : DefaultSettingsProvider() {

    override fun getDefaultBackground(): TerminalColor =
        TerminalColor.BLACK

    override fun getDefaultForeground(): TerminalColor =
        TerminalColor.WHITE

    override fun getSelectionColor(): TextStyle =
        TextStyle(TerminalColor.WHITE, TerminalColor.rgb(60, 60, 60))

    override fun getFoundPatternColor(): TextStyle =
        TextStyle(TerminalColor.BLACK, TerminalColor.rgb(255, 255, 0))

    override fun useAntialiasing(): Boolean = true
}
