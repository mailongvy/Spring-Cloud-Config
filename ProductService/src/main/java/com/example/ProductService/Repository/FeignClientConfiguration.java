package com.example.ProductService.Repository;

import io.micrometer.tracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.RequestInterceptor;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public RequestInterceptor tracingInterceptor(Tracer tracer) {
        return requestTemplate -> {
            var span = tracer.currentSpan();
            if (span != null) {
                // B3 propagation headers - đảm bảo trace context được truyền
                requestTemplate.header("X-B3-TraceId", span.context().traceId());
                requestTemplate.header("X-B3-SpanId", span.context().spanId());
                requestTemplate.header("X-B3-Sampled", "1");

                // Parent span ID if available
                if (span.context().parentId() != null) {
                    requestTemplate.header("X-B3-ParentSpanId", span.context().parentId());
                }
            }
        };
    }
}
