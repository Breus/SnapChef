package dev.blaauwendraad.recipe_book.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.File;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ApplicationScoped
public class ImageService {
    private static final String BUCKET_NAME = "images";
    private S3Client s3Client;

    @Inject
    public ImageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void putObject(File file) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(file.getName())
                .contentType("image/jpeg")
                .build();
        s3Client.putObject(request, RequestBody.fromFile(file));
    }

    public byte[] getObject(String objectKey) {
        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder().bucket(BUCKET_NAME).key(objectKey).build();
        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
        return objectBytes.asByteArray();
    }
}
