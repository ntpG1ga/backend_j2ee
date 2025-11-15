package com.example.doan_j2ee.repository;

import com.example.doan_j2ee.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
}
 