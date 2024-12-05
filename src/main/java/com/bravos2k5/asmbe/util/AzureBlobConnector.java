package com.bravos2k5.asmbe.util;

import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import lombok.Getter;

import java.time.Duration;
import java.util.Properties;

public final class AzureBlobConnector {

    private static AzureBlobConnector instance;
    private final StorageSharedKeyCredential sharedKeyCredential;
    public final String endPoint;
    @Getter
    private final BlobServiceClient client;

    private AzureBlobConnector() {
        String acName = "bravosstorage";
        String acKey = System.getenv("ABS_KEY");
        endPoint = String.format("https://%s.blob.core.windows.net/",acName);
        sharedKeyCredential = new StorageSharedKeyCredential(acName,acKey);
        client = buildClient();
    }

    public static AzureBlobConnector gI() {
        if(instance == null) {
            instance = new AzureBlobConnector();
        }
        return instance;
    }

    private BlobServiceClient buildClient() {
        if(endPoint == null || sharedKeyCredential == null) {
            throw new RuntimeException("AzureBlobConnector bị lỗi, không thể tạo client");
        }
        BlobServiceClientBuilder builder = new BlobServiceClientBuilder()
                .endpoint(endPoint)
                .credential(sharedKeyCredential);
        builder.httpClient(new NettyAsyncHttpClientBuilder()
                .connectTimeout(Duration.ofSeconds(8))
                .build());
        return builder.buildClient();
    }

}