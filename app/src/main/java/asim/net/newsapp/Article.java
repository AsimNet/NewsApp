package asim.net.newsapp;

/**
 * Created by asimaltwijry on 4/21/17.
 */

public class Article {
    private String title;
    private String section;
    private String author;
    private String url;

    public Article(String title, String section, String author,String URL) {
        this.title = title;
        this.section = section;
        this.author = author;
        this.url = URL;
    }

    public String getTitle() {
        return title;
    }


    public String getAuthor() {
        return author;
    }

    public String getSection() {
        return section;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", section='" + section + '\'' +
                ", author='" + author + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
