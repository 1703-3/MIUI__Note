package net.micode.notes.utils.markdown.span;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;

public class ListSpan implements LeadingMarginSpan {

    private static final int MARGIN_WIDTH = 40;
    private static final int TEXT_WIDTH = 20;
    private static final int GAP_WIDTH = 40;

    private String indexStr;

    public ListSpan(String indexStr) {
        this.indexStr = indexStr;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return MARGIN_WIDTH + TEXT_WIDTH + GAP_WIDTH;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = p.getStyle();
            int color = p.getColor();

            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.BLACK);

            c.drawText(indexStr, x + MARGIN_WIDTH - TEXT_WIDTH / 2, baseline, p);

            p.setStyle(style);
            p.setColor(color);
        }
    }
}
