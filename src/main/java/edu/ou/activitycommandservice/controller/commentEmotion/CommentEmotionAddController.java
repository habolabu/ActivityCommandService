package edu.ou.activitycommandservice.controller.commentEmotion;

import edu.ou.activitycommandservice.common.constant.Endpoint;
import edu.ou.activitycommandservice.data.pojo.request.commentEmotion.CommentEmotionAddRequest;
import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.CommentEmotion.BASE)
public class CommentEmotionAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> commentEmotionAddService;

    /**
     * Add new comment emotion
     *
     * @param commentEmotionAddRequest new comment emotion information
     * @return id of new comment emotion
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.REACT_EXIST_COMMENT)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewCommentEmotion(
            @Validated
            @RequestBody
            CommentEmotionAddRequest commentEmotionAddRequest
    ) {
        return new ResponseEntity<>(
                commentEmotionAddService.execute(commentEmotionAddRequest),
                HttpStatus.OK
        );
    }
}
