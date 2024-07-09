package org.example.DZ4V2;

import org.example.DZ4V2.Dao.ModelsDao;
import org.example.DZ4V2.Models.Post;
import org.example.DZ4V2.Models.PostComment;
import org.example.DZ4V2.Models.User;
import org.example.DZ4V2.Util.HibernateUtil;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        System.out.println("\u001B[34m");
        ModelsDao modelsDao = new ModelsDao();
        modelsDao.saveModels();

        System.out.println("\u001B[32m");
        System.out.println("<=====================Вывод таблицы User=======================================================================================================>");
        System.out.print("\u001B[33m");
        List<User> users = modelsDao.getUsers();
        for (User user : users) {
            System.out.println("User ID: " + user.getId() +
                    ", Name: " + user.getUserName());
        }

        System.out.println("\u001B[32m");
        System.out.println("<=====================Вывод таблицы Post=======================================================================================================>");
        System.out.print("\u001B[33m");
        List<Post> posts = modelsDao.getPosts();
        for (Post post : posts) {
            System.out.println("Post_ID: " + post.getId() +
                    ", Title: " + post.getTitle() +
                    ", UserID: " + post.getUser().getId() +
                    " и Date: " + post.getDate());
        }

        System.out.println("\u001B[32m");
        System.out.println("<=====================Вывод таблицы PostComment=======================================================================================================>");
        System.out.println("\u001B[33m");
        List<PostComment> postComments = modelsDao.getPostComments();
        for (PostComment postComment : postComments) {
            System.out.println("PostComment_ID: " + postComment.getId() +
                    ", Text: " + postComment.getText() +
                    ", Post_ID: " + postComment.getPost().getId() +
                    ", User_ID: " + postComment.getUser().getId() +
                    " и Date: " + postComment.getDate());
        }

        // 3.1.1 Загрузить все комментарии (post_comment) по определенной публикации (post)
        System.out.println("\u001B[32m");
        System.out.println("<=====================3.1.1 Загрузить все комментарии (post_comment) по определенной публикации (post)=======================================================================================================>");
        System.out.println("\u001B[33m");
        Long postId = 1L; // пример ID поста

        modelsDao.getCommentsForPost(postId).forEach(comment -> System.out.println("Comment_ID: " + comment.getId() +
                ", Text: " + comment.getText() +
                ", Post_ID: " + comment.getPost().getId() +
                ", User_ID: " + comment.getUser().getId() +
                ", Date: " + comment.getDate()));

        // 3.1.2 Вывести все комментарии (post_comment) для всех публикаций (post)
        System.out.println("\u001B[32m");
        System.out.println("<=====================3.1.2 Вывести все комментарии (post_comment) для всех публикаций (post)=======================================================================================================>");
        System.out.println("\u001B[33m");
        Map<Post, List<PostComment>> postCommentsMap = modelsDao.getAllPostsAndComments();

        postCommentsMap.forEach((post, comments) -> {
            System.out.println("Post ID: " + post.getId() +
                    ", Title: " + post.getTitle() +
                    ", User ID: " + post.getUser().getId() +
                    ", Date: " + post.getDate());

            comments.forEach(comment -> {
                System.out.println("  Comment ID: " + comment.getId() +
                        ", Text: " + comment.getText() +
                        ", Post ID: " + comment.getPost().getId() +
                        ", User ID: " + comment.getUser().getId() +
                        ", Date: " + comment.getDate());
            });
        });

        // 3.2 Загрузить все публикации(post) по идентификатору юзера(user)
        System.out.println("\u001B[32m");
        System.out.println("<=====================3.2 Загрузить все публикации(post) по идентификатору юзера(user)=======================================================================================================>");
        System.out.println("\u001B[33m");
        Long userId = 4L;

        modelsDao.getPostsForUser(userId).forEach(post -> System.out.println("Post_ID: " + post.getId() +
                ", Title: " + post.getTitle() +
                ", User_ID: " + post.getUser().getId() +
                " и Date: " + post.getDate()));

        // 3.3 Загрузить все комментарии(post_comment) по идентификатору юзера(user)
        System.out.println("\u001B[32m");
        System.out.println("<=====================3.3 Загрузить все комментарии(post_comment) по идентификатору юзера(user)=======================================================================================================>");
        System.out.println("\u001B[33m");

        modelsDao.getCommentsForUser(userId).forEach(comment -> System.out.println("Comment_ID: " + comment.getId() +
                ", Text: " + comment.getText() +
                ", Post_ID: " + comment.getPost().getId() +
                ", User_ID: " + comment.getUser().getId() +
                ", Date: " + comment.getDate()));

        // 3.4 По идентификатору юзера(user) загрузить юзеров(List<User>), под чьими публикациями он оставлял комменты.
        System.out.println("\u001B[32m");
        System.out.println("<=====================3.4 По идентификатору юзера(user) загрузить юзеров(List<User>), под чьими публикациями он оставлял комменты.=======================================================================================================>");
        System.out.println("\u001B[33m");
        List<User> usersWithComments = modelsDao.getUsersWithCommentsByUserId(userId);
        System.out.println("Вывод пользователей, под чьими публикациями данный пользователь USER_ID, оставлял комментарии, USER_ID: " + userId);

        usersWithComments.forEach(user -> System.out.println("User ID: " + user.getId() + ", Name: " + user.getUserName()));

//        // Удалить все таблицы
//        System.out.println("\u001B[32m");
//        System.out.println("<=====================Удаление таблиц=======================================================================================================>");
//        System.out.println("\u001B[33m");
//        modelsDao.dropTables();
    }
}
