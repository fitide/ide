package org.main.ide.editor

import androidx.compose.ui.graphics.Color
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.*
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag
import org.main.ide.uistate.UIColors

internal fun colorForTag(tag: LinkTreeCodeTag): Color =
    when (tag) {
        is KeyWord         -> Color(0xFF569CD6)
        is Var             -> Color(0xFF9CDCFE)
        is Func            -> Color(0xFFDCDCAA)
        is ImportStatement -> Color(0xFF4EC9B0)
        is Construction    -> Color(0xFFC586C0)
        is ErrorNode       -> Color(0xFFFF5555)
        else               -> UIColors.TextPrimary
    }
