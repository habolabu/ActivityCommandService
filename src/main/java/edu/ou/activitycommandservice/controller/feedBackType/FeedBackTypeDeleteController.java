package edu.ou.activitycommandservice.controller.feedBackType;

import edu.ou.activitycommandservice.common.constant.Endpoint;
import edu.ou.activitycommandservice.data.pojo.request.feedBackType.FeedBackTypeDeleteRequest;
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
@RequestMapping(Endpoint.FeedBackType.BASE)
public class FeedBackTypeDeleteController {
    private final IBaseService<IBaseRequest, IBaseResponse> feedBackTypeDeleteService;

    /**
     * Delete exist feedback type
     *
     * @param feedBackTypeDeleteRequest slug of feedback type
     * @return slug of exist feedback type
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.DELETE_EXIST_FEEDBACK_TYPE)
    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> deleteExistFeedBackType(
            @Validated
            @RequestBody
            FeedBackTypeDeleteRequest feedBackTypeDeleteRequest
    ) {
        return new ResponseEntity<>(
                feedBackTypeDeleteService.execute(feedBackTypeDeleteRequest),
                HttpStatus.OK
        );
    }
}
