package com.eventokipper.api.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventokipper.api.domain.event.Event;

public interface EventRepository extends JpaRepository<Event, UUID> {

}
