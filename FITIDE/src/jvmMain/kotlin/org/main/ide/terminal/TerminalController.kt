package org.main.ide.terminal

import com.jediterm.terminal.TtyConnector
import com.jediterm.terminal.ui.JediTermWidget
import com.pty4j.PtyProcess
import com.pty4j.PtyProcessBuilder
import java.nio.charset.StandardCharsets

class TerminalController(workingDirectory: String) {

    val process: PtyProcess
    val widget: JediTermWidget

    init {
        val shell = when {
            System.getProperty("os.name").startsWith("Windows") ->
                arrayOf("cmd.exe")
            System.getProperty("os.name").startsWith("Mac") ->
                arrayOf("/bin/zsh", "-l")
            else ->
                arrayOf("/bin/bash", "-l")
        }

        process = PtyProcessBuilder()
            .setCommand(shell)
            .setDirectory(workingDirectory)
            .setEnvironment(
                mapOf(
                    "TERM" to "xterm-256color",
                    "COLORTERM" to "truecolor",
                    "LANG" to "en_US.UTF-8",
                    "LC_ALL" to "en_US.UTF-8"
                )
            )
            .start()

        val settings = TerminalSettings()
        widget = JediTermWidget(settings)

        widget.ttyConnector = object : TtyConnector {

            private val charset = StandardCharsets.UTF_8
            private val inputStream = process.inputStream

            override fun close() {
                process.destroy()
            }

            override fun getName(): String = "Local Terminal"

            override fun read(buf: CharArray, offset: Int, length: Int): Int {
                val tmp = ByteArray(4096)
                val bytesRead = inputStream.read(tmp)
                if (bytesRead <= 0) return bytesRead

                val text = String(tmp, 0, bytesRead, charset)
                val chars = text.toCharArray()
                val count = minOf(chars.size, length)
                System.arraycopy(chars, 0, buf, offset, count)
                return count
            }

            override fun write(bytes: ByteArray) {
                try {
                    process.outputStream.write(bytes)
                    process.outputStream.flush()
                } catch (_: Exception) {}
            }

            override fun write(string: String) {
                write(string.toByteArray(charset))
            }

            override fun isConnected(): Boolean = process.isAlive

            override fun ready(): Boolean {
                return try {
                    inputStream.available() > 0
                } catch (e: Exception) {
                    false
                }
            }

            override fun waitFor(): Int = process.waitFor()
        }

        widget.start()
    }
}
