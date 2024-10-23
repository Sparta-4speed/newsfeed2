package com.sparta.newsfeed.entity;

import com.sparta.newsfeed.feed.dto.FeedRequest;
import com.sparta.newsfeed.feed.dto.FeedResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "feeds")
@NoArgsConstructor
public class Feed extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @Column(nullable = false)
    private String content;

//    @Column(nullable = false)
//    private Long likes;

    // **** 유저와의 다대일 관계 ****
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Feed(Users user, String content) {
        this.user = user;
//        this.likes = 0L;
        this.content = content;
    }

    public static Feed from(Users user, FeedRequest requestDto) {

        return new Feed(user, requestDto.getContent());
    }

    public FeedResponse to() {
        return new FeedResponse(
                getId(),
                getUser().getUsername(),
                getContent(),
//                getLikes(),
                getCreatedAt(),
                getModifiedAt()
        );
    }

    public void update(String content) {

        this.content = content;
    }
}