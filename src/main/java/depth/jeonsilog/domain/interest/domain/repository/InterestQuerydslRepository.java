package depth.jeonsilog.domain.interest.domain.repository;

import depth.jeonsilog.domain.interest.domain.Interest;

import java.util.List;

public interface InterestQuerydslRepository {

    List<Interest> findInterestsByUserAndExhibitionWithoutRatingAndReview();
}
