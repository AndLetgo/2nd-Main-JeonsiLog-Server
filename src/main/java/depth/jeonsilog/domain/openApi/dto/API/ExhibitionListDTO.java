package depth.jeonsilog.domain.openApi.dto.API;

import lombok.*;
import org.springframework.lang.Nullable;

import java.util.ArrayList;

@ToString
@Getter
@NoArgsConstructor
public class ExhibitionListDTO {

    /** 참고
     * https://velog.io/@zooneon/Java-ObjectMapper%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-JSON-%ED%8C%8C%EC%8B%B1%ED%95%98%EA%B8%B0
     */

    @Nullable
    private ExhibitionListResponseDTO response;

    @Getter
    @NoArgsConstructor
    public static class ExhibitionListResponseDTO {

        // Description : "response" >> "msgBody" >> totalCount, cPage, rows, "perforList" >> seq

        @Nullable
        private ExhibitionListMsgBodyDTO msgBody;

        @Getter
        @NoArgsConstructor
        public static class ExhibitionListMsgBodyDTO {

            private Integer totalCount;

            private Integer cPage;

            private Integer rows;

            @Nullable
            private ArrayList<PerformElement> perforList = new ArrayList<>();

            @Getter
            @NoArgsConstructor
            public static class PerformElement {

                private Integer seq;

            }

        }

    }
}
