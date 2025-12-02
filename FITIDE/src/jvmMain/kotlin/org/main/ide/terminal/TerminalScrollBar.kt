import java.awt.Color
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JScrollBar
import javax.swing.JScrollPane
import javax.swing.plaf.basic.BasicScrollBarUI

fun applyCustomScrollBar(root: Component) {

    fun install(bar: JScrollBar) {
        bar.isOpaque = false
        bar.preferredSize = Dimension(6, bar.preferredSize.height)

        bar.ui = object : BasicScrollBarUI() {

            override fun configureScrollBarColors() {
                thumbColor = Color(120, 120, 120)
                trackColor = Color(0, 0, 0, 0)
            }

            override fun createDecreaseButton(o: Int): JButton =
                invisibleButton()

            override fun createIncreaseButton(o: Int): JButton =
                invisibleButton()

            override fun paintThumb(g: Graphics, c: JComponent, r: Rectangle) {
                val g2 = g as Graphics2D
                g2.color = thumbColor
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 6, 6)
            }

            override fun paintTrack(g: Graphics, c: JComponent, r: Rectangle) {}

            private fun invisibleButton(): JButton =
                JButton().apply {
                    preferredSize = Dimension(0, 0)
                    isOpaque = false
                    isBorderPainted = false
                    isContentAreaFilled = false
                    isFocusPainted = false
                }
        }

        bar.repaint()
    }

    fun scan(comp: Component) {
        if (comp is JScrollBar) {
            install(comp)
        }
        if (comp is Container) {
            for (child in comp.components) {
                scan(child)
            }
        }
    }

    scan(root)
}

fun applyDarkBackground(root: Component) {

    fun darken(comp: Component) {
        comp.background = Color(0, 0, 0)
        comp.foreground = Color(255, 255, 255)

        if (comp is JComponent) {
            comp.isOpaque = true
        }

        if (comp is JScrollPane) {
            comp.viewport.background = Color(0, 0, 0)
            comp.viewport.isOpaque = true
        }

        if (comp is Container) {
            for (child in comp.components) {
                darken(child)
            }
        }
    }

    darken(root)
}