package org.example.DZ4V2.Dao;

import org.example.DZ4V2.DateCreation.ObjectCreator;
import org.example.DZ4V2.DateCreation.RandomAnnotationProcessor;
import org.example.DZ4V2.Models.Post;
import org.example.DZ4V2.Models.PostComment;
import org.example.DZ4V2.Models.User;
import org.example.DZ4V2.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;


import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ModelsDao {


    public Timestamp getRandomDate() {
        long min = 1704067200000L;  // 1 января 2024 года UTC0
        long max = 1735689600000L;  // 1 января 2025 года UTC0
        long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(min, max);
        return Timestamp.from(Instant.ofEpochMilli(randomMillisSinceEpoch));
    }


    public void saveModels() {

        HibernateUtil.buildSessionFactory();
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();

                System.out.println("<=====================Создание таблицы User=======================================================================================================>");
                for (int i = 0; i < 5; i++) {
                    User user = new User();
                    user.setUserName("User" + (i + 1)); // Установка имени пользователя
                    System.out.println("Создание пользователя с именем: " + user.getUserName());
                    session.save(user);
                }

                // Коммитим транзакцию после создания пользователей
                transaction.commit();

                // Начинаем новую транзакцию для создания постов и комментариев
                transaction = session.beginTransaction();

                List<User> users = getUsers();
                if (users.isEmpty()) {
                    throw new RuntimeException("Список пользователей пустой. Не удалось создать записи Post и PostComment.");
                }

                System.out.println("<=====================Создание таблицы Post=======================================================================================================>");
                for (int i = 0; i < 5; i++) {
                    Post post = new Post();
                    post.setTitle("Title" + (i + 1)); // Установка заголовка поста
                    post.setUser(users.get(i % users.size())); // Установка пользователя для поста
//                    post.setDate(LocalDateTime.now());
                    System.out.println("Создание поста с заголовком: " + post.getTitle() +
                            ", с userID: " + post.getUser().getId() +
                            " и датой: " + post.getDate());
                    session.save(post);
                }

                // Коммитим транзакцию после создания постов
                transaction.commit();

                // Начинаем новую транзакцию для создания комментариев
                transaction = session.beginTransaction();

                List<Post> posts = getPosts();
                if (posts.isEmpty()) {
                    throw new RuntimeException("Список постов пустой. Не удалось создать записи PostComment.");
                }

                System.out.println("<=====================Создание таблицы PostComment=======================================================================================================>");
                for (int i = 0; i < 5; i++) {
                    PostComment postComment = new PostComment();
                    postComment.setText("Comment" + (i + 1)); // Установка текста комментария
                    postComment.setPost(posts.get(i % posts.size())); // Установка поста для комментария
                    postComment.setUser(users.get(i % users.size())); // Установка пользователя для комментария
//                    postComment.setDate(getRandomDate());
                    System.out.println("Создание комментария с текстом: " + postComment.getText() +
                            ", с Post ID: " + postComment.getPost().getId() +
                            ", с user ID: " + postComment.getUser().getId() +
                            " и датой: " + postComment.getDate());
                    session.save(postComment);
                }

                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }

    public List<User> getUsers() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    public List<Post> getPosts() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from Post", Post.class).list();
        }
    }

    public List<PostComment> getPostComments() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from PostComment", PostComment.class).list();
        }
    }

    // Метод для получения всех комментариев определенного поста
    public List<PostComment> getCommentsForPost(Long postId) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "from PostComment where post.id = :postId";
            return session.createQuery(hql, PostComment.class)
                    .setParameter("postId", postId)
                    .list();
        }
    }

    // Вывести все комментарии для всех публикаций
    public Map<Post, List<PostComment>> getAllPostsAndComments() {
        try (Session session = HibernateUtil.getSession()) {
            Map<Post, List<PostComment>> postCommentsMap = new HashMap<>();

            List<Post> posts = session.createQuery("from Post", Post.class).list();
            for (Post post : posts) {
                List<PostComment> comments = getCommentsForPost(post.getId());
                postCommentsMap.put(post, comments);
            }
            return postCommentsMap;
        }
    }

    // Загрузить все публикации по идентификатору юзера
    public List<Post> getPostsForUser(Long userId) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "from Post where user.id = :userId";
            return session.createQuery(hql, Post.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    // Загрузить все комментарии по идентификатору юзера
    public List<PostComment> getCommentsForUser(Long userId) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "from PostComment where user.id = :userId";
            return session.createQuery(hql, PostComment.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    // По идентификатору юзера загрузить юзеров, под чьими публикациями он оставлял комменты
    public List<User> getUsersWithCommentsByUserId(Long userId) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "select distinct u from PostComment pc " +
                    "join pc.post p " +
                    "join p.user u " +
                    "where pc.user.id = :userId";
            return session.createQuery(hql, User.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public void dropTables() {
        HibernateUtil.buildSessionFactory();
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.createNativeQuery("DROP TABLE IF EXISTS post_comment CASCADE").executeUpdate();
                session.createNativeQuery("DROP TABLE IF EXISTS post CASCADE").executeUpdate();
                session.createNativeQuery("DROP TABLE IF EXISTS users CASCADE").executeUpdate();
                transaction.commit();
                System.out.println("Базы данных удалены");
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                session.close();
                HibernateUtil.shutdown();
            }
        }
    }
}
