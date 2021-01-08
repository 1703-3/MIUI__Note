package net.micode.notes.utils.markdown.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import net.micode.notes.utils.DisplayUtil;

public class CodeSpan implements LineBackgroundSpan {

    private final float mTextSize;

    public CodeSpan(Context context) {
        mTextSize = DisplayUtil.sp2px(context, 20);
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        Paint.Style style = p.getStyle();
        int color = p.getColor();
        float textSize = p.getTextSize();

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.GRAY);
        p.setTextSize(mTextSize);

        int textWidth = (int) p.measureText(text, start, end);

        c.drawRect(left, top, left + textWidth, bottom, p);

        p.setTextSize(textSize);
        p.setStyle(style);
        p.setColor(color);
    }
}
