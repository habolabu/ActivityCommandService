package edu.ou.activitycommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(
        name = "Emotion",
        schema = "ActivityCommandService"
)
public class EmotionEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "icon")
    private String icon;
    @Basic
    @Column(name = "isDeleted")
    private Timestamp isDeleted;
}
