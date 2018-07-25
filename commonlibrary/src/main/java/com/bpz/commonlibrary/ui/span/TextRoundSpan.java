package com.bpz.commonlibrary.ui.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;

/**
 * 文字环绕图片的实现
 * float fontSpacing=mTextView.getPaint().getFontSpacing();
 * lines = (int) (finalHeight/fontSpacing);
 * <p>
 * Build the layout with LeadingMarginSpan2
 * <p>
 * TextRoundSpan span = new TextRoundSpan(lines, finalWidth +10 );
 */
class TextRoundSpan implements LeadingMarginSpan.LeadingMarginSpan2 {
    private int margin;
    private int lines;

    TextRoundSpan(int lines, int margin) {
        this.margin = margin;
        this.lines = lines;
    }

    /**
     * Apply the margin
     */
    @Override
    public int getLeadingMargin(boolean first) {
        if (first) {
            return margin;
        } else {
            return 0;
        }
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom, CharSequence text,
                                  int start, int end, boolean first, Layout layout) {
    }


    @Override
    public int getLeadingMarginLineCount() {
        return lines;
    }
}
