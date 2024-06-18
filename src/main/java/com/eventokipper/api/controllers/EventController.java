package com.eventokipper.api.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eventokipper.api.domain.event.Event;
import com.eventokipper.api.domain.event.EventRequestDTO;
import com.eventokipper.api.domain.event.EventResponseDTO;
import com.eventokipper.api.service.EventService;

@RestController
@RequestMapping("/api/event")
public class EventController {

  @Autowired
  private EventService eventService;

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<Event> create(@RequestParam("title") String title,
      @RequestParam(value = "description", required = false) String description,
      @RequestParam("date") Long date,
      @RequestParam("city") String city,
      @RequestParam("state") String state,
      @RequestParam("remote") Boolean remote,
      @RequestParam("eventUrl") String eventUrl,
      @RequestParam(value = "image", required = false) MultipartFile image) {

    EventRequestDTO eventRequestDTO = new EventRequestDTO(title, description, date, city, state, remote, eventUrl,
        image);
    Event newEvent = this.eventService.createEvent(eventRequestDTO);
    return ResponseEntity.ok(newEvent);
  }

  @GetMapping
  public ResponseEntity<List<EventResponseDTO>> getEvents(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    List<EventResponseDTO> events = this.eventService.getUpcomingEvents(page, size);

    return ResponseEntity.ok(events);
  }

  @GetMapping("/filter")
  public ResponseEntity<List<EventResponseDTO>> getfilteredEvents(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String uf,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

    List<EventResponseDTO> events = this.eventService.getFilteredEvents(page, size, title, city, uf, startDate,
        endDate);

    return ResponseEntity.ok(events);
  }

}

// Page<Event> findFilteredEvents(@Param("title") String title,
// @Param("city") String city,
// @Param("uf") String uf,
// @Param("startDate") Date startDate,
// @Param("endDate") Date endDate,
// Pageable pageable);