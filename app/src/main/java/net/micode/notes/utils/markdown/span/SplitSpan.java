package net.micode.notes.utils.markdown.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SplitSpan extends ReplacementSpan {

    private int width;

    public SplitSpan(Context context) {
        super();
        width = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return width;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        Paint.Style style = paint.getStyle();
        int color = paint.getColor();

        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(255 / 2, 0, 0, 0);

        canvas.drawLine(x, top, width, top, paint);

        paint.setStyle(style);
        paint.setColor(color);
    }

}
