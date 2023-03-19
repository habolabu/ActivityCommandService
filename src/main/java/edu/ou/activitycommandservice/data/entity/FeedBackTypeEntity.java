package edu.ou.activitycommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(
        name = "FeedBackType",
        schema = "ActivityCommandService"
)
public class FeedBackTypeEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "slug")
    private String slug;
    @Basic
    @Column(name = "createdAt")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());;
    @Basic
    @Column(name = "isDeleted")
    private Timestamp isDeleted;

}
