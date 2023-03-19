package edu.ou.activitycommandservice.controller.postEmotion;

import edu.ou.activitycommandservice.common.constant.Endpoint;
import edu.ou.activitycommandservice.data.pojo.request.postEmotion.PostEmotionAddRequest;
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
@RequestMapping(Endpoint.PostEmotion.BASE)
public class PostEmotionAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> postEmotionAddService;

    /**
     * Add new post emotion
     *
     * @param postEmotionAddRequest new post emotion information
     * @return id of new post emotion
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.REACT_EXIST_POST)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewPostEmotion(
            @Validated
            @RequestBody
            PostEmotionAddRequest postEmotionAddRequest
    ) {
        return new ResponseEntity<>(
                postEmotionAddService.execute(postEmotionAddRequest),
                HttpStatus.OK
        );
    }
}
