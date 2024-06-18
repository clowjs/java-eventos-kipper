package com.eventokipper.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventokipper.api.domain.address.Address;
import com.eventokipper.api.domain.event.Event;
import com.eventokipper.api.domain.event.EventRequestDTO;
import com.eventokipper.api.repositories.AddressRepository;

@Service
public class AddressService {

  @Autowired
  private AddressRepository addressRepository;

  public Address createAddress(EventRequestDTO data, Event event) {
    com.eventokipper.api.domain.address.Address newAddress = new Address();

    newAddress.setCity(data.city());
    newAddress.setUf(data.uf());
    newAddress.setEvent(event);

    return addressRepository.save(newAddress);

  }
}