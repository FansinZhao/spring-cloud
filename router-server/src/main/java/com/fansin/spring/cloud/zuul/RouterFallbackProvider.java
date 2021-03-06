package com.fansin.spring.cloud.zuul;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhaofeng on 17-6-12.
 */
@Component
public class RouterFallbackProvider implements FallbackProvider {

    /**
     * The route this fallback will be used for.
     *
     * @return The route the fallback will be used for.
     */
    @Override
    public String getRoute() {
        //all
//        return "*";
        return "fallback";
    }

    /**
     * Provides a fallback response.
     *
     * @return The fallback response.
     */
    @Override
    public ClientHttpResponse fallbackResponse() {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
                return "OK";
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("RouterFallbackProvider 找不到serviceId,降级处理".getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }

    /**
     * Provides a fallback response based on the cause of the failed execution.
     *
     * @param cause cause of the main method failure
     * @return the fallback response
     */
    @Override
    public ClientHttpResponse fallbackResponse(Throwable cause) {
        cause.printStackTrace();
        //根据cause返回不同的response
        return null;
    }
}
