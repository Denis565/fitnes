package com.example.fitnes.repository;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.SubscriptionSale;
import com.example.fitnes.models.TrainingSchedule;
import com.example.fitnes.models.Worker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.ArrayList;

public interface SubscriptionSaleRepository extends CrudRepository<SubscriptionSale,Long> {
    @Query(value = "SELECT * FROM `subscriptionsale` WHERE client_list_id = ?1 and subscription_list_id = ?2", nativeQuery = true)
    ArrayList<SubscriptionSale> findByIdWorkerAndIdSubscription(Long idClient, Long idSubscription);

    @Query(value = "SELECT * FROM `subscriptionsale` WHERE client_list_id = ?1 and subscription_list_id = ?2 and DATEDIFF(?3,end_date) > 0 " , nativeQuery = true)
    ArrayList<SubscriptionSale> findByIdWorkerAndIdSubscriptionAndDate(Long idClient, Long idSubscription,String date);

    @Query(value = "SELECT * FROM `subscriptionsale` WHERE DATEDIFF(CURRENT_DATE(),end_date) <= 0", nativeQuery = true)
    ArrayList<SubscriptionSale> findBySubscriptionSalesList();
}
