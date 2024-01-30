package com.tzyel.springbaseproject.controller.api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.tzyel.springbaseproject.dto.mail.MailAttachmentDto;
import com.tzyel.springbaseproject.dto.mail.template.DummyMailTemplateDto;
import com.tzyel.springbaseproject.entity.redis.StudentRedisEntity;
import com.tzyel.springbaseproject.repository.redis.StudentRedisRepository;
import com.tzyel.springbaseproject.service.MailService;
import com.tzyel.springbaseproject.service.RedisService;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/example")
public class ExampleCodeController extends ApiBaseController {
    @Value("${application.aws.bucket}")
    private String bucket;
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private MailService mailService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private StudentRedisRepository studentRedisRepository;

    /**
     * {@code
     * curl -X GET http://localhost:8080/api/example/redis
     * }
     */
    @GetMapping("redis")
    public void redisExample() throws Exception {
        StudentRedisEntity studentRedisEntity = new StudentRedisEntity();
        studentRedisEntity.setId(new Date().toString());
        studentRedisEntity.setName("Name 1");
        studentRedisRepository.save(studentRedisEntity);

        studentRedisRepository.findAll();

        redisService.setValue("KEY", studentRedisEntity, 100000000, true);
        redisService.getValue("KEY");
    }

    /**
     * {@code
     * curl -X GET http://localhost:8080/api/example/mail
     * }
     */
    @GetMapping("mail")
    public void mailExample() throws Exception {
        DummyMailTemplateDto mailTemplateDto = new DummyMailTemplateDto("Name", "123");
        mailTemplateDto.setAttachmentList(Arrays.asList(new MailAttachmentDto("testFile.csv", """
                        1,2,3
                        4,5,6
                        ,,r
                        """.getBytes(), "text/csv"),
                new MailAttachmentDto("testFile.xml", Files.readAllBytes(Path.of("/path/to/file.xml")), ContentType.TEXT_XML.getMimeType())
        ));
        mailService.sendMail(mailTemplateDto, List.of("receiver_sbp_project@example.com"));
    }

    /**
     * {@code
     * curl -X GET http://localhost:8080/api/example/aws
     * }
     */
    @GetMapping("aws")
    public void awsExample() throws Exception {
        System.out.println(amazonS3.listBuckets());

        // The key of the object in the bucket, usually the name of the file.
        final String key = "hello-v2.txt";

        // Creating a File object and FileInputStream.
        File file = new File("/path/to/hello-v2.txt");
        InputStream fileInputStream = new FileInputStream(file);

        // Creating an ObjectMetadata to specify the content type and content length.
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/plain");
        metadata.setContentLength(file.length());

        // Put the file into the S3 bucket
        amazonS3.putObject(new PutObjectRequest(bucket, key, fileInputStream, metadata));

        // Retrieving the object from the bucket.
        S3Object s3Object = amazonS3.getObject(bucket, key);

        // Read the text content of the file using a BufferedReader.
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        new BufferedReader(new InputStreamReader(objectInputStream));
    }
}
