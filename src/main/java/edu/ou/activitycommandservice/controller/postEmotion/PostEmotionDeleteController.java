package edu.ou.activitycommandservice.controller.postEmotion;

import edu.ou.activitycommandservice.common.constant.Endpoint;
import edu.ou.activitycommandservice.data.pojo.request.postEmotion.PostEmotionDeleteRequest;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.PostEmotion.BASE)
public class PostEmotionDeleteController {
    private final IBaseService<IBaseRequest, IBaseResponse> postEmotionDeleteService;

    /**
     * Delete exist post emotion
     *
     * @param postEmotionDeleteRequest id of post emotion
     * @return id of exist post emotion
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.REMOVE_POST_REACTION)
    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> deleteExistPostEmotion(
            @Validated
            @RequestBody
            PostEmotionDeleteRequest postEmotionDeleteRequest
    ) {
        return new ResponseEntity<>(
                postEmotionDeleteService.execute(postEmotionDeleteRequest),
                HttpStatus.OK
        );
    }
}
