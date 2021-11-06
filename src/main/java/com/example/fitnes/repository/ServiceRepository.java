package com.example.fitnes.repository;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.Service;
import com.example.fitnes.models.Worker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ServiceRepository extends CrudRepository<Service,Long> {
    Service findByName (String name);

    @Query(value = "SELECT service.id,service.name FROM service INNER JOIN service_subscription on service_id = service.id WHERE subscription_id = ?1", nativeQuery = true)
    ArrayList<Service> findByIDSubscription(Long idSubscroption);

    @Query(value = "SELECT service_id FROM service_subscription WHERE subscription_id = ?1 and service_id = ?2", nativeQuery = true)
    Long findByIDSubscriptionIDService(Long idSubscroption,Long idService);
}
