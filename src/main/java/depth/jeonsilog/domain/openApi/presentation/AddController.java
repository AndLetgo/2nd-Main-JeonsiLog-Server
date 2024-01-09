package depth.jeonsilog.domain.openApi.presentation;

import depth.jeonsilog.domain.openApi.application.AddService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/adds")
public class AddController {

    private final AddService addService;

    @PostMapping
    public ResponseEntity<?> addExhibitionAndPlace() {
        return addService.addExhibitionAndPlace();
    }
}
