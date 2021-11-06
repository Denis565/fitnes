package com.example.fitnes.controllers;

import com.example.fitnes.models.Subscription;
import com.example.fitnes.models.SubscriptionSale;
import com.example.fitnes.repository.ServiceRepository;
import com.example.fitnes.repository.SubscriptionRepository;
import com.example.fitnes.repository.SubscriptionSaleRepository;
import com.example.fitnes.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/subscriptionsale")
public class SubscriptionSaleController {
    @Autowired
    private SubscriptionSaleRepository subscriptionSaleRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @GetMapping("/")
    public String subscriptionsaleview(Model model){
        Iterable<SubscriptionSale> subscriptionSales = subscriptionSaleRepository.findAll();
        model.addAttribute("allsubscriptionsales", subscriptionSales);
        return  "subscriptionsale/subscriptionsale-view";
    }
}
