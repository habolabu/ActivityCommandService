package edu.ou.activitycommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(
        name = "FeedBack",
        schema = "ActivityCommandService"
)
public class FeedBackEntity implements Serializable {
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
    @Column(name = "createdAt")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());;

    @Basic
    @Column(name = "isDeleted")
    private Timestamp isDeleted;
    @Basic
    @Column(name = "content")
    private String content;
    @Basic
    @Column(name = "userId")
    private int userId;
    @Basic
    @Column(name = "feedBackTypeId")
    private int feedBackTypeId;
}
