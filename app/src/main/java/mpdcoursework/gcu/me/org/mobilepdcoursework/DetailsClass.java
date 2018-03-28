package mpdcoursework.gcu.me.org.mobilepdcoursework;

/**
 * Created by camer on 18/03/2018.
 */
//S1628376 Cameron Crawford
import java.io.Serializable;

public class DetailsClass implements Serializable
{
    private String title;
    private String description;
    private String link;
    private String georss;
    private String author;
    private String comments;
    private String pubDate;

    public DetailsClass(String date)
    {
        description = date;
    }

    public DetailsClass(String atitle , String adescription, String alink, String ageorss, String aauthor, String acomments, String apubDate)
    {
        title = atitle;
        description = adescription;
        link = alink;
        georss = ageorss;
        author = aauthor;
        comments = acomments;
        pubDate = apubDate;
    }

    public DetailsClass(String atitle , String adescription, String alink)
    {
        title = atitle;
        description = adescription;
        link = alink;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getGeorss() {
        return georss;
    }

    public String getAuthor() {
        return author;
    }

    public String getComments() {
        return comments;
    }

    public String getPubDate() {
        return pubDate;
    }

}
