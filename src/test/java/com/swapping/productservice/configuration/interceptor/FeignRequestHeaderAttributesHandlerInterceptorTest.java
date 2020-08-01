package com.swapping.productservice.configuration.interceptor;

import feign.RequestTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.MDC;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FeignRequestHeaderAttributesHandlerInterceptorTest {

    @InjectMocks
    private FeignRequestHeaderAttributesHandlerInterceptor feignRequestHeaderAttributesHandlerInterceptor;

    @Before
    public void init() {
        MDC.clear();
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void it_should_put_correlation_id_into_request_from_MDC_if_available() {
        //given
        MDC.put("x-correlationId", "correlation-id-from-mdc");
        RequestTemplate requestTemplate = new RequestTemplate();
        requestTemplate.method("GET");

        //when
        feignRequestHeaderAttributesHandlerInterceptor.apply(requestTemplate);

        //then
        assertThat(requestTemplate.headers().get("x-correlationId")).containsOnlyOnce("correlation-id-from-mdc");
    }

    @Test
    public void it_should_not_put_correlation_id_into_request_from_MDC_if_not_available() {
        //given
        RequestTemplate requestTemplate = new RequestTemplate();
        requestTemplate.method("GET");

        //when
        feignRequestHeaderAttributesHandlerInterceptor.apply(requestTemplate);

        //then
        assertThat(requestTemplate.headers().get("x-correlationId")).isNull();
    }

    @Test
    public void it_should_not_put_agentName_into_request_from_MDC_if_not_available() {
        //given
        RequestTemplate requestTemplate = new RequestTemplate();
        requestTemplate.method("GET");

        //when
        feignRequestHeaderAttributesHandlerInterceptor.apply(requestTemplate);

        //then
        assertThat(requestTemplate.headers().get("x-agentname")).isNull();
    }

    @Test
    public void it_should_not_put_executorUser_into_request_from_MDC_if_not_available() {
        //given
        RequestTemplate requestTemplate = new RequestTemplate();
        requestTemplate.method("GET");

        //when
        feignRequestHeaderAttributesHandlerInterceptor.apply(requestTemplate);

        //then
        assertThat(requestTemplate.headers().get("x-executor-user")).isNull();
    }
}