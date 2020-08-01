package com.swapping.productservice.configuration.interceptor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestHeaderAttributesHandlerInterceptorTest {

    private RequestHeaderAttributesHandlerInterceptor requestHeaderAttributesHandlerInterceptor;

    @Before
    public void init() {
        requestHeaderAttributesHandlerInterceptor = new RequestHeaderAttributesHandlerInterceptor("applicationName");
        MDC.clear();
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void it_should_put_correlation_id_from_request_correlation_value() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("x-correlationId", "123456789");
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        requestHeaderAttributesHandlerInterceptor.preHandle(request, response, new Object());

        //then
        assertThat(MDC.get("x-correlationId")).isEqualTo("123456789");
    }

    @Test
    public void it_should_put_agent_name_from_request_agent_name_value() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("x-agent-name", "agentName");
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        requestHeaderAttributesHandlerInterceptor.preHandle(request, response, new Object());

        //then
        assertThat(MDC.get("x-agent-name")).isEqualTo("agentName");
    }

    @Test
    public void it_should_put_default_applicationName_as_agent_name_when_request_agent_name_header_is_null() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        requestHeaderAttributesHandlerInterceptor.preHandle(request, response, new Object());

        //then
        assertThat(MDC.get("x-agent-name")).isEqualTo("applicationName");
    }

    @Test
    public void it_should_put_correlation_id_when_request_correlation_value_is_null() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        requestHeaderAttributesHandlerInterceptor.preHandle(request, response, new Object());

        //then
        assertThat(MDC.get("x-correlationId")).isNotEmpty();
    }

    @Test
    public void it_should_remove_correlation_id_when_request_after_completion() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        requestHeaderAttributesHandlerInterceptor.afterCompletion(request, response, new Object(), new Exception());

        //then
        assertThat(MDC.get("x-correlationId")).isNull();
        assertThat(MDC.get("x-agent-name")).isNull();
        assertThat(MDC.get("x-agent-user")).isNull();
    }

    @Test
    public void it_should_put_agent_user_from_request_agent_user_value() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("x-agent-user", "agentName");
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        requestHeaderAttributesHandlerInterceptor.preHandle(request, response, new Object());

        //then
        assertThat(MDC.get("x-agent-user")).isEqualTo("agentName");
    }

    @Test
    public void it_should_put_default_system_user_as_agent_user_when_request_agent_user_header_is_null() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        requestHeaderAttributesHandlerInterceptor.preHandle(request, response, new Object());

        //then
        assertThat(MDC.get("x-agent-user")).isEqualTo("-1");
    }
}