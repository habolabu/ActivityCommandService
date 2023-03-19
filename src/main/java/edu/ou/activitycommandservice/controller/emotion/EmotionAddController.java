package edu.ou.activitycommandservice.controller.emotion;

import edu.ou.activitycommandservice.common.constant.Endpoint;
import edu.ou.activitycommandservice.data.pojo.request.emotion.EmotionAddRequest;
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
@RequestMapping(Endpoint.Emotion.BASE)
public class EmotionAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> emotionAddService;

    /**
     * Add new emotion
     *
     * @param emotionAddRequest new emotion information
     * @return id of new emotion
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.ADD_NEW_EMOTION)
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewEmotion(
            @Validated
            @RequestBody
            EmotionAddRequest emotionAddRequest
    ) {
        return new ResponseEntity<>(
                emotionAddService.execute(emotionAddRequest),
                HttpStatus.OK
        );
    }
}
