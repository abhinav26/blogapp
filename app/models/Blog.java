package models;

import java.util.List;

import org.jongo.MongoCollection;
import org.jongo.Oid;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import com.google.common.collect.Lists;

import uk.co.panaxiom.playjongo.PlayJongo;

public class Blog {
    public static MongoCollection blog() {
        return PlayJongo.getCollection("blog");
    }

    @MongoId
    @MongoObjectId
    private String _id;
    private String title;
    private List<Paragraph> paragraphs;
    private long createdTime;
    private boolean valid;

    public void insert() {
        blog().save(this);
    }

    public static List<Blog> findBlogsByRange(int begin, int end) {
        Iterable<Blog> blogs = blog().find("{}").skip(begin).limit(end - begin + 1).as(Blog.class);
        return Lists.newArrayList(blogs);
    }

    public static List<Blog> findAll() {
        Iterable<Blog> all = blog().find().as(Blog.class);
        return Lists.newArrayList(all);
    }

    public static Blog findById(String id) {
        return blog().findOne(Oid.withOid(id)).as(Blog.class);
    }

    public static Blog findAndUpdateById(String id) {
        return blog().findAndModify(Oid.withOid(id)).as(Blog.class);
    }
    public void updateAndAddComment(Blog blog) {
        blog().update(Oid.withOid(blog.get_id())).with(blog);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String get_id() {
        return _id;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public boolean isValid() {
        return valid;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

}
