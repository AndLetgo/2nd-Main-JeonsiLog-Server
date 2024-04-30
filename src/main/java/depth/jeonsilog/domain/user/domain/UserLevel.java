package depth.jeonsilog.domain.user.domain;

import org.springframework.expression.spel.ast.OpNE;

public enum UserLevel {

    NON, // 레벨 없음 : 0개
    DONE, // 첫 감상평 작성 이후 (여전히 레벨 없음 단계) : 1 ~ 2개
    BEGINNER, // 초보 : 3 ~ 9개
    INTERMEDIATE, // 중수 : 10 ~ 19개
    ADVANCED, // 고수 : 20 ~ 29개
    MASTER // 마스터 : 30개 ~
}
