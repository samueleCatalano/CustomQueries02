package com.customqueries02.customqueries02.controllers;

import com.customqueries02.customqueries02.entities.Flight;
import com.customqueries02.customqueries02.repositories.FlightRepository;
import com.customqueries02.customqueries02.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/flights")
public class FlightController {


    @Autowired
    private FlightRepository flightRepository;

    @PostMapping("{n}")
    public String createFlight50(@RequestParam(required = false) Integer n) {
        if (n == null) {
            n = 100;
        }
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            Flight flight = new Flight();
            String randomString = random.ints(1, 100, 8)
                    .collect(StringBuilder::new,
                            StringBuilder::appendCodePoint,
                            StringBuilder::append)
                    .toString();
            flight.setDescription(randomString + "" + randomString + "" + randomString);
            flight.setFromAirport(randomString);
            flight.setToAirport(randomString);
            flight.setStatus(Status.values()[random.nextInt(0, 3)]);
            flightRepository.save(flight);


        }
        return HttpStatus.CREATED + "" + n + "number of flight";

    }

    @GetMapping
    public Page<Flight> getAllFlights(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            Sort sorting = Sort.by(Sort.Direction.ASC, "fromAirport");
            Pageable pageable = PageRequest.of(page, size, sorting);
            return flightRepository.findAll(pageable);
        } else return Page.empty();
    }

    @GetMapping("/ontime")
    public List<Flight> getForStatus() {
        return flightRepository.findByStatusOnTime();
    }

    @GetMapping("/status")
    public List<Flight> getFlightsByStatus(@RequestParam String p1, @RequestParam String p2, HttpServletResponse response) {
        List<Flight> flightList = new ArrayList<>();
        p1 = p1.toUpperCase();
        p2 = p2.toUpperCase();
        List<String> statusList = List.of(Status.ONTIME.toString(),
                Status.DELAYED.toString(), Status.CANCELLED.toString());
        if (statusList.contains(p1) && statusList.contains(p2)) {
            flightList.addAll(flightRepository.findByTwoStatus(p1, p2));
        } else response.setStatus(400);
        return flightList;
    }
}
