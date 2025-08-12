package com.personal.mall.thirty;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
public class MallThirtyPartyApplicationTests {

//    @Test
//    public void testUploadFile() {
//        try {
//            // Create a minioClient with the MinIO server playground, its access key and secret key.
//            MinioClient minioClient =
//                    MinioClient.builder()
//                            .endpoint("http://127.0.0.1:9000")
//                            .credentials("minioadmin", "minioadmin")
//                            .build();
//
//            // Make 'asiatrip' bucket if not exist.
//            boolean found =
//                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("mall-product").build());
//            if (!found) {
//                // Make a new bucket called 'asiatrip'.
//                minioClient.makeBucket(MakeBucketArgs.builder().bucket("mall-product").build());
//            } else {
//                System.out.println("Bucket 'mall-product' already exists.");
//            }
//
//            // Upload '/home/user/Photos/asiaphotos.zip' as object name 'asiaphotos-2015.zip' to bucket
//            // 'asiatrip'.
//            minioClient.uploadObject(
//                    UploadObjectArgs.builder()
//                            .bucket("mall-product")
//                            .object("lyf.jpeg")
//                            .filename("/Users/liupanpan/Downloads/ac6ffe56188e7dd1adda99cdc63c2419.jpeg")
//                            .build());
//            System.out.println(
//                    "'/Users/liupanpan/Downloads/ac6ffe56188e7dd1adda99cdc63c2419.jpeg' is successfully uploaded as "
//                            + "object 'lyf.zip' to bucket 'mall-product'.");
//        } catch (MinioException e) {
//            System.out.println("Error occurred: " + e);
//            System.out.println("HTTP trace: " + e.httpTrace());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeyException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
