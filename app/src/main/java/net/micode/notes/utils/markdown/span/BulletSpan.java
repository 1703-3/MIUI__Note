package net.micode.notes.utils.markdown.span;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;

public class BulletSpan implements LeadingMarginSpan {

    private static final int MARGIN_WIDTH = 40;
    private static final int RADIUS_WIDTH = 8;
    private static final int GAP_WIDTH = 40;

    @Override
    public int getLeadingMargin(boolean first) {
        return MARGIN_WIDTH + RADIUS_WIDTH + GAP_WIDTH;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = p.getStyle();
            int color = p.getColor();

            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.BLACK);

            c.drawCircle(x + MARGIN_WIDTH + RADIUS_WIDTH / 2, (top + bottom) / 2, RADIUS_WIDTH, p);

            p.setStyle(style);
            p.setColor(color);
        }
    }
}
