package edu.ou.activitycommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(
        name = "PostEmotion",
        schema = "ActivityCommandService"
)
@IdClass(PostEmotionEntityPK.class)
public class PostEmotionEntity implements Serializable {
    @Id
    @Column(name = "postId")
    private int postId;
    @Id
    @Column(name = "userId")
    private int userId;
    @Id
    @Column(name = "emotionId")
    private int emotionId;
}
