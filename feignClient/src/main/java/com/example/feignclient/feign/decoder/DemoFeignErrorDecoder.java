package com.example.feignclient.feign.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class DemoFeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.resolve(response.status());

        if (HttpStatus.NOT_FOUND.equals(status)) {
            log.error("NOT_FOUND");
            // TODO: 이 부분을 CustomException으로 변경해서 ControllerAdvice를 통해서 처리해야한다.
            throw new RuntimeException(String.format("[DemoFeignErrorDecoder] Http Status: %s", status));
        }
        return errorDecoder.decode(methodKey, response);
    }
}
