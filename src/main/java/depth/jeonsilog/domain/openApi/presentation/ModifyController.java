package depth.jeonsilog.domain.openApi.presentation;

import depth.jeonsilog.domain.openApi.application.ModifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/modification")
public class ModifyController {

    private final ModifyService modifyService;

    @PatchMapping("/exhibition-name")
    public ResponseEntity<?> modifyExhibitionName() {
        return modifyService.modifyExhibitionName();
    }
}
