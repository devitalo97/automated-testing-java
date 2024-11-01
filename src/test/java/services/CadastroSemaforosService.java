package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import models.SemaforoModel;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class CadastroSemaforosService {

    final SemaforoModel semaforoModel = new SemaforoModel();
    public final Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    public Response createdResponse;
    public Response authResponse;
    public Response deletedResponse;
    String baseUrl = "http://localhost:8080";
    private String authToken;
    private String idTrafficLight;
    String schemasPath = "src/test/resources/schemas/";
    JSONObject jsonSchema;
    private final ObjectMapper mapper = new ObjectMapper();

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
        createdResponse = given()
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

        authResponse = given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post(loginEndpoint)
                .then()
                .extract()
                .response();

        authToken = authResponse.jsonPath().getString("token");
    }

    public void retrieveIdTrafficLight() {
        SemaforoModel semaforoModel = gson.fromJson(
                createdResponse.jsonPath().prettify(), SemaforoModel.class);
        idTrafficLight = String.valueOf(gson.fromJson(createdResponse.jsonPath().prettify(), SemaforoModel.class).getId());
    }

    public void deleteTrafficLight(String endPoint) {
        String url = String.format("%s%s/%s", baseUrl, endPoint, idTrafficLight);
        deletedResponse = given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .when()
                .delete(url)
                .then()
                .extract()
                .response();
    }

    private JSONObject loadJsonFromFile(String filePath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            JSONTokener tokener = new JSONTokener(inputStream);
            return new JSONObject(tokener);
        }
    }

    public void setContract(String contract) throws IOException {
        switch (contract) {
            case "Cadastro bem-sucedido de semaforo" -> jsonSchema = loadJsonFromFile(schemasPath + "cadastro-bem-sucedido-de-semaforo.json");
            default -> throw new IllegalStateException("Unexpected contract" + contract);
        }
    }

    public Set<ValidationMessage> validateResponseAgainstSchema() throws IOException
    {
        JSONObject jsonResponse = new JSONObject(createdResponse.getBody().asString());
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema schema = schemaFactory.getSchema(jsonSchema.toString());
        JsonNode jsonResponseNode = mapper.readTree(jsonResponse.toString());
        Set<ValidationMessage> schemaValidationErrors = schema.validate(jsonResponseNode);
        return schemaValidationErrors;
    }
}
