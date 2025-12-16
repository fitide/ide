package org.main.ide.editor

import androidx.compose.ui.graphics.Color
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag
import org.main.ide.uistate.UIColors

internal fun colorForTag(tag: LinkTreeCodeTag): Color =
    when (tag) {
        LinkTreeCodeTag.KeyWord         -> Color(0xFFcc7732)
        LinkTreeCodeTag.Var             -> Color(0xFFA9B7C6)
        LinkTreeCodeTag.Func            -> Color(0xFF4A9EFF)
        LinkTreeCodeTag.importStatement -> Color(0xFFCC7832)
        LinkTreeCodeTag.constant    -> Color(0xFF9876AA)
        LinkTreeCodeTag.Error       -> Color(0xFFBC3F3C)
        else               -> UIColors.TextPrimary
    }
