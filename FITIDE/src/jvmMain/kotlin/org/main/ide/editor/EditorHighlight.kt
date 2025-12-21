package org.main.ide.editor

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import org.ide.IdeController
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour
import org.main.ide.uistate.UIColors

internal fun createSyntaxHighlightTransformation(
    ide: IdeController
): VisualTransformation =
    VisualTransformation { original ->

        val builder = AnnotatedString.Builder(original.text)

        for (tok in ide.getSyntaxHighlightingForCurrentFile()) {
            val pos = tok.pos ?: continue
            val tag = tok.tag ?: continue

            val start = offsetFor(pos.rowS, pos.colS, original.text)
            val end = offsetFor(pos.rowE, pos.colE, original.text)

            if (start in 0 until end && end <= original.text.length) {
                builder.addStyle(
                    SpanStyle(color = colorForTag(tag)),
                    start,
                    end
                )
            }
        }

        TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
    }

