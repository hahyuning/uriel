package com.uriel.travel.Controller;

import com.uriel.travel.domain.dto.order.OrderRequestDto;
import com.uriel.travel.domain.dto.toss.WebHookInfo;
import com.uriel.travel.service.OrderService;
import com.uriel.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class WidgetController {

    private final OrderService orderService;
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 예약금 결제
    @RequestMapping(value = "/confirm")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody OrderRequestDto.Create requestDto) throws Exception {

        // 예약 가능 여부 체크 (상품 예약 상태, 인원수 체크)
        orderService.checkReservedUserCount(requestDto.getTotalCount(), requestDto.getProductId());

        JSONParser parser = new JSONParser();
        JSONObject obj = new JSONObject();
        obj.put("orderId", requestDto.getOrderId());
        obj.put("amount", requestDto.getAmount());
        obj.put("paymentKey", requestDto.getPaymentKey());

        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
        String widgetSecretKey = "test_sk_Z61JOxRQVEoWEyYPlRnR8W0X9bAq";
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        // 결제 성공 및 실패 비즈니스 로직을 구현하세요.
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();

        // 결제 승인이 이루어지거나, 가상계좌인 경우 Order 객체 생성
        String method = (String) jsonObject.get("method");
        String status = (String) jsonObject.get("status");

        if (method.equals("가상계좌") || status.equals("DONE")) {
            String imomOrderId = orderService.createOrder(jsonObject, requestDto, "woori@imom.kr");
            updateMarketingAgreement("woori@imom.kr", requestDto.isMarketing());
        }
        return ResponseEntity.status(code).body(jsonObject);
    }

    // 잔금 완납
    @RequestMapping(value = "/confirm/full")
    public ResponseEntity<JSONObject> confirmFullPayment(@RequestBody OrderRequestDto.AdditionalPayment requestDto) throws Exception {

        JSONParser parser = new JSONParser();
        JSONObject obj = new JSONObject();
        obj.put("orderId", requestDto.getOrderId());
        obj.put("amount", requestDto.getAmount());
        obj.put("paymentKey", requestDto.getPaymentKey());

        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
        String widgetSecretKey = "test_sk_Z61JOxRQVEoWEyYPlRnR8W0X9bAq";
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        // 결제 성공 및 실패 비즈니스 로직을 구현하세요.
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader); // payment 객체?????

        responseStream.close();

        String method = (String) jsonObject.get("method");
        String status = (String) jsonObject.get("status");
        if (method.equals("가상계좌") || status.equals("DONE")) {
            orderService.additionalPayment(jsonObject, requestDto);
            updateMarketingAgreement("woori@imom.kr", requestDto.isMarketing());
        }

        return ResponseEntity.status(code).body(jsonObject);
    }

    private void updateMarketingAgreement(String email, boolean agreement) {
        userService.updateMarketingAgreement(email, agreement);
    }

    // 가상계좌 웹훅 로직
    @PostMapping("/virtual-account/hook")
    public void virtualAccountWebHook(@RequestBody WebHookInfo.VirtualAccount requestDto) throws Exception {
        logger.info("가상계좌 웹훅 들어옴");
        logger.info(requestDto.getStatus() + "");

        // 결제 정보 받아오기
        JSONParser parser = new JSONParser();

        String widgetSecretKey = "test_sk_Z61JOxRQVEoWEyYPlRnR8W0X9bAq";
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        URL url = new URL("https://api.tosspayments.com/v1/payments/orders/" + requestDto.getOrderId());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);

        orderService.updateOrderStateByVAWebHook(requestDto, jsonObject);
    }

    // 가상계좌 웹훅 로직
    @PostMapping("/others/hook")
    public void virtualAccountWebHook(@RequestBody WebHookInfo.OtherPayments requestDto) throws Exception {
        logger.info("일반결제 웹훅 들어옴");
        logger.info(requestDto.getData().getStatus() + "");

        orderService.updateOrderStateByOPWebHook(requestDto);
    }
}
