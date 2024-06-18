package com.eventokipper.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.eventokipper.api.domain.event.Event;
import com.eventokipper.api.domain.event.EventRequestDTO;
import com.eventokipper.api.domain.event.EventResponseDTO;
import com.eventokipper.api.repositories.EventRepository;

@Service
public class EventService {

  @Value("${aws.bucket.name}")
  private String bucketName;

  @Autowired
  private AmazonS3 s3Client;

  @Autowired
  private AddressService addressService;

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

    if (!data.remote()) {
      addressService.createAddress(data, newEvent);
    }

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

  public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Event> events = eventRepository.findUpcomingEvents(new Date(), pageable);

    return events.map(event -> new EventResponseDTO(
        event.getId(),
        event.getTitle(),
        event.getDescription(),
        event.getDate(),
        event.getAddress() != null ? event.getAddress().getCity() : "",
        event.getAddress() != null ? event.getAddress().getUf() : "",
        event.getRemote(),
        event.getEventUrl(),
        event.getImgUrl()))
        .stream().toList();
  }

  public List<EventResponseDTO> getFilteredEvents(int page, int size, String title, String city, String uf,
      Date startDate, Date endDate) {

    title = title != null ? title : "";
    city = city != null ? city : "";
    uf = uf != null ? uf : "";
    startDate = startDate != null ? startDate : new Date();
    endDate = endDate != null ? endDate : new Date(System.currentTimeMillis() + 315569520000L);

    Pageable pageable = PageRequest.of(page, size);

    Page<Event> events = eventRepository.findFilteredEvents(title, city, uf, startDate, endDate, pageable);

    return events.map(event -> new EventResponseDTO(
        event.getId(),
        event.getTitle(),
        event.getDescription(),
        event.getDate(),
        event.getAddress() != null ? event.getAddress().getCity() : "",
        event.getAddress() != null ? event.getAddress().getUf() : "",
        event.getRemote(),
        event.getEventUrl(),
        event.getImgUrl()))
        .stream().toList();
  }

  public EventResponseDTO getEventById(UUID id) {
    Event event = eventRepository.findById(id).orElse(null);

    if (event == null) {
      return null;
    }

    return new EventResponseDTO(
        event.getId(),
        event.getTitle(),
        event.getDescription(),
        event.getDate(),
        event.getAddress() != null ? event.getAddress().getCity() : "",
        event.getAddress() != null ? event.getAddress().getUf() : "",
        event.getRemote(),
        event.getEventUrl(),
        event.getImgUrl());
  }
}
