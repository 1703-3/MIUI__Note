package net.micode.notes.utils.markdown.span;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;

public class QuoteSpan implements LeadingMarginSpan {

    private static final int LINE_WIDTH = 20;
    private static final int GAP_WIDTH = 40;

    @Override
    public int getLeadingMargin(boolean first) {
        return LINE_WIDTH + GAP_WIDTH;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        Paint.Style style = p.getStyle();
        int color = p.getColor();

        p.setStyle(Paint.Style.FILL);

        p.setColor(Color.GRAY);
        c.drawRect(x, top, x + LINE_WIDTH, bottom, p);

        p.setStyle(style);
        p.setColor(color);
    }
}
