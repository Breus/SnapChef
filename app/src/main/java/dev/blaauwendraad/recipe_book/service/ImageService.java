package dev.blaauwendraad.recipe_book.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import java.io.File;
import java.util.UUID;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ApplicationScoped
public class ImageService {
    private static final String BUCKET_NAME = "images";
    private S3Client s3Client;

    @Inject
    public ImageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String putImage(File image) {
        String key = image.getName() + "_" + UUID.randomUUID().toString();
        putImage(image, key);
        return key;
    }

    public void putImage(File image, String imageKey) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(imageKey)
                .contentType("image/jpeg")
                .build();
        s3Client.putObject(request, RequestBody.fromFile(image));
    }

    public byte[] getImage(String imageKey) {
        try {
            GetObjectRequest getObjectRequest =
                    GetObjectRequest.builder().bucket(BUCKET_NAME).key(imageKey).build();
            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
            return objectBytes.asByteArray();
        } catch (NoSuchKeyException e) {
            throw new EntityNotFoundException("Image not found for imageKey: " + imageKey, e);
        }
    }

    public void deleteImage(String imageKey) {
        DeleteObjectRequest deleteObjectRequest =
                DeleteObjectRequest.builder().bucket(BUCKET_NAME).key(imageKey).build();
        s3Client.deleteObject(deleteObjectRequest);
    }
}
