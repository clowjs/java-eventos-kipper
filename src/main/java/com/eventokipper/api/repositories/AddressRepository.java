package com.eventokipper.api.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventokipper.api.domain.address.Address;

public interface AddressRepository extends JpaRepository<Address, UUID> {

}
