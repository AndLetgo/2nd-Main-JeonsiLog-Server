package depth.jeonsilog.domain.interest.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.exhibition.domain.QExhibition;
import depth.jeonsilog.domain.interest.domain.Interest;
import depth.jeonsilog.domain.interest.domain.QInterest;
import depth.jeonsilog.domain.rating.domain.QRating;
import depth.jeonsilog.domain.review.domain.QReview;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class InterestQuerydslRepositoryImpl implements InterestQuerydslRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Interest> findInterestsByUserAndExhibitionWithoutRatingAndReview() {
        QInterest interest = QInterest.interest;
        QRating rating = QRating.rating;
        QReview review = QReview.review;
        QExhibition exhibition = QExhibition.exhibition;

        // 서브쿼리를 사용하여 해당 전시회와 사용자에 대한 평점과 리뷰의 존재 여부를 확인
        BooleanExpression noRatings = JPAExpressions
                .selectFrom(rating)
                .where(rating.user.eq(interest.user),
                        rating.exhibition.eq(interest.exhibition))
                .notExists();

        BooleanExpression noReviews = JPAExpressions
                .selectFrom(review)
                .where(review.user.eq(interest.user),
                        review.exhibition.eq(interest.exhibition))
                .notExists();

        return queryFactory
                .selectFrom(interest)
                .join(interest.exhibition, exhibition)
                .where(exhibition.operatingKeyword.eq(OperatingKeyword.ON_DISPLAY),
                        noRatings,
                        noReviews)
                .fetch();
    }
}
