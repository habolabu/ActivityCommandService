package edu.ou.activitycommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(
        name = "Post",
        schema = "ActivityCommandService"
)
public class PostEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "slug")
    private String slug;
    @Basic
    @Column(name = "content")
    private String content;
    @Basic
    @Column(name = "totalComment")
    private int totalComment = 0;
    @Basic
    @Column(name = "totalEmotion")
    private int totalEmotion = 0;
    @Basic
    @Column(name = "createdAt")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());;
    @Basic
    @Column(name = "isEdited")
    private boolean isEdited = false;
    @Basic
    @Column(name = "isDeleted")
    private Timestamp isDeleted;
}
