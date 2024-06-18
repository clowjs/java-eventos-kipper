package com.eventokipper.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.eventokipper.api.domain.event.Event;
import com.eventokipper.api.domain.event.EventRequestDTO;
import com.eventokipper.api.repositories.EventRepository;

@Service
public class EventService {

  @Value("${aws.bucket.name}")
  private String bucketName;

  @Autowired
  private AmazonS3 s3Client;

  @Autowired
  private EventRepository eventRepository;

  public Event createEvent(EventRequestDTO data) {
    String imgUrl = null;
    if (data.image() != null) {
      imgUrl = this.uploadImage(data.image());
    }

    Event newEvent = new Event();
    newEvent.setTitle(data.title());
    newEvent.setDescription(data.description());
    newEvent.setEventUrl(data.eventUrl());
    newEvent.setDate(new Date(data.date()));
    newEvent.setImgUrl(imgUrl);
    newEvent.setRemote(data.remote());

    eventRepository.save(newEvent);

    return newEvent;
  }

  private String uploadImage(MultipartFile image) {
    // Upload image to S3
    String filename = UUID.randomUUID() + "-" + image.getOriginalFilename();

    try {
      File file = this.convertMultiPartToFile(image);
      s3Client.putObject(bucketName, filename, file);
      file.delete();
      return s3Client.getUrl(bucketName, filename).toString();
    } catch (Exception e) {
      System.out.println("Error uploading image to S3");
      return "";
    }
  }

  private File convertMultiPartToFile(MultipartFile file) throws IOException {

    File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(file.getBytes());
    fos.close();

    return convFile;
  }
}
