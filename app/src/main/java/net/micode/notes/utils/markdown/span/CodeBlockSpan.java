package net.micode.notes.utils.markdown.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;

public class CodeBlockSpan implements LeadingMarginSpan {

    private static final int GAP_WIDTH = 40;

    private final int width;

    public CodeBlockSpan(Context context) {
        width = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return GAP_WIDTH;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        Paint.Style style = p.getStyle();
        int color = p.getColor();

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLACK);

        c.drawRect(x, top, width, bottom, p);

        p.setStyle(style);
        p.setColor(color);
    }
}
