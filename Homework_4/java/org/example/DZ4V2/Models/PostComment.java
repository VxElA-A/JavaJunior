package org.example.DZ4V2.Models;

import org.example.DZ4V2.DateCreation.RandomAnnotationProcessor;
import org.example.DZ4V2.DateCreation.RandomDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "post_comment")
public class PostComment {

    private static final Long numberOfUsers = 6L;  // на единицу меньше
    private static Long count = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @RandomDate(min = 1704067200000L, max = 1794978900000L)
    @Column(name = "post_date", columnDefinition = "TIMESTAMP")
    private Timestamp date;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text, Long postId) {
        this.text = text + "#" + ThreadLocalRandom.current().nextLong(1, postId);
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public PostComment() {
        setId(count++);
        setText("Text", numberOfUsers);
//        RandomAnnotationProcessor.processAnnotationForData(this);
        this.date = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 0, 0));
    }

    public void setText(String s) {
        this.text = s;
    }
}

