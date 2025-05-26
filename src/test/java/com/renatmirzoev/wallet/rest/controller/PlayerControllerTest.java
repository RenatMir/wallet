package com.renatmirzoev.wallet.rest.controller;

import com.renatmirzoev.wallet.ModelUtils;
import com.renatmirzoev.wallet.rest.AbstractRestTest;
import com.renatmirzoev.wallet.rest.model.CreatePlayerRequest;
import com.renatmirzoev.wallet.rest.model.CreatePlayerResponse;
import com.renatmirzoev.wallet.rest.model.ErrorResponse;
import com.renatmirzoev.wallet.rest.model.GetPlayerResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class PlayerControllerTest extends AbstractRestTest {

    @Test
    void shouldReturnNotFoundOnGetPlayerById() {
        ResponseEntity<ErrorResponse> response = performRequest(Endpoints.Player.GET_BY_ID.getPath(), ErrorResponse.class)
            .httpMethod(HttpMethod.GET)
            .pathParams(Map.of("id", "1"))
            .andReturn();

        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldCreateAndGetPlayerById() {
        CreatePlayerRequest request = ModelUtils.createPlayerRequest();

        ResponseEntity<CreatePlayerResponse> createResponse = performRequest(Endpoints.Player.CREATE.getPath(), CreatePlayerResponse.class)
            .httpMethod(HttpMethod.POST)
            .payload(request)
            .andReturn();

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        CreatePlayerResponse createPlayerResponse = createResponse.getBody();
        assertThat(createPlayerResponse).isNotNull();

        {
            ResponseEntity<GetPlayerResponse> getResponse = performRequest(Endpoints.Player.GET_BY_ID.getPath(), GetPlayerResponse.class)
                .httpMethod(HttpMethod.GET)
                .pathParams(Map.of("id", String.valueOf(createPlayerResponse.getPlayerId())))
                .andReturn();

            assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            GetPlayerResponse getPlayerResponse = getResponse.getBody();
            assertThat(getPlayerResponse).isNotNull();

            assertThat(getPlayerResponse.getEmail()).isEqualTo(request.getEmail());
            assertThat(getPlayerResponse.getFullName()).isEqualTo(request.getFullName());
            assertThat(getPlayerResponse.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
        }
    }

    @Test
    void shouldCreateAndGetPlayerByLocationHeader() {
        CreatePlayerRequest request = ModelUtils.createPlayerRequest();

        ResponseEntity<CreatePlayerResponse> createResponse = performRequest(Endpoints.Player.CREATE.getPath(), CreatePlayerResponse.class)
            .httpMethod(HttpMethod.POST)
            .payload(request)
            .andReturn();

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getHeaders().getLocation().toString()).isNotNull();

        {
            String url = createResponse.getHeaders().getLocation().toString();
            ResponseEntity<GetPlayerResponse> getResponse = performRequest(url, GetPlayerResponse.class)
                .httpMethod(HttpMethod.GET)
                .andReturn();

            assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            GetPlayerResponse getPlayerResponse = getResponse.getBody();
            assertThat(getPlayerResponse).isNotNull();

            assertThat(getPlayerResponse.getEmail()).isEqualTo(request.getEmail());
            assertThat(getPlayerResponse.getFullName()).isEqualTo(request.getFullName());
            assertThat(getPlayerResponse.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
        }
    }

}