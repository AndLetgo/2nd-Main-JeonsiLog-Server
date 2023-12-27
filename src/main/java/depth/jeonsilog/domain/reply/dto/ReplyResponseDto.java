package depth.jeonsilog.domain.reply.dto;

import depth.jeonsilog.domain.user.dto.UserResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

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

}
