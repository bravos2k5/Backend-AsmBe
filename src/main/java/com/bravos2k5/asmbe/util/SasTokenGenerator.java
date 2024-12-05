package com.bravos2k5.asmbe.util;

import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.sas.SasProtocol;

import java.time.OffsetDateTime;

public class SasTokenGenerator {

    public static String generateSasToken(String containerName, String permission, int expHours) {
        OffsetDateTime expTime = OffsetDateTime.now().plusHours(expHours);
        BlobSasPermission blobSasPermission = getBlobSasPermission(permission);
        BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expTime,blobSasPermission);
        values.setProtocol(SasProtocol.HTTPS_HTTP);
        values.setStartTime(OffsetDateTime.now().minusMinutes(5));
        return AzureBlobConnector.gI().getClient().getBlobContainerClient(containerName).generateSas(values);
    }

    private static BlobSasPermission getBlobSasPermission(String permission) {
        BlobSasPermission blobSasPermission = new BlobSasPermission();
        if(permission.contains("r")) {
            blobSasPermission.setReadPermission(true);
        }
        if(permission.contains("w")) {
            blobSasPermission.setCreatePermission(true);
            blobSasPermission.setWritePermission(true);
        }
        if(permission.contains("d")) {
            blobSasPermission.setDeletePermission(true);
            blobSasPermission.setPermanentDeletePermission(true);
        }
        return blobSasPermission;
    }

}
