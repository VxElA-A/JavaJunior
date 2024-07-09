package org.example.DZ4V2.Models;

import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "users")
public class User {

    private static <T> T getRandom(List<? extends T> items) {
        int index = ThreadLocalRandom.current().nextInt(0, items.size());
        return items.get(index);
    }

    private static Long count = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "name")
    private String userName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public User() {
        this.setId(count++);
        List<String> names = List.of("Vasya", "Petya", "Sasha", "Dasha", "Lena", "Olya", "Vlad");
        this.setUserName(getRandom(names));
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}

