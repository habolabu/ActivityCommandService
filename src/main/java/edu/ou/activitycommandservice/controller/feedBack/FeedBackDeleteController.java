package edu.ou.activitycommandservice.controller.feedBack;

import edu.ou.activitycommandservice.common.constant.Endpoint;
import edu.ou.activitycommandservice.data.pojo.request.feedBack.FeedBackDeleteRequest;
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
@RequestMapping(Endpoint.FeedBack.BASE)
public class FeedBackDeleteController {
    private final IBaseService<IBaseRequest, IBaseResponse> feedBackDeleteService;

    /**
     * Delete exist feedback
     *
     * @param feedBackDeleteRequest slug of feedback
     * @return slug of exist feedback
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.DELETE_EXIST_FEEDBACK)
    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> deleteExistFeedBack(
            @Validated
            @RequestBody
            FeedBackDeleteRequest feedBackDeleteRequest
    ) {
        return new ResponseEntity<>(
                feedBackDeleteService.execute(feedBackDeleteRequest),
                HttpStatus.OK
        );
    }
}
