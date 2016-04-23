package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Blog;
import models.Comment;
import models.Paragraph;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

@Singleton
public class CommentController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public Result addNewComment() {
        JsonNode requestJson = request().body().asJson();
        JsonNode commentNode = requestJson.findValue("comment");
        JsonNode blogIdNode = requestJson.findValue("blog_id");
        JsonNode paragraphIdNode = requestJson.findValue("paragraph_id");
        ObjectNode jsonResult = Json.newObject();
        if (commentNode == null || blogIdNode == null || paragraphIdNode == null
                || commentNode.asText().isEmpty()) {
            jsonResult.put("error", "Incomplete information");
            return badRequest(Json.toJson(jsonResult));
        }
        Comment comment = new Comment();
        comment.setCreatedTime(System.currentTimeMillis());
        comment.setText(commentNode.asText());
        comment.setValid(true);
        Blog blog = Blog.findById(blogIdNode.asText());
        List<Paragraph> paras = blog.getParagraphs();
        int paraId = paragraphIdNode.asInt();
        if(paras.size() <= paraId) {
            jsonResult.put("error", "Invalid paragraph id");
            return badRequest(Json.toJson(jsonResult));
        }
        List<Comment> currentComments = paras.get(paraId).getComments();
        if(currentComments == null) {
            currentComments = new ArrayList<Comment>();
        }
        currentComments.add(comment);
        paras.get(paraId).setComments(currentComments);
        blog.updateAndAddComment(blog);
        jsonResult.put("status", "Successfully added new comment");
        return ok(Json.toJson(jsonResult));
    }
}
