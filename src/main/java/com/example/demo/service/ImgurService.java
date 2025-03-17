package com.example.demo.service;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ImgurService {

    @Value("${imgur.client.id}")
    private String clientId;

    public String uploadImage(File image) throws IOException {
        String uploadUrl = "https://api.imgur.com/3/upload";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost uploadFile = new HttpPost(uploadUrl);
        uploadFile.addHeader("Authorization", "Client-ID " + clientId);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addPart("image", new FileBody(image));
        HttpEntity multipart = builder.build();

        uploadFile.setEntity(multipart);

        try (CloseableHttpResponse response = httpClient.execute(uploadFile)) {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String responseString = EntityUtils.toString(responseEntity);
                // Optionally parse JSON response to extract the URL
                return responseString;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
