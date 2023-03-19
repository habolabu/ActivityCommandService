package edu.ou.activitycommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(
        name = "CommentEmotion",
        schema = "ActivityCommandService"
)
@IdClass(CommentEmotionEntityPK.class)
public class CommentEmotionEntity implements Serializable {
    @Id
    @Column(name = "commentId")
    private int commentId;
    @Id
    @Column(name = "userId")
    private int userId;
    @Id
    @Column(name = "emotionId")
    private int emotionId;
}
