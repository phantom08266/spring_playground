package com.example.feignclient.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor(staticName = "ofDefault")
public class DemoFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        //Get 요청
        if (HttpMethod.GET.name().equals(template.method())) {
            log.info("GET 요청 [DemoFeignInterceptor] 인터셉터 : queries : " + template.queries());
            return;
        }

        // Post 요청
        String encodedResponseBody = StringUtils.toEncodedString(template.body(), StandardCharsets.UTF_8);
        log.info("encodedResponseBody : " + encodedResponseBody);

        // String 을 object로 변환할 수도 있음.
        String convertRequestBody = encodedResponseBody;
        template.body(convertRequestBody);
    }
}
