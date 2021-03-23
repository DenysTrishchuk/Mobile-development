package ua.kpi.comsys.iv8127.android_prog.ui.lab3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.RawRes;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import ua.kpi.comsys.iv8127.android_prog.R;

public class BookInfo {
    @SuppressLint("ClickableViewAccessibility")
    public void showPopupWindow(final View view, Book book) {
        view.getContext();
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View popupView = inflater.inflate(R.layout.popup_book_info, null);

        try {
            parseBookInfo(readTextFile(popupView.getContext(),
                    getResId("book_" + book.getIsbn13(), R.raw.class)), book);
        } catch (ParseException e) {
            System.err.println("Incorrect content of JSON file!");
            e.printStackTrace();
        }

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        if (book.getImagePath().length() != 0){
            ((ImageView) popupView.findViewById(R.id.book_info_image)).setImageResource(
                    getResId(book.getImagePath().toLowerCase()
                            .split("\\.")[0], R.drawable.class));
        }
        ((TextView) popupView.findViewById(R.id.book_info_authors)).setText(book.getAuthors());
        ((TextView) popupView.findViewById(R.id.book_info_description)).setText(book.getDesc());
        ((TextView) popupView.findViewById(R.id.book_info_pages)).setText(book.getPages());
        ((TextView) popupView.findViewById(R.id.book_info_publisher)).setText(book.getPublisher());
        ((TextView) popupView.findViewById(R.id.book_info_rating)).setText(book.getRating());
        ((TextView) popupView.findViewById(R.id.book_info_subtitle)).setText(book.getSubtitle());
        ((TextView) popupView.findViewById(R.id.book_info_title)).setText(book.getTitle());
        ((TextView) popupView.findViewById(R.id.book_info_year)).setText(book.getYear());
    }

    private void parseBookInfo(String jsonText, Book book) throws ParseException {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonText);
        book.setAuthors((String) jsonObject.get("authors"));
        book.setPublisher((String) jsonObject.get("publisher"));
        book.setDesc((String) jsonObject.get("desc"));
        book.setPages((String) jsonObject.get("pages"));
        book.setRating(jsonObject.get("rating") + "/5");
        book.setYear((String) jsonObject.get("year"));
    }

    private static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static String readTextFile(Context context, @RawRes int id){
        InputStream inputStream = context.getResources().openRawResource(id);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int size;
        try {
            while ((size = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, size);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            System.err.println("FIle cannot be reading!");
            e.printStackTrace();
        }
        return outputStream.toString();
    }
}
