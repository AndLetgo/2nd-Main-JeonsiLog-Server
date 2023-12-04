package depth.jeonsilog.domain.s3.presentation;

import depth.jeonsilog.domain.s3.application.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@Tag(name = "S3 API", description = "AWS S3 API입니다.")
@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    /**
     * TODO : s3 controller, service 는 image의 repository, entity와 연결될 필요가 있다
     * TODO : Image 테이블과 연결
     */

    private final S3Service s3Service;

    // client -> s3 upload (s3에 저장)
    @Operation(summary = "Image File S3에 저장", description = "사용자의 Image를 S3에 저장하는 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = {@Content(mediaType = "application/json",schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))})
    })
    @PostMapping
    public String saveFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return s3Service.saveFile(multipartFile);
    }

    // s3 -> client : return url (s3에서 가져옴)
    @Operation(summary = "S3에서 Image File의 URL을 가져옵니다.", description = "사용자가 입력한 이름의 Image를 S3에서 가져오는 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json",schema = @Schema(implementation = URL.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))})
    })
    @GetMapping("/find/{filename}")
    public ResponseEntity<?> downloadImage(@Parameter(description = "FileName을 입력하세요", required = true) @PathVariable(value = "filename") String filename) {
        return s3Service.downloadImage(filename);
    }

    // s3 image delete (s3에서 삭제)
    @Operation(summary = "S3의 Image File을 삭제합니다.", description = "사용자가 입력한 이름의 Image를 S3에서 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json",schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))})
    })
    @DeleteMapping
    public void deleteImage(@RequestParam("file") String originalFilename) {
        s3Service.deleteImage(originalFilename);
    }
}