package edu.ou.activitycommandservice.data.pojo.request.comment;


import lombok.Data;

@Data
public class CommentOwnerCheckRequest {
    private int userId;
    private int commentId;
}
