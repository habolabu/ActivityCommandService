package edu.ou.activitycommandservice.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class PostEmotionEntityPK implements Serializable {
    @Column(name = "postId")
    @Id
    private int postId;
    @Column(name = "userId")
    @Id
    private int userId;
    @Column(name = "emotionId")
    @Id
    private int emotionId;
}
