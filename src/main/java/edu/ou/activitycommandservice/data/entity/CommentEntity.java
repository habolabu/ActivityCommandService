package edu.ou.activitycommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(
        name = "Comment",
        schema = "ActivityCommandService"
)
public class CommentEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "content")
    private String content;
    @Basic
    @Column(name = "totalEmotion")
    private int totalEmotion = 0;
    @Basic
    @Column(name = "createdAt")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());;
    @Basic
    @Column(name = "isDeleted")
    private Timestamp isDeleted;
    @Basic
    @Column(name = "userId")
    private int userId;
    @Basic
    @Column(name = "postId")
    private int postId;
    @Basic
    @Column(name = "commentId")
    private Integer commentId;

}
