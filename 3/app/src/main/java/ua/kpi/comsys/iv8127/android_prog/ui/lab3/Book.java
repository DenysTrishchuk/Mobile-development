package ua.kpi.comsys.iv8127.android_prog.ui.lab3;

public class Book {
    private final String title;
    private final String subtitle;
    private final String isbn13;
    private final String price;
    private final String imagePath;

    public Book(String title, String subtitle, String isbn13, String price, String imagePath){
        this.title = title;
        this.subtitle = subtitle;
        this.isbn13 = isbn13;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public String getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }
}
