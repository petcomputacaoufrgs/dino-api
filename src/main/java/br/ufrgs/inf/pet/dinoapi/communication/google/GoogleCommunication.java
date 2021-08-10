package br.ufrgs.inf.pet.dinoapi.communication.google;

import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleAPIHeaderEnum;
import br.ufrgs.inf.pet.dinoapi.enumerable.HttpContentTypeEnum;
import br.ufrgs.inf.pet.dinoapi.enumerable.HttpHeaderEnum;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class GoogleCommunication extends LogUtilsBase {

    public GoogleCommunication(LogAPIErrorServiceImpl logAPIErrorService) {
        super(logAPIErrorService);
    }

    public HttpRequest.Builder createBaseRequest(String accessToken) {
        return HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .header(GoogleAPIHeaderEnum.AUTHORIZATION.getValue(), "Bearer " + accessToken)
                .header(HttpHeaderEnum.CONTENT_TYPE.getValue(), HttpContentTypeEnum.JSON.getValue());
    }

    public HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        return HttpClient
                .newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }
}
