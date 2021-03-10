package ua.kpi.comsys.iv8127.android_prog.ui.lab3;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.lang.reflect.Field;

import ua.kpi.comsys.iv8127.android_prog.R;

public class BookShelf {

    public ConstraintLayout bookShelf;

    public BookShelf(Context context, LinearLayout bookList, Book book){
        bookShelf = newBookShelf(context, bookList, book);
    }

    private ConstraintLayout newBookShelf(Context context, LinearLayout bookList, Book book){
        ConstraintLayout bookLayTmp = new ConstraintLayout(context);
        bookLayTmp.setBackgroundResource(R.drawable.outline_bookshelf);
        bookLayTmp.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        bookList.addView(bookLayTmp);

        ImageView imageTmp = new ImageView(context);
        imageTmp.setId(imageTmp.hashCode());
        if (book.getImagePath().length() != 0)
            imageTmp.setImageResource(
                    getResId(book.getImagePath().toLowerCase().split("\\.")[0], R.drawable.class));
        ConstraintLayout.LayoutParams imgParams =
                new ConstraintLayout.LayoutParams(300, 300);
        bookLayTmp.addView(imageTmp, imgParams);

        TextView textTitle = new TextView(context);
        textTitle.setText(book.getTitle());
        textTitle.setEllipsize(TextUtils.TruncateAt.END);
        textTitle.setMaxLines(1);
        textTitle.setId(textTitle.hashCode());
        bookLayTmp.addView(textTitle, new ConstraintLayout.LayoutParams(
                                                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                                                ConstraintLayout.LayoutParams.WRAP_CONTENT));

        TextView textSubtitle = new TextView(context);
        textSubtitle.setText(book.getSubtitle());
        textSubtitle.setEllipsize(TextUtils.TruncateAt.END);
        textSubtitle.setMaxLines(4);
        textSubtitle.setId(textSubtitle.hashCode());
        bookLayTmp.addView(textSubtitle, new ConstraintLayout.LayoutParams(
                                                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                                                ConstraintLayout.LayoutParams.WRAP_CONTENT));

        TextView textPrice = new TextView(context);
        textPrice.setText(book.getPrice());
        textPrice.setId(textPrice.hashCode());
        bookLayTmp.addView(textPrice, new ConstraintLayout.LayoutParams(
                                                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                                                ConstraintLayout.LayoutParams.WRAP_CONTENT));

        ConstraintSet textConstraintSet = new ConstraintSet();
        textConstraintSet.clone(bookLayTmp);

        textConstraintSet.connect(imageTmp.getId(), ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START);
        textConstraintSet.connect(imageTmp.getId(), ConstraintSet.TOP,
                ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        textConstraintSet.connect(imageTmp.getId(), ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

        textConstraintSet.connect(textTitle.getId(), ConstraintSet.START,
                imageTmp.getId(), ConstraintSet.END);
        textConstraintSet.connect(textTitle.getId(), ConstraintSet.TOP,
                imageTmp.getId(), ConstraintSet.TOP);
        textConstraintSet.connect(textTitle.getId(), ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END);
        textConstraintSet.connect(textTitle.getId(), ConstraintSet.BOTTOM,
                imageTmp.getId(), ConstraintSet.BOTTOM);

        textConstraintSet.connect(textSubtitle.getId(), ConstraintSet.START,
                textTitle.getId(), ConstraintSet.START);
        textConstraintSet.connect(textSubtitle.getId(), ConstraintSet.TOP,
                textTitle.getId(), ConstraintSet.BOTTOM);
        textConstraintSet.connect(textSubtitle.getId(), ConstraintSet.END,
                textTitle.getId(), ConstraintSet.END);

        textConstraintSet.connect(textPrice.getId(), ConstraintSet.START,
                textSubtitle.getId(), ConstraintSet.START);
        textConstraintSet.connect(textPrice.getId(), ConstraintSet.TOP,
                textSubtitle.getId(), ConstraintSet.BOTTOM);
        textConstraintSet.connect(textPrice.getId(), ConstraintSet.END,
                textSubtitle.getId(), ConstraintSet.END);

        textConstraintSet.setMargin(textTitle.getId(), ConstraintSet.START, 8);
        textConstraintSet.setMargin(textTitle.getId(), ConstraintSet.END, 8);
        textConstraintSet.setVerticalBias(textTitle.getId(), 0.15f);

        textConstraintSet.setMargin(textSubtitle.getId(), ConstraintSet.TOP, 8);

        textConstraintSet.setMargin(textPrice.getId(), ConstraintSet.TOP, 24);

        textConstraintSet.applyTo(bookLayTmp);

        return bookLayTmp;
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
