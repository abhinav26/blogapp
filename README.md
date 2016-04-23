# blogapp

APIs:

GET     /api/v1/blog/<blog_id>              get blog by id
POST    /api/v1/blog                        create new blog. Needs Parameters: "title", "full_content" in json body. Returns blog_id if successful.

GET     /api/v1/getallblogs                 gets all blogs (Not a good option to use when number of blogs becomes very high.)
GET     /api/v1/getblogs/<begin>/<end>      gets all blogs betweens begin and end indices. For eg: to get first five blogs, it needs to be called with begin =0, end =4. For pagination, it becomes client's responsibilty to call this with correct parameters. It also increases flexibility.

POST    /api/v1/comment                     adds new comment on corresponding paragraph of a blog. Needs Parameters: "blog_id", "paragraph_id", "comment" in json body. paragraph_id is the zero based index of the paragraph in the blog.



Code in files:

Controllers:
https://github.com/abhinav26/blogapp/blob/master/app/controllers/BlogController.java
https://github.com/abhinav26/blogapp/blob/master/app/controllers/CommentController.java

Models:
https://github.com/abhinav26/blogapp/tree/master/app/models

Conf: 
https://github.com/abhinav26/blogapp/blob/master/conf/application.conf

Routes:
https://github.com/abhinav26/blogapp/blob/master/conf/routes
