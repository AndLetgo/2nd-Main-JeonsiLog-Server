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

    // TODO: AccessToken 발급 받기. -> Header에 포함하여 푸시 알림 요청
    private static String getAccessToken() throws IOException {
        ClassPathResource resource = new ClassPathResource("services-account.json");
        GoogleCredential googleCredential = GoogleCredential
//                .fromStream(new FileInputStream(resource.getFile()))
                .fromStream(resource.getInputStream())
                .createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshToken();
        log.info("액세스 토큰 발급: " + googleCredential.getAccessToken());
        return googleCredential.getAccessToken();
    }

    // TODO: 활동 알림 만들기
    public void makeActiveAlarm(String fcmToken, String title) {

        JSONObject jsonValue = new JSONObject();
        jsonValue.put("title", title);

        JSONObject jsonData = new JSONObject();
        jsonData.put("token", fcmToken);
        jsonData.put("data", jsonValue);

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("message", jsonData);

        pushAlarm(jsonMessage);
    }

    // TODO: 전시 알림 만들기
    public void makeExhibitionAlarm(String fcmToken, String title, String body) {

        JSONObject jsonValue = new JSONObject();
        jsonValue.put("title", title);
        jsonValue.put("body", body);

        JSONObject jsonData = new JSONObject();
        jsonData.put("token", fcmToken);
        jsonData.put("data", jsonValue);

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("message", jsonData);

        pushAlarm(jsonMessage);
    }

    // TODO: 만들어진 알림을 받아서 푸시한다.
    private void pushAlarm(JSONObject jsonMessage) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + getAccessToken())
                    .addHeader("Content-Type", "application/json; UTF-8")
                    .url(API_URL)
                    .post(RequestBody.create(jsonMessage.toString(), MediaType.parse("application/json")))
                    .build();
            Response response = okHttpClient.newCall(request).execute();

            log.info("### response str : " + response);
            log.info("### response result : " + response.isSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
