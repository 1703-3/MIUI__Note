package net.micode.notes.utils.markdown;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yk.note.R;
import net.micode.notes.utils.markdown.span.BulletSpan;
import net.micode.notes.utils.markdown.span.CodeBlockSpan;
import net.micode.notes.utils.markdown.span.ListSpan;
import net.micode.notes.utils.markdown.span.QuoteSpan;
import net.micode.notes.utils.markdown.span.SplitSpan;

import java.util.ArrayList;
import java.util.List;

public class Markdown {

    private final String source;

    private final List<MD> mdList = new ArrayList<>();

    private Context context;

    private final SpannableStringBuilder builder = new SpannableStringBuilder();

    private boolean isCodeBlock = false;

    private Markdown(String source) {
        this.source = source;
    }

    public static Markdown from(String content) {
        return new Markdown(content);
    }

    //markdown文本类
    private class MD {
        //标题
        static final int TITLE = 0;

        static final int SPLIT = 1;
        static final int BULLET = 2;
        //引用
        static final int QUOTE = 3;

        static final int LIST = 4;
        //代码块
        static final int CODE_BLOCK = 5;
        static final int OTHER = 6;

        //类型
        private int type;
        //文本
        private String content;
        //接口
        private CharSequence cs;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public CharSequence getCs() {
            return cs;
        }

        public void setCs(CharSequence cs) {
            this.cs = cs;
        }

        @NonNull
        @Override
        public String toString() {
            return "type:" + type + "\ncontent:" + content + "\n";
        }
    }


    public void to(TextView tv) {
        context = tv.getContext();
        splitRow(source);
        tv.setText(builder);
    }

    //按行分割markdown文本
    private void splitRow(String markdown) {
        String[] rows = markdown.split("\n");
        for (String row : rows) {
            processRow(row);
        }
        setSS();
    }

    private void addMD(String content, int type) {
        MD md = new MD();
        md.setContent(content);
        md.setType(type);
        mdList.add(md);
    }

    private void processTitle(String row) {
        addMD(row, MD.TITLE);
    }

    private void processSplit(String row) {
        addMD(row, MD.SPLIT);
    }

    private void processBullet(String row) {
        addMD(row, MD.BULLET);
    }

    private void processQuote(String row) {
        addMD(row, MD.QUOTE);
    }

    private void processList(String row) {
        addMD(row, MD.LIST);
    }

    private void processCodeBlock(String row) {
        if (!isCodeBlock) {
            addMD(row, MD.CODE_BLOCK);
        } else {
            updateLastMD("\n" + row);
        }
        isCodeBlock = !isCodeBlock;
    }

    private void processRow(String row) {
        //正则表达式处理每一行数据
        if (row.equals("```")) {
            processCodeBlock(row);
        } else if (isCodeBlock) {
            updateLastMD(row);
        } else if (row.matches("#{1,6} .+")) {
            processTitle(row);
        } else if (row.matches("-{3,}")) {
            processSplit(row);
        } else if (row.matches("- .+")) {
            processBullet(row);
        } else if (row.matches("> .+")) {
            processQuote(row);
        } else if (row.matches("[0-9]\\. .+")) {
            processList(row);
        } else {
            processOther(row);
        }
    }
    private void processOther(String row) {
        if (row.equals("")) {
            addMD(row, MD.OTHER);
            return;
        }
        if (checkLastType()) {
            updateLastMD(row);
            return;
        }
        addMD(row, MD.OTHER);
    }



    private void updateLastMD(String content) {
        MD md = mdList.get(mdList.size() - 1);
        md.setContent(md.getContent() + "\n" + content);
    }

    private boolean checkLastType() {
        int size = mdList.size();
        if (size == 0) {
            return false;
        }
        int lastType = mdList.get(size - 1).getType();
        return lastType == MD.LIST || lastType == MD.BULLET || lastType == MD.QUOTE;
    }

    private void setSS() {
        for (MD md : mdList) {
            switch (md.getType()) {
                case MD.TITLE:
                    setTitle(md);
                    break;
                case MD.SPLIT:
                    setSplit(md);
                    break;
                case MD.BULLET:
                    setBullet(md);
                    break;
                case MD.QUOTE:
                    setQuote(md);
                    break;
                case MD.LIST:
                    setList(md);
                    break;
                case MD.CODE_BLOCK:
                    setCodeBlock(md);
                    break;
                case MD.OTHER:
                    setOther(md);
                    break;
                default:
                    break;
            }
        }
    }

    private void setTitle(MD md) {
        String content = md.getContent();
        String title = content.replace("#", "");
        int level = content.length() - title.length();
        title = title.trim();
        md.setCs(getTitleSS(title, 0, title.length(), level));
        builder.append(md.getCs()).append("\n");
    }

    private void setSplit(MD md) {
        md.setCs(getSplitSS(md.getContent()));
        builder.append(md.getCs()).append("\n");
    }

    private void setBullet(MD md) {
        String content = md.getContent();
        String bullet = content.substring(content.indexOf('-') + 1).trim();
        md.setCs(getBulletSS(bullet, 0, bullet.length()));
        builder.append(md.getCs()).append("\n");
    }

    private void setQuote(MD md) {
        String content = md.getContent();
        String quote = content.substring(content.indexOf('>') + 1).trim();
        md.setCs(getQuoteSS(quote, 0, quote.length()));
        builder.append(md.getCs()).append("\n");
    }

    private void setList(MD md) {
        String content = md.getContent();
        String list = content.substring(content.indexOf(" ") + 1);
        String indexStr = content.substring(0, content.indexOf(".") + 1);
        md.setCs(getListSS(list, 0, list.length(), indexStr));
        builder.append(md.getCs()).append("\n");
    }

    private void setCodeBlock(MD md) {
        String content = md.getContent();
        String codeBlock = content.replace("```", "");
        md.setCs(getCodeBlockSS(codeBlock, 0, codeBlock.length()));
        builder.append(md.getCs()).append("\n");
    }

    private void setOther(MD md) {
        builder.append(md.getContent()).append("\n");
    }

    private static final int[] TITLE_RES_STYLE = {
            R.style.md_title_1,
            R.style.md_title_2,
            R.style.md_title_3,
            R.style.md_title_4,
            R.style.md_title_5,
            R.style.md_title_6,
    };

    private SpannableString getTitleSS(String content, int start, int end, int level) {
        SpannableString ss = new SpannableString(content);
        TextAppearanceSpan span = new TextAppearanceSpan(context, TITLE_RES_STYLE[level]);
        ss.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private SpannableString getSplitSS(String content) {
        SpannableString ss = new SpannableString(content);
        SplitSpan span = new SplitSpan(context);
        ss.setSpan(span, 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private SpannableString getBulletSS(String content, int start, int end) {
        SpannableString ss = new SpannableString(content);
        ss.setSpan(new BulletSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private SpannableString getQuoteSS(String content, int start, int end) {
        SpannableString ss = new SpannableString(content);
        ss.setSpan(new QuoteSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private SpannableString getListSS(String content, int start, int end, String indexStr) {
        SpannableString ss = new SpannableString(content);
        ss.setSpan(new ListSpan(indexStr), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private SpannableString getCodeBlockSS(String content, int start, int end) {
        SpannableString ss = new SpannableString(content);
        ss.setSpan(new CodeBlockSpan(context), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


}
