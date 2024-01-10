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

        private Long replyId;

        private String contents;

        // 수정 없으므로 생성 시간
        private LocalDateTime createdDate;

        // 필요한 User의 요소가 여기 다 있음
        private UserResponseDto.SearchUsersRes user;
    }

    @Data
    @Builder
    public static class ReplyResListWithPaging {

        @Schema(type = "boolean", example = "true", description = "다음 페이지 존재 여부를 반환합니다.")
        private boolean hasNextPage;

        private List<ReplyResponseDto.ReplyRes> data;
    }

}
