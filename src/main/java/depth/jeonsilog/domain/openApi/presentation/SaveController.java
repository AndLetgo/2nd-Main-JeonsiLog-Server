package depth.jeonsilog.domain.openApi.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import depth.jeonsilog.domain.exhibition.application.ExhibitionService;
import depth.jeonsilog.domain.exhibition.domain.Exhibition;
import depth.jeonsilog.domain.exhibition.domain.OperatingKeyword;
import depth.jeonsilog.domain.exhibition.domain.PriceKeyword;
import depth.jeonsilog.domain.exhibition.domain.repository.ExhibitionRepository;
import depth.jeonsilog.domain.exhibition.dto.API.ExhibitionDetailDTO;
import depth.jeonsilog.domain.exhibition.dto.API.ExhibitionListDTO;
import depth.jeonsilog.domain.exhibition.dto.API.PlaceDetailDTO;
import depth.jeonsilog.domain.openApi.application.SaveService;
import depth.jeonsilog.domain.place.domain.Place;
import depth.jeonsilog.domain.place.domain.repository.PlaceRepository;
import depth.jeonsilog.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
