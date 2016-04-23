package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Blog;
import models.Paragraph;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

@Singleton
public class BlogController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public Result addNewBlog() {
        JsonNode requestJson = request().body().asJson();
        JsonNode completeContentNode = requestJson.findValue("full_content");
        JsonNode titleNode = requestJson.findValue("title");
        ObjectNode jsonResult = Json.newObject();
        if (completeContentNode == null || titleNode == null ||
                completeContentNode.asText().isEmpty() || titleNode.asText().isEmpty()) {
            jsonResult.put("error", "Title or content field is empty");
            return badRequest(Json.toJson(jsonResult));
        }
        String title = titleNode.asText();
        String content = completeContentNode.asText();
        List<Paragraph> paragraphs = new ArrayList<Paragraph>();
        for (String p : content.split("\n\n")) {
            Paragraph para = new Paragraph();
            para.setContent(p);
            para.setValid(true);
            paragraphs.add(para);
        }
        Blog blog = new Blog();
        blog.setCreatedTime(System.currentTimeMillis());
        blog.setParagraphs(paragraphs);
        blog.setTitle(title);
        blog.setValid(true);
        blog.insert();
        jsonResult.put("blog_id", blog.get_id());
        jsonResult.put("status", "Successfully added new blog");
        return ok(Json.toJson(jsonResult));
    }

    public Result getAllBlogs() {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        ObjectNode jsonResult = Json.newObject();
        List<Blog> blogs = Blog.findAll();
        String result = "";
        try {
            result = mapper.writeValueAsString(blogs);
            JsonParser jp = factory.createJsonParser(result);
            JsonNode node = mapper.readTree(result);
            jsonResult.set("blogs", node);
            return ok(Json.toJson(jsonResult));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonResult.put("error", "Something went wrong");
        return badRequest(Json.toJson(jsonResult));
    }

    public Result getBlogsInRange(Integer begin, Integer end) {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        ObjectNode jsonResult = Json.newObject();
        List<Blog> blogs = Blog.findBlogsByRange(begin, end);
        String result = "";
        try {
            result = mapper.writeValueAsString(blogs);
            JsonParser jp = factory.createJsonParser(result);
            JsonNode node = mapper.readTree(result);
            jsonResult.set("blogs", node);
            return ok(Json.toJson(jsonResult));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonResult.put("error", "Something went wrong");
        return badRequest(Json.toJson(jsonResult));
    }

    public Result getBlogById(String id) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonResult = Json.newObject();
        Blog blog = Blog.findById(id);
        String result = "";
        try {
            result = mapper.writeValueAsString(blog);
            return ok(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        jsonResult.put("error", "Something went wrong");
        return badRequest(Json.toJson(jsonResult));
    }
}
