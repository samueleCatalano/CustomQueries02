package com.customqueries02.customqueries02.repositories;

import com.customqueries02.customqueries02.entities.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlightRepository extends JpaRepository <Flight,Long>{

    @Override
    Page<Flight> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM Flights f WHERE f.status = ONTIME",nativeQuery = true)
    List<Flight> findByStatusOnTime();

    @Query(value = "SELECT * FROM flight AS f WHERE f.status =:p1 OR f.status=:p2", nativeQuery = true)
    List<Flight> findByTwoStatus(@Param("p1") String p1, @Param("p2") String p2);
}
