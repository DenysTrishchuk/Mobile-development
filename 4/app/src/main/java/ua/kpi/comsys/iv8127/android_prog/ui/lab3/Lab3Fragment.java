package ua.kpi.comsys.iv8127.android_prog.ui.lab3;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.daimajia.swipe.SwipeLayout;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import ua.kpi.comsys.iv8127.android_prog.R;

public class Lab3Fragment extends Fragment {
    private View root;
    private static LinearLayout bookList;
    private static HashMap<SwipeLayout, Book> booksLinear;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_lab3, container, false);
        bookList = root.findViewById(R.id.scroll_lay);
        booksLinear = new HashMap<>();

        try {
            ArrayList<Book> books = parseBooks(readTextFile(root.getContext(), R.raw.bookslist));
            for (Book book :
                    books) {
                Object[] tmp = new BookShelf(root.getContext(), bookList, book).bookShelf;
                booksLinear.put((SwipeLayout) tmp[0], (Book)tmp[1]);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SearchView searchView = root.findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int countResults = 0;
                for (SwipeLayout book :
                        booksLinear.keySet()) {
                    if (query == null){
                        book.setVisibility(View.VISIBLE);
                        countResults++;
                    }
                    else {
                        if (booksLinear.get(book).getTitle().toLowerCase()
                                .contains(query.toLowerCase()) || query.length() == 0){
                            book.setVisibility(View.VISIBLE);
                            countResults++;
                        }
                        else
                            book.setVisibility(View.GONE);
                    }
                }

                if (countResults == 0){
                    root.findViewById(R.id.no_books_view).setVisibility(View.VISIBLE);
                }
                else {
                    root.findViewById(R.id.no_books_view).setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                int countResults = 0;
                for (SwipeLayout book :
                        booksLinear.keySet()) {
                    if (query == null){
                        book.setVisibility(View.VISIBLE);
                        countResults++;
                    }
                    else {
                        if (booksLinear.get(book).getTitle().toLowerCase()
                                .contains(query.toLowerCase()) || query.length() == 0){
                            book.setVisibility(View.VISIBLE);
                            countResults++;
                        }
                        else
                            book.setVisibility(View.GONE);
                    }
                }

                if (countResults == 0){
                    root.findViewById(R.id.no_books_view).setVisibility(View.VISIBLE);
                }
                else {
                    root.findViewById(R.id.no_books_view).setVisibility(View.GONE);
                }
                return false;
            }
        });

        Button btnAddBook = root.findViewById(R.id.button_add_book);
        btnAddBook.setOnClickListener(v -> {
            BookAdd popUpClass = new BookAdd();
            Object[] popups = popUpClass.showPopupWindow(v);

            View popupView = (View) popups[0];
            PopupWindow popupWindow = (PopupWindow) popups[1];

            EditText inputTitle = popupView.findViewById(R.id.input_title);
            EditText inputSubtitle = popupView.findViewById(R.id.input_subtitle);
            EditText inputPrice = popupView.findViewById(R.id.input_price);

            Button buttonAdd = popupView.findViewById(R.id.button_add_add);
            buttonAdd.setOnClickListener(v1 -> {
                if (inputTitle.getText().toString().length() != 0 &&
                        inputSubtitle.getText().toString().length() != 0 &&
                        inputPrice.getText().toString().length() != 0) {
                    Object[] tmp = new BookShelf(root.getContext(), bookList,
                            new Book(inputTitle.getText().toString(),
                                    inputSubtitle.getText().toString(),
                                    inputPrice.getText().toString())).bookShelf;

                    booksLinear.put((SwipeLayout) tmp[0], (Book)tmp[1]);
                    changeLaySizes();

                    popupWindow.dismiss();
                }
                else{
                    Toast.makeText(getActivity(), "Incorrect data!",
                            Toast.LENGTH_LONG).show();
                }
            });
        });


        changeLaySizes();

        return root;
    }

    public static void binClicked(SwipeLayout swipeLayout){
        booksLinear.remove(swipeLayout);
        bookList.removeView(swipeLayout);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        changeLaySizes();
    }

    private void changeLaySizes(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) root.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        for (SwipeLayout bookshelf :
                booksLinear.keySet()) {
            ((ConstraintLayout)bookshelf.getChildAt(1)).getChildAt(0).setLayoutParams(
                    new ConstraintLayout.LayoutParams(width/3, width/3));
        }
    }

    public static String readTextFile(Context context, @RawRes int id){
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

    private ArrayList<Book> parseBooks(String jsonText) throws ParseException {
        ArrayList<Book> result = new ArrayList<>();

        JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonText);

        JSONArray books = (JSONArray) jsonObject.get("books");
        for (Object book : books) {
            JSONObject tmp = (JSONObject) book;
            result.add(new Book(
                    (String) tmp.get("title"),
                    (String) tmp.get("subtitle"),
                    (String) tmp.get("isbn13"),
                    (String) tmp.get("price"),
                    (String) tmp.get("image")
            ));
        }

        return result;
    }
}