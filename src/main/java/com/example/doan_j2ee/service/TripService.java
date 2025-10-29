package com.example.doan_j2ee.service;

import com.example.doan_j2ee.dto.TripDTO;
import com.example.doan_j2ee.model.Bus;
import com.example.doan_j2ee.model.Route;
import com.example.doan_j2ee.model.Trip;
import com.example.doan_j2ee.repository.BusRepository;
import com.example.doan_j2ee.repository.RouteRepository;
import com.example.doan_j2ee.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;

    @Autowired
    public TripService(TripRepository tripRepository, BusRepository busRepository, RouteRepository routeRepository) {
        this.tripRepository = tripRepository;
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
    }

    public List<TripDTO> getAllTrips() {
        return tripRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TripDTO getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi ID: " + id));
        return toDTO(trip);
    }

    public TripDTO createTrip(TripDTO dto) {
        Bus bus = busRepository.findById(dto.getBusId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Bus ID: " + dto.getBusId()));
        Route route = routeRepository.findById(dto.getRouteId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Route ID: " + dto.getRouteId()));

        Trip trip = new Trip();
        trip.setBus(bus);
        trip.setRoute(route);
        trip.setDepartureTime(dto.getDepartureTime());
        trip.setArrivalTime(dto.getArrivalTime());
        trip.setPrice(dto.getPrice());
        trip.setStatus(dto.getStatus());

        Trip saved = tripRepository.save(trip);
        return toDTO(saved);
    }

    public TripDTO updateTrip(TripDTO dto) {
        Trip trip = tripRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi!"));
        trip.setDepartureTime(dto.getDepartureTime());
        trip.setArrivalTime(dto.getArrivalTime());
        trip.setPrice(dto.getPrice());
        trip.setStatus(dto.getStatus());
        return toDTO(tripRepository.save(trip));
    }

    public void deleteTrip(Long id) {
        if (!tripRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy chuyến đi!");
        }
        tripRepository.deleteById(id);
    }

    private TripDTO toDTO(Trip trip) {
        return new TripDTO(
                trip.getId(),
                trip.getBus().getId(),
                trip.getRoute().getRouteId(),
                trip.getDepartureTime(),
                trip.getArrivalTime(),
                trip.getPrice(),
                trip.getStatus()
        );
    }
} 
