package depth.jeonsilog.domain.reply.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class ReplyRequestDto {
    
    @Data
    public static class CreateReplyReq {

        @Schema(type = "long", example = "1", description = "감상평 id입니다.")
        @NotNull(message = "감상평 id를 입력해야 합니다.")
        private Long reviewId;

        @Schema(type = "string", example = "너의 감상평에 대해 난 그렇게 생각 안하는데?", description = "감상평에 대한 댓글입니다.")
        @Size(max = 1000, message = "댓글은 공백 포함 1000자까지 입력 가능합니다.")
        private String contents;
        
    }
}
