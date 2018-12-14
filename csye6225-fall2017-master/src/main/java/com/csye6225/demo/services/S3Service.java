package com.csye6225.demo.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.csye6225.demo.bean.User;
import com.csye6225.demo.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class S3Service {

        @Autowired
        AttachmentRepository attachmentRepository;

       private String bucketName = "";


        public  void uploadObjectToS3(MultipartFile file, String taskKey) throws IOException {
            System.out.println("its here in S3Controller: ");


            try {

                AmazonS3 s3Client = new AmazonS3Client();

                System.out.println("before >>>>>>>>>>>>>>>>>>>>");
                List<Bucket> t = s3Client.listBuckets();
                for(Bucket buck:t){
                    if(buck.getName().endsWith(".me.bucket")){
                        bucketName = buck.getName();
                    }

                }
                System.out.println("after >>>>>>>>>>>>>>>>>>>>>");

                InputStream is= file.getInputStream();
                String contentType = file.getContentType();
                Long fileSize = file.getSize();
                ObjectMetadata meta = new ObjectMetadata();
                meta.setContentType(contentType);
                meta.setContentLength(fileSize);

                s3Client.putObject(new PutObjectRequest(bucketName, taskKey, is, meta));

            } catch (AmazonServiceException ase) {
                System.out.println("Error Message:    " + ase.getMessage());
                System.out.println("HTTP Status Code: " + ase.getStatusCode());
                System.out.println("AWS Error Code:   " + ase.getErrorCode());
                System.out.println("Error Type:       " + ase.getErrorType());
                System.out.println("Request ID:       " + ase.getRequestId());
            }

            catch (AmazonClientException ace) {
                System.out.println("Error Message: " + ace.getMessage());
            }


        }



}
