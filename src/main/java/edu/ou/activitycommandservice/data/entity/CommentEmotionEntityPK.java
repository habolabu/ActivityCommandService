package edu.ou.activitycommandservice.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class CommentEmotionEntityPK implements Serializable {
    @Column(name = "commentId")
    @Id
    private int commentId;
    @Column(name = "userId")
    @Id
    private int userId;
    @Column(name = "emotionId")
    @Id
    private int emotionId;
}
