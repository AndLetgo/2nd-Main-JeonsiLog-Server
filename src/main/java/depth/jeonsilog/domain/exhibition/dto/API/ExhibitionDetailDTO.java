package depth.jeonsilog.domain.exhibition.dto.API;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

@ToString
@Getter
@NoArgsConstructor
public class ExhibitionDetailDTO {

    // Description : "response" >> "msgBody" >> seq, "perforInfo" >> seq, title, startDate, endDate, price, placeSeq

    @Nullable
    private ExhibitionDetailResponseDTO response;

    @Getter
    @NoArgsConstructor
    public static class ExhibitionDetailResponseDTO {

        @Nullable
        private ExhibitionDetailMsgBodyDTO msgBody;

        @Getter
        @NoArgsConstructor
        public static class ExhibitionDetailMsgBodyDTO {

            private Integer seq;

            @Nullable
            private PerformanceInfo perforInfo;

            @Getter
            @NoArgsConstructor
            public static class PerformanceInfo {

                private Integer seq;

                private String title;

                private String startDate;

                private String endDate;

                private String price;

                private Integer placeSeq;

                private String imgUrl;

            }

        }

    }
}
