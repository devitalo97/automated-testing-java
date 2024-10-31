package steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import models.StatusErrorMessageModel;
import org.junit.Assert;
import services.CadastroSemaforosService;

import java.util.List;
import java.util.Map;

public class CadastroSemaforosSteps {
    CadastroSemaforosService cadastroSemaforosService = new CadastroSemaforosService();

    @Dado("que eu estou autenticado com email {string} e senha {string}")
    public void queEuEstouAutenticadoComEmailESenha(String email, String senha) {
        cadastroSemaforosService.authenticate(email, senha);
    }

    @E("que eu tenha os seguintes dados do semáforo:")
    public void queEuTenhaOsSeguintesDadosDoSemaforo(List<Map<String, String>> rows) {
        for(Map<String, String> columns : rows) {
            cadastroSemaforosService.setFields(columns.get("campo"),  columns.get("valor"));
        }
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de semáforos")
    public void euEnviarARequisicaooParaOEndpointDeCadastroDeSemaforos(String endPoint) {
        cadastroSemaforosService.createTrafficLight(endPoint);
    }

    @Então("o status code da resposta deve ser {int}")
    public void oStatusCodeDaRespostaDeveSer(int statusCode) {
        Assert.assertEquals(statusCode, cadastroSemaforosService.response.statusCode());
    }

    @E("a mensagem de erro deve informar {string}")
    public void aMensagemDeErroDeveInformar(String message) {
        StatusErrorMessageModel statusErrorMessageModel = cadastroSemaforosService.gson.fromJson(
                cadastroSemaforosService.response.jsonPath().prettify(), StatusErrorMessageModel.class);
        Assert.assertEquals(message, statusErrorMessageModel.getStatus());
    }
}
