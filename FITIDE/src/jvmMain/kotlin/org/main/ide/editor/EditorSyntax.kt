package org.main.ide.editor

import androidx.compose.ui.graphics.Color
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag
import org.main.ide.uistate.UIColors

internal fun colorForTag(tag: LinkTreeCodeTag): Color =
    when (tag) {
        LinkTreeCodeTag.KeyWord         -> Color(0xFF569CD6)
        LinkTreeCodeTag.Var             -> Color(0xFF9CDCFE)
        LinkTreeCodeTag.Func            -> Color(0xFFDCDCAA)
        LinkTreeCodeTag.importStatement -> Color(0xFF4EC9B0)
        LinkTreeCodeTag.constant    -> Color(0xFFC586C0)
        LinkTreeCodeTag.Error       -> Color(0xFFFF5555)
        else               -> UIColors.TextPrimary
    }
