package net.micode.notes.utils;

import android.content.Context;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class DisplayUtil {

    //像素显示设置

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5);
    }

    public static List<String> getRowList(float width, String src, Paint paint) {
        List<String> rowList = new ArrayList<>();
        int start = 0;
        for (int i = 1; i < src.length(); i++) {
            String row = src.substring(start, i);
            if (i == src.length() - 1 && paint.measureText(row) < width) {
                rowList.add(row);
            }
            if (paint.measureText(row) >= width) {
                rowList.add(row);
                start = i;
            }
        }
        return rowList;
    }
}
