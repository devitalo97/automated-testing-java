package steps;

import com.networknt.schema.ValidationMessage;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import models.SemaforoModel;
import models.StatusErrorMessageModel;
import org.junit.Assert;
import services.CadastroSemaforosService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Então("o status code da resposta de criacao deve ser {int}")
    public void oStatusCodeDaRespostaDeCriacaoDeveSer(int statusCode) {
        Assert.assertEquals(statusCode, cadastroSemaforosService.createdResponse.statusCode());
    }

    @E("a mensagem de erro deve informar {string}")
    public void aMensagemDeErroDeveInformar(String message) {
        StatusErrorMessageModel statusErrorMessageModel = cadastroSemaforosService.gson.fromJson(
                cadastroSemaforosService.createdResponse.jsonPath().prettify(), StatusErrorMessageModel.class);
        Assert.assertEquals(message, statusErrorMessageModel.getStatus());
    }

    @E("que eu recupere o ID do semáforo criado no contexto")
    public void queEuRecupereOIDDoSemaforoCriadoNoContexto() {
        cadastroSemaforosService.retrieveIdTrafficLight();
    }

    @Quando("eu enviar a requisição DELETE para o endpoint {string} com o ID do semáforo")
    public void euEnviarARequisicaoDELETEParaOEndpointComOIDDoSemaforo(String endPoint) {
        cadastroSemaforosService.deleteTrafficLight(endPoint);
    }

    @Então("o status code da resposta de delecao deve ser {int}")
    public void oStatusCodeDaRespostaDeDelecaoDeveSer(int statusCode) {
        Assert.assertEquals(statusCode, cadastroSemaforosService.deletedResponse.statusCode());
    }

    @E("que o arquivo de contrato esperado é o {string}")
    public void queOArquivoDeContratoEsperadoÉO(String contractFileName) throws IOException {
        cadastroSemaforosService.setContract(contractFileName);
    }

    @Então("a resposta da requisição deve estar em conformidade com o contrato selecionado")
    public void aRespostaDaRequisiçãoDeveEstarEmConformidadeComOContratoSelecionado() throws IOException {
        Set<ValidationMessage> validateResponse = cadastroSemaforosService.validateResponseAgainstSchema();
        Assert.assertTrue("O contrato está inválido. Erros encontrados: " + validateResponse, validateResponse.isEmpty());
    }

}
