package depth.jeonsilog.domain.fcm.application;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {

    // API_URL은 메세지 전송을 위해 요청하는 주소이다. {프로젝트 ID}넣기
    private static final String API_URL = "https://fcm.googleapis.com/v1/projects/jeonsilog-fd54e/messages:send";
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

    //AccessToken 발급 받기. -> Header에 포함하여 푸시 알림 요청
    private static String getAccessToken() throws IOException {
        ClassPathResource resource = new ClassPathResource("services-account.json");
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(new FileInputStream(resource.getFile()))
                .createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshToken();
        log.info("액세스 토큰 발급: " + googleCredential.getAccessToken());
        return googleCredential.getAccessToken();
    }

    public void send(String fcmToken, String title, String body) {

        // 1. create message body
        JSONObject jsonValue = new JSONObject();
        jsonValue.put("title", title);
        jsonValue.put("body", body);

        JSONObject jsonData = new JSONObject();
        jsonData.put("token", fcmToken);
        jsonData.put("data", jsonValue);

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("message", jsonData);

        // 2. create token & send push
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + getAccessToken())
                    .addHeader("Content-Type", "application/json; UTF-8")
                    .url(API_URL)
                    .post(RequestBody.create(jsonMessage.toString(), MediaType.parse("application/json")))
                    .build();
            Response response = okHttpClient.newCall(request).execute();

            log.info("### response str : " + response.toString());
            log.info("### response result : " + response.isSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
