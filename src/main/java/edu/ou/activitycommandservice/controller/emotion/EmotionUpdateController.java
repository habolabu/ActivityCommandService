package edu.ou.activitycommandservice.controller.emotion;

import edu.ou.activitycommandservice.common.constant.Endpoint;
import edu.ou.activitycommandservice.data.pojo.request.emotion.EmotionUpdateRequest;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.Emotion.BASE)
public class EmotionUpdateController {
    private final IBaseService<IBaseRequest, IBaseResponse> feedBackUpdateService;

    /**
     * Update exist emotion
     *
     * @param emotionUpdateRequest new emotion information
     * @return id of exist emotion
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.MODIFY_EXIST_EMOTION)
    @PutMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> updateExistEmotion(
            @Validated
            @RequestBody
            EmotionUpdateRequest emotionUpdateRequest
    ) {
        return new ResponseEntity<>(
                feedBackUpdateService.execute(emotionUpdateRequest),
                HttpStatus.OK
        );
    }
}
