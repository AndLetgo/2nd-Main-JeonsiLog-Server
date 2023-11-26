package depth.jeonsilog.domain.s3.presentation;

import depth.jeonsilog.domain.s3.application.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    // client -> s3 upload (s3에 저장)
    @PostMapping
    public String saveFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return s3Service.saveFile(multipartFile);
    }

    // s3 -> client : return url (s3에서 가져옴)
    @GetMapping("/find/{filename}")
    public ResponseEntity<URL> downloadImage(@PathVariable String filename) {
        return s3Service.downloadImage(filename);
    }

    // s3 image delete (s3에서 삭제)
    @DeleteMapping
    public void deleteImage(@RequestParam("file") String originalFilename) {
        s3Service.deleteImage(originalFilename);
    }
}