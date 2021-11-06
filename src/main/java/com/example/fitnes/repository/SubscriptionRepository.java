package com.example.fitnes.repository;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription,Long> {
    Subscription findBySubscriptionNumber(int number);
    Subscription findByName (String name);
}
