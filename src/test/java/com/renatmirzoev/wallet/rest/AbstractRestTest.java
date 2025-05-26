package com.renatmirzoev.wallet.rest;

import com.renatmirzoev.wallet.AbstractIntegrationTest;
import com.renatmirzoev.wallet.rest.model.ErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public abstract class AbstractRestTest extends AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    protected <T> Request<T> performRequest(String endpoint, Class<T> clazz) {
        return new Request<>(endpoint, clazz, mockMvc);
    }

    protected static class Endpoints {

        protected interface Endpoint {
            String getPath();
        }

        @Getter
        @RequiredArgsConstructor
        public enum Player implements Endpoint {
            CREATE("/players"),
            GET_BY_ID("/players/{id}"),
            TEST("/players/test");

            private final String path;
        }
    }

    protected void assertErrorResponse(ResponseEntity<ErrorResponse> response, HttpStatus expectedStatus) {
        assertThat(response.getStatusCode().isError()).isTrue();
        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
        assertThat(response.getBody()).isNotNull();
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse.getStatus()).isEqualTo(expectedStatus);
    }
}
