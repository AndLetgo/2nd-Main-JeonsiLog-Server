package depth.jeonsilog.domain.openApi.presentation;

import depth.jeonsilog.domain.openApi.application.SaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/saves")
public class SaveController {

    private final SaveService saveService;

    // Description : 저장 임시 로직
    @PostMapping("/save")
    public ResponseEntity<?> saveExhibitionAndPlace() {
        return saveService.saveExhibitionAndPlace();

    }
}
