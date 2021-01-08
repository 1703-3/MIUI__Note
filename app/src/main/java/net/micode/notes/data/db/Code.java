package net.micode.notes.data.db;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;


public class Code {
    /**
     * 随机数数组
     * 去除了易混淆的 数字 0 和 字母 o O
     *                数字 6 和 字母 b
     *                数字 9 和 字母 q
     *                字母 c C 和 G
     *                字母 t （经常和随机线混在一起看不清）
     */
    private static final char[] CHARS = {
            '1',    '2',    '3',    '4',    '5',    '7',    '8',
            'a',    'b',    'd',    'e',    'f',    'g',    'h',    'j',    'k',    'm',
            'n',    'p',    'r',    's',    'u',    'v',    'w',    'x',    'y',    'z',
            'A',    'B',    'D',    'E',    'F',    'H',    'J',    'K',    'M',
            'N',    'P',    'Q',    'R',    'S',    'T',    'U',    'V',    'W',    'X',
            'Y',    'Z'
    };

    private static Code bmpCode;

    //实例化对象
    public static Code getInstance() {
        if(bmpCode == null)
            bmpCode = new Code();
        return bmpCode;
    }


    //验证码默认随机数的个数
    private static final int DEFAULT_CODE_LENGTH = 4;
    //默认字体大小
    private static final int DEFAULT_FONT_SIZE = 25;
    //默认线条的条数
    private static final int DEFAULT_LINE_NUMBER = 5;
    //padding值
    private static final int BASE_PADDING_LEFT = 10, RANGE_PADDING_LEFT = 15, BASE_PADDING_TOP = 15, RANGE_PADDING_TOP = 20;
    //验证码的默认宽高
    private static final int DEFAULT_WIDTH = 100, DEFAULT_HEIGHT = 40;


    private final int width = DEFAULT_WIDTH;
    private final int height = DEFAULT_HEIGHT;

    private int range_padding_left = RANGE_PADDING_LEFT;
    private int base_padding_top = BASE_PADDING_TOP;
    private int range_padding_top = RANGE_PADDING_TOP;

    private int line_number = DEFAULT_LINE_NUMBER;
    private int font_size = DEFAULT_FONT_SIZE;


    private String code;
    private int padding_left, padding_top;
    private Random random = new Random();

    //生成验证码
    private String createCode() {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < DEFAULT_CODE_LENGTH; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }




    public String getCode() {
        return code;
    }




    //画干扰线
    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = random.nextInt(width);
        int startY = random.nextInt(height);
        int stopX = random.nextInt(width);
        int stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    //生成随机颜色
    private int randomColor() {
        int red = random.nextInt(256) ;
        int green = random.nextInt(256);
        int blue = random.nextInt(256) ;
        return Color.rgb(red, green, blue);
    }


    //随机生成文字样式，颜色，粗细，倾斜度
    private void randomTextStyle(Paint paint) {

        int color = randomColor();
        paint.setColor(color);
        //true为粗体，false为非粗体
        paint.setFakeBoldText(random.nextBoolean());
        float skewX =(float)(random.nextInt(11) / 10);
        skewX = random.nextBoolean() ? skewX : -skewX;

        //设置斜体
        paint.setTextSkewX(skewX);
    }

    //随机生成padding值
    private void randomPadding() {

        padding_left += BASE_PADDING_LEFT + random.nextInt(range_padding_left);
        padding_top = base_padding_top + random.nextInt(range_padding_top);
    }
    //验证码图片
    public Bitmap createBitmap() {
        padding_left = 0;

        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bp);

        code = createCode();

        c.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(font_size);

        //画验证码
        for (int i = 0; i < code.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            c.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
        }
        //画线条
        for (int i = 0; i < line_number; i++) {
            drawLine(c, paint);
        }


        c.save();//保存
        c.restore();
        return bp;
    }
}

