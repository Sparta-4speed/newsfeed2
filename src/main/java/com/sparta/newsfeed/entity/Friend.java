package com.sparta.newsfeed.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Friends")
@NoArgsConstructor
@Getter
@Setter
public class Friend extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users fromUser;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private Users toUser;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    @Builder
    public Friend(Users fromUser, Users toUser, FriendshipStatus status) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.status = status;
    }
}
