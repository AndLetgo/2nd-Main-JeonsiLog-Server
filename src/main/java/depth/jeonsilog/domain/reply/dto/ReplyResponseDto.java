package depth.jeonsilog.domain.reply.dto;

import depth.jeonsilog.domain.review.dto.ReviewResponseDto;
import depth.jeonsilog.domain.user.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyResponseDto {

    @Data
    @Builder
    public static class ReplyRes {

        @Schema(type = "long", example = "1", description = "댓글 ID를 출력합니다.")
        private Long replyId;

        @Schema(type = "string", example = "앵무는 앵무의 말을 하고", description = "댓글 내용을 출력합니다.")
        private String contents;

        // 수정 없으므로 생성 시간
        @Schema(type = "LocalDateTime", example = "2023-12-22 23:51:45.848882", description = "알림이 생성된 DateTime을 출력합니다.")
        private LocalDateTime createdDate;

        // 필요한 User의 요소가 여기 다 있음
        @Schema(type = "SearchUsersRes", example = "user 정보", description = "필요한 user의 정보가 들어있습니다.")
        private UserResponseDto.SearchUsersRes user;
    }

    @Data
    @Builder
    public static class ReplyResListWithPaging {

        @Schema(type = "boolean", example = "true", description = "다음 페이지 존재 여부를 반환합니다.")
        private boolean hasNextPage;

        private List<ReplyResponseDto.ReplyRes> data;
    }

    @Data
    @Builder
    public static class ExistReplyRes {

        @Schema(type = "long", example = "1", description = "댓글 ID를 출력합니다.")
        private Long replyId;

        @Schema(type = "boolean", example = "true", description = "댓글이 존재하는지 출력합니다. 삭제되었으면 false.")
        private Boolean isExist;
    }

}
