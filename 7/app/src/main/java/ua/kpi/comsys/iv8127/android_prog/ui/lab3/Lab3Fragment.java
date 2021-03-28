package ua.kpi.comsys.iv8127.android_prog.ui.lab3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ua.kpi.comsys.iv8127.android_prog.R;
import ua.kpi.comsys.iv8127.android_prog.ui.lab7.App;
import ua.kpi.comsys.iv8127.android_prog.ui.lab7.AppDatabase;
import ua.kpi.comsys.iv8127.android_prog.ui.lab7.BooksTable;
import ua.kpi.comsys.iv8127.android_prog.ui.lab7.SearchTable;

public class Lab3Fragment extends Fragment {
    private static View root;
    private static LinearLayout bookList;
    private static HashMap<SwipeLayout, Book> booksLinear;
    private static TextView noItems;
    private static ProgressBar loadingBar;
    private static Set<SwipeLayout> removeSet;
    private static AppDatabase database;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_lab3, container, false);
        setRetainInstance(true);
        bookList = root.findViewById(R.id.scroll_lay);
        booksLinear = new HashMap<>();

        noItems = root.findViewById(R.id.no_books_view);
        loadingBar = root.findViewById(R.id.no_items_progressbar);

        removeSet = new HashSet<>();

        SearchView searchView = root.findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                removeSet.addAll(booksLinear.keySet());
                if (query.length() >= 3) {
                    AsyncLoadBooks aTask = new AsyncLoadBooks();
                    loadingBar.setVisibility(View.VISIBLE);
                    noItems.setVisibility(View.GONE);
                    aTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, query);
                }
                else {
                    for (SwipeLayout swipeLayout : removeSet) {
                        binClicked(swipeLayout);
                    }
                    removeSet.clear();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                removeSet.addAll(booksLinear.keySet());
                if (query.length() >= 3) {
                    AsyncLoadBooks aTask = new AsyncLoadBooks();
                    loadingBar.setVisibility(View.VISIBLE);
                    noItems.setVisibility(View.GONE);
                    aTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, query);
                }
                else {
                    for (SwipeLayout swipeLayout : removeSet) {
                        binClicked(swipeLayout);
                    }
                    removeSet.clear();
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
                    noItems.setVisibility(View.GONE);

                    popupWindow.dismiss();
                }
                else{
                    Toast.makeText(getActivity(), "Incorrect data!",
                            Toast.LENGTH_LONG).show();
                }
            });
        });

        changeLaySizes();
        database = App.getInstance().getDatabase();

        return root;
    }

    protected static void loadBooks(ArrayList<Book> books){
        if (books != null) {
            for (SwipeLayout swipeLayout : removeSet) {
                binClicked(swipeLayout);
            }
            removeSet.clear();
            if (books.size() > 0) {
                noItems.setVisibility(View.GONE);
                for (Book book :
                        books) {
                    Object[] tmp = new BookShelf(root.getContext(), bookList, book).bookShelf;

                    booksLinear.put((SwipeLayout) tmp[0], (Book)tmp[1]);
                }
            } else {
                noItems.setVisibility(View.VISIBLE);
            }
        }
        else {
            noItems.setVisibility(View.VISIBLE);
            Toast.makeText(root.getContext(), "Cannot load data!", Toast.LENGTH_LONG).show();
        }
        loadingBar.setVisibility(View.GONE);
    }

    public static void binClicked(SwipeLayout swipeLayout){
        booksLinear.remove(swipeLayout);
        bookList.removeView(swipeLayout);
        if (booksLinear.keySet().isEmpty()){
            noItems.setVisibility(View.VISIBLE);
        }
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

    private static class AsyncLoadBookToDB extends AsyncTask<Book, Void, Void> {
        @Override
        protected Void doInBackground(Book... books) {
            BooksTable booksTable = new BooksTable(Long.parseLong(books[0].getIsbn13()),
                    books[0].getTitle(),
                    books[0].getSubtitle(),
                    books[0].getPrice(),
                    "", "", 0, "", "", 0);
            String urldisplay = books[0].getImagePath();
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            if (mIcon11 != null) {
                booksTable.setImage(mIcon11);

                database.bookDao().insert(booksTable);
                SearchTable search = database.searchTableDao().getLastSearch();
                ArrayList<Long> booksIsbn = new ArrayList<>(search.searchedBooks);
                booksIsbn.add(booksTable.getIsbn13());
                search.searchedBooks = booksIsbn;

                database.searchTableDao().update(search);
            }
            return null;
        }
    }

    private static class AsyncLoadBooks extends AsyncTask<String, Void, ArrayList<Book>> {

        private String getRequest(String url) throws IOException{
            StringBuilder result = new StringBuilder();

            URL getReq = new URL(url);
            URLConnection bookConnection = getReq.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(bookConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                result.append(inputLine).append("\n");

            in.close();
            return result.toString();
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

        private ArrayList<Book> offlineLoad(String query){
            ArrayList<Book> newBooks = new ArrayList<>();
            ArrayList<Long> isbns = database.searchTableDao().getLastByQuery(query).searchedBooks;
            for (long isbn :
                    isbns) {
                newBooks.add(database.bookDao().getByIsbn13(isbn).makeBook());
            }
            return newBooks;
        }

        private ArrayList<Book> search(String newText){
            String jsonResponse = String.format("https://api.itbook.store/1.0/search/\"%s\"", newText);
            try {
                ArrayList<Book> books = parseBooks(getRequest(jsonResponse));
                SearchTable newSearch = new SearchTable();
                newSearch.searchQueue = newText;
                newSearch.searchedBooks = new ArrayList<>();
                database.searchTableDao().insert(newSearch);

                for (Book book :
                        books) {
                    new AsyncLoadBookToDB().execute(book);
                }
                return books;
            } catch (UnknownHostException e) {

                System.err.println("Request timeout!");
                if (database.searchTableDao().getLastByQuery(newText) != null) {
                    return offlineLoad(newText);
                }

            } catch (MalformedURLException e) {
                System.err.println(String.format("Incorrect URL <%s>!", jsonResponse));
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                System.err.println("Incorrect content of JSON file!");
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected ArrayList<Book> doInBackground(String... strings) {
            String searchQueue = strings[0];
            return search(searchQueue);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            super.onPostExecute(books);
            if (books == null)
                Toast.makeText(root.getContext(), "Cannot load data!", Toast.LENGTH_SHORT).show();
            Lab3Fragment.loadBooks(books);
        }
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @SuppressLint("StaticFieldLeak")
        ImageView bmImage;
        @SuppressLint("StaticFieldLeak")
        ProgressBar loadingBar;
        @SuppressLint("StaticFieldLeak")
        Context context;

        public DownloadImageTask(ImageView bmImage, ProgressBar loadingBar, Context context) {
            this.bmImage = bmImage;
            this.loadingBar = loadingBar;
            this.context = context;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null)
                bmImage.setImageBitmap(result);
            else {
                bmImage.setBackgroundResource(R.drawable.unnamed);
                Toast.makeText(context, "Cannot load data!", Toast.LENGTH_LONG).show();
            }
            loadingBar.setVisibility(View.GONE);
            bmImage.setVisibility(View.VISIBLE);
        }
    }
}