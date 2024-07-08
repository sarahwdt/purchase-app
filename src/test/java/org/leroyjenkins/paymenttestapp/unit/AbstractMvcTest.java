package org.leroyjenkins.paymenttestapp.unit;

import org.junit.jupiter.api.extension.ExtendWith;
import org.leroyjenkins.paymenttestapp.exception.BusinessLogicException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
public abstract class AbstractMvcTest {
    @Autowired
    protected MockMvc mockMvc;

    protected void assertErrorResponse(RequestBuilder requestBuilder, BusinessLogicException exception)
            throws Exception {
        ResultMatcher titleMatcher = jsonPath("$.title").value(exception.getTitle());
        ResultMatcher detailMatcher = jsonPath("$.detail").value(exception.getDetail());
        List<ResultMatcher> bodyMatchers = new ArrayList<>(List.of(titleMatcher, detailMatcher));
        if (exception.getProperties() != null) {
            for (Map.Entry<String, Object> entry : exception.getProperties().entrySet()) {
                bodyMatchers.add(jsonPath("$." + entry.getKey()).value(entry.getValue()));
            }
        }
        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest())
                .andExpectAll(bodyMatchers.toArray(ResultMatcher[]::new));
    }
}
