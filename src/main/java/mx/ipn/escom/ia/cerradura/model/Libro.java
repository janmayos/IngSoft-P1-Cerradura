package mx.ipn.escom.ia.cerradura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libro")
public class Libro {

    @Id
    @Column(name = "book_id", nullable = false)
    private String bookId;

    @Column(name = "author_id", nullable = true)
    private String authorId;

    @Column(name = "author_name", nullable = true)
    private String authorName;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "first_publish_year", nullable = true)
    private int firstPublishYear;

    @Column(name = "cover_url", nullable = true)
    private String coverUrl;

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

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
