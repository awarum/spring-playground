package com.nwboxed.simplespring;

import com.nwboxed.simplespring.api.CarController;
import com.nwboxed.simplespring.model.Car;
import org.apache.logging.log4j.util.Base64Util;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authorization.AuthoritiesAuthorizationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static groovy.json.JsonOutput.toJson;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarApplicationTest {

    @InjectMocks
    CarController carController;
    @Autowired
    MockMvc mvc;

    @Test
    public void contextStarts() {
    }

    @Test
    public void shouldHaveEmptyDatabase() throws Exception {
        mvc.perform(
                get("/api/cars")
                .header(HttpHeaders.AUTHORIZATION,
                        buildBasicAuthorizationHeader("admin","password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    public void shouldCreateRetrieveListAndDelete() throws Exception {
        Car newCar = new Car("Test", "Green");

        // CREATE
        MvcResult postResult = mvc.perform(post("/api/cars")
                        .header(HttpHeaders.AUTHORIZATION, buildBasicAuthorizationHeader("admin","password"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(newCar)))
                .andExpect(status().isCreated()).andReturn();
        String id = getResourceIdFromUrl(postResult.getResponse().getRedirectedUrl());
        System.out.println(id);

        String url = "/api/cars/" + id;

        // GET
        MvcResult getResult = mvc.perform(get(url)
                .header(HttpHeaders.AUTHORIZATION, buildBasicAuthorizationHeader("admin","password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['colour']", is("Green")))
                .andExpect(jsonPath("$['type']", is("Test")))
                .andReturn();
        System.out.println(getResult.getResponse().getContentAsString());

        MvcResult listAllResult =
                mvc.perform(get("/api/cars")
                                .header(HttpHeaders.AUTHORIZATION, buildBasicAuthorizationHeader("admin","password")))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1))).andReturn();
        System.out.println(listAllResult.getResponse().getContentAsString());

        // DELETE
        mvc.perform(delete(url)
                .header(HttpHeaders.AUTHORIZATION, buildBasicAuthorizationHeader("admin","password")))
                .andExpect(status().isNoContent())
                .andReturn();

        // CANNOT DELETE AGAIN
        mvc.perform(delete(url)
                .header(HttpHeaders.AUTHORIZATION, buildBasicAuthorizationHeader("admin","password")))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private String getResourceIdFromUrl(String locationUrl) {
        String[] parts = locationUrl.split("/");
        return parts[parts.length - 1];
    }

    private String buildBasicAuthorizationHeader(String username, String password) {
        return "Bearer " + Base64Util.encode(username + ":" + password);
    }
}