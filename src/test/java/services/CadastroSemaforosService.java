package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import models.SemaforoModel;

public class CadastroSemaforosService {

    final SemaforoModel semaforoModel = new SemaforoModel();
    public final Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    public Response response;
    String baseUrl = "http://localhost:8080";
    private String authToken;

    public void setFields(String field, String value) {
        switch (field) {
            case "id" -> semaforoModel.setId(Long.parseLong(value));
            case "lat" -> semaforoModel.setLat(Double.parseDouble(value));
            case "lng" -> semaforoModel.setLng(Double.parseDouble(value));
            case "status" -> semaforoModel.setStatus(value);
            default -> throw new IllegalStateException("Unexpected feld" + field);
        }
    }

    public void createTrafficLight(String endPoint) {
        String url = baseUrl + endPoint;
        String bodyToSend = gson.toJson(semaforoModel);
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(bodyToSend)
                .when()
                .post(url)
                .then()
                .extract()
                .response();
    }

    public void authenticate(String username, String password) {
        String loginEndpoint = baseUrl + "/auth/sign-in";
        String loginBody = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", username, password);

        response = given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post(loginEndpoint)
                .then()
                .extract()
                .response();

        authToken = response.jsonPath().getString("token");
    }
}
