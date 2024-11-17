package mx.ipn.escom.ia.cerradura.model;

public class Libro {
    private String bookId;
    private String authorId;
    private String authorName;
    private String title;
    private int firstPublishYear;
    private String coverUrl; // Add this field



    // Add other fields if necessary



    // Add getter and setter for coverUrl

    public String getCoverUrl() {

        return coverUrl;

    }



    public void setCoverUrl(String coverUrl) {

        this.coverUrl = coverUrl;

    }

    // Getters y Setters
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFirstPublishYear() {
        return firstPublishYear;
    }

    public void setFirstPublishYear(int firstPublishYear) {
        this.firstPublishYear = firstPublishYear;
    }
}
