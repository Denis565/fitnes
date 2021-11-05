package com.example.fitnes.controllers;

import com.example.fitnes.models.*;
import com.example.fitnes.repository.ServiceRepository;
import com.example.fitnes.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    ArrayList<Long> idServiceArray = new ArrayList<Long>();
    ArrayList<String> idServiceArray = new ArrayList<Long>();


    @GetMapping("/")
    public String subscriptionview(Model model){
        Iterable<Subscription> subscriptions = subscriptionRepository.findAll();
        model.addAttribute("allsubscription", subscriptions);
        return  "subscription/subscription-view";
    }

    @PostMapping("subscription-view/{id}/del")
    public String deleteemployee(
            @PathVariable(value = "id") Long id,
            Model model)
    {
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow();
        subscriptionRepository.delete(subscription);
        return "redirect:/subscription/";
    }

    @GetMapping("/add")
    public String subscriptionaddview(Subscription subscription, Service service,Model model) {
        Iterable<Service> ser = serviceRepository.findAll();
        model.addAttribute("allService",ser);
        return "subscription/subscription-add";
    }

    @PostMapping("/add")
    public String subscriptionadd(
            @Valid Subscription subscription,
            BindingResult bindingResult,
            @Valid Service service,
            BindingResult bindingResultService,
            Model model) {


        return "subscription/subscription-add";
    }

    @PostMapping("/addService")
    public String subscriptionaddserviceview(@RequestParam Long idService, @Valid Service service,BindingResult bindingResult, Model model) {

        if (!idServiceArray.contains(idService)){
            idServiceArray.add(idService);
        }
        return "redirect:/subscription/add";
    }


}
