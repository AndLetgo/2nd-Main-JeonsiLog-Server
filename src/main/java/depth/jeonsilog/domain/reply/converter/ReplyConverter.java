package depth.jeonsilog.domain.reply.converter;

import depth.jeonsilog.domain.reply.domain.Reply;
import depth.jeonsilog.domain.reply.dto.ReplyRequestDto;
import depth.jeonsilog.domain.reply.dto.ReplyResponseDto;
import depth.jeonsilog.domain.review.domain.Review;
import depth.jeonsilog.domain.review.dto.ReviewResponseDto;
import depth.jeonsilog.domain.user.domain.User;
import depth.jeonsilog.domain.user.dto.UserResponseDto;

import java.util.ArrayList;
import java.util.List;

public class ReplyConverter {

    // Replies -> ReplyResList
    public static List<ReplyResponseDto.ReplyRes> toReplyResList(List<Reply> replies) {

        List<ReplyResponseDto.ReplyRes> replyResList = new ArrayList<>();

        for (Reply reply : replies) {

            User user = reply.getUser();
            UserResponseDto.SearchUsersRes userRes = UserResponseDto.SearchUsersRes.builder()
                    .userId(user.getId())
                    .nickname(user.getNickname())
                    .profileImgUrl(user.getProfileImg())
                    .userLevel(user.getUserLevel())
                    .build();

            ReplyResponseDto.ReplyRes replyRes = ReplyResponseDto.ReplyRes.builder()
                    .replyId(reply.getId())
                    .contents(reply.getContents())
                    .createdDate(reply.getCreatedDate())
                    .user(userRes)
                    .build();

            replyResList.add(replyRes);
        }

        return replyResList;
    }

    public static ReplyResponseDto.ReplyResListWithPaging toReplyResListWithPaging(boolean hasNextPage, List<ReplyResponseDto.ReplyRes> replyResList) {
        return ReplyResponseDto.ReplyResListWithPaging.builder()
                .hasNextPage(hasNextPage)
                .data(replyResList)
                .build();
    }

    // Make Reply
    public static Reply toReply(Review review, User user, ReplyRequestDto.CreateReplyReq createReplyReq) {
        return Reply.builder()
                .user(user)
                .review(review)
                .contents(createReplyReq.getContents())
                .build();
    }

    public static ReplyResponseDto.ExistReplyRes toExistReplyRes(Long replyId, Boolean isExist) {
        return ReplyResponseDto.ExistReplyRes.builder()
                .replyId(replyId)
                .isExist(isExist)
                .build();
    }
}
