package depth.jeonsilog.domain.exhibition.dto.API;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@ToString
@Getter
@NoArgsConstructor
public class PlaceDetailDTO {

    // Description : "response" >> "msgBody" >> seq, "perforInfo" >> seq, culName, culAddr, culHomeUrl

    @Nullable
    private PlaceDetailResponseDTO response;

    @Getter
    @NoArgsConstructor
    public static class PlaceDetailResponseDTO {

        @Nullable
        private PlaceDetailMsgBodyDTO msgBody;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class PlaceDetailMsgBodyDTO {

            private Integer seq;

            @Nullable
            private PlaceInfo placeInfo;

            @Getter
            @NoArgsConstructor
            public static class PlaceInfo {

                private Integer seq;

                // place - name
                private String culName;

                // place - address
                private String culAddr;

                // place - tel
                private String culTel;

                // place - homePage
                private String culHomeUrl;

            }

        }

    }

}
