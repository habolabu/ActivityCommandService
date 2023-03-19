package edu.ou.activitycommandservice.controller.feedBack;

import edu.ou.activitycommandservice.common.constant.Endpoint;
import edu.ou.activitycommandservice.data.pojo.request.feedBack.FeedBackUpdateRequest;
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
@RequestMapping(Endpoint.FeedBack.BASE)
public class FeedBackUpdateController {
    private final IBaseService<IBaseRequest, IBaseResponse> feedBackUpdateService;

    /**
     * Update exist feedback
     *
     * @param feedBackUpdateRequest new feedback information
     * @return slug of exist feedback
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.MODIFY_EXIST_FEEDBACK)
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> updateExistFeedBack(
            @Validated
            @RequestBody
            FeedBackUpdateRequest feedBackUpdateRequest
    ) {
        return new ResponseEntity<>(
                feedBackUpdateService.execute(feedBackUpdateRequest),
                HttpStatus.OK
        );
    }
}
