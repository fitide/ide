package org.main.ide.terminal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import com.jediterm.terminal.TextStyle
import com.jediterm.terminal.TerminalColor
import com.jediterm.terminal.TtyConnector
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider
import com.pty4j.PtyProcessBuilder
import java.nio.charset.StandardCharsets

class CustomTerminalSettingsProvider : DefaultSettingsProvider() {

    override fun getDefaultBackground(): TerminalColor {
        return TerminalColor.BLACK
    }

    override fun getDefaultForeground(): TerminalColor {
        return TerminalColor.WHITE
    }

    override fun getSelectionColor(): TextStyle {
        return TextStyle(TerminalColor.WHITE, TerminalColor.rgb(60, 60, 60))
    }

    override fun getFoundPatternColor(): TextStyle {
        return TextStyle(TerminalColor.BLACK, TerminalColor.rgb(255, 255, 0))
    }

    override fun useAntialiasing(): Boolean {
        return true
    }
}

@Composable
fun Terminal(workingDirectory: String = System.getProperty("user.home")) {
    SwingPanel(
        modifier = Modifier.fillMaxSize(),
        factory = {
            val settings = CustomTerminalSettingsProvider()
            val widget = JediTermWidget(settings)

            val shell = if (System.getProperty("os.name").startsWith("Windows")) {
                arrayOf("cmd.exe")
            } else if (System.getProperty("os.name").startsWith("Mac")) {
                arrayOf("/bin/zsh", "-l")
            } else {
                arrayOf("/bin/bash", "-l")
            }

            val ptyProcess = PtyProcessBuilder()
                .setCommand(shell)
                .setDirectory(workingDirectory)
                .setEnvironment(mapOf(
                    "TERM" to "xterm-256color",
                    "COLORTERM" to "truecolor",
                    "LANG" to "en_US.UTF-8",
                    "LC_ALL" to "en_US.UTF-8"
                ))
                .setConsole(false)
                .start()

            val connector: TtyConnector = object : TtyConnector {
                private val charset = StandardCharsets.UTF_8
                private val inputStream = ptyProcess.inputStream

                override fun close() {
                    ptyProcess.destroy()
                }

                override fun getName(): String = "Local Terminal"

                override fun read(buf: CharArray, offset: Int, length: Int): Int {
                    val byteBuffer = ByteArray(4096)
                    val bytesRead = inputStream.read(byteBuffer)

                    if (bytesRead <= 0) return bytesRead

                    val text = String(byteBuffer, 0, bytesRead, charset)
                    val chars = text.toCharArray()

                    val count = minOf(chars.size, length)
                    System.arraycopy(chars, 0, buf, offset, count)

                    return count
                }


                override fun write(bytes: ByteArray) {
                    try {
                        ptyProcess.outputStream.write(bytes)
                        ptyProcess.outputStream.flush()
                    } catch (e: Exception) {
                        // Игнорируем ошибки записи
                    }
                }

                override fun isConnected(): Boolean = ptyProcess.isAlive

                override fun write(string: String) {
                    write(string.toByteArray(charset))
                }

                override fun waitFor(): Int = ptyProcess.waitFor()

                override fun ready(): Boolean {
                    return try {
                        inputStream.available() > 0
                    } catch (e: Exception) {
                        false
                    }
                }
            }

            widget.ttyConnector = connector
            widget.start()

            widget
        }
    )
}