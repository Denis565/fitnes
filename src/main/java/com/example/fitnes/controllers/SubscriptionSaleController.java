package com.example.fitnes.controllers;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.Subscription;
import com.example.fitnes.models.SubscriptionSale;
import com.example.fitnes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping("/subscriptionsale")
public class SubscriptionSaleController {
    @Autowired
    private SubscriptionSaleRepository subscriptionSaleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @GetMapping("/")
    public String subscriptionsaleview(Model model){
        Iterable<SubscriptionSale> subscriptionSales = subscriptionSaleRepository.findAll();
        model.addAttribute("allsubscriptionsales", subscriptionSales);
        return  "subscriptionsale/subscriptionsale-view";
    }

    @PostMapping("subscriptionsale-view/{id}/del")
    public String delsubscription(
            @PathVariable(value = "id") Long id,
            Model model)
    {
        SubscriptionSale subscriptionSale = subscriptionSaleRepository.findById(id).orElseThrow();
        subscriptionSale.setClient_list(null);
        subscriptionSaleRepository.delete(subscriptionSale);
        return "redirect:/subscriptionsale/";
    }

    @GetMapping("/add")
    public String subscriptionsaleaddview(SubscriptionSale subscriptionSale, Model model) {
        Iterable<Client> clientIterable = clientRepository.findAll();
        Iterable<Subscription> subscriptionIterable = subscriptionRepository.findAll();
        model.addAttribute("allClient",clientIterable);
        model.addAttribute("allsubscription",subscriptionIterable);
        return "subscriptionsale/subscriptionsale-add";
    }

    @PostMapping("/add")
    public String subscriptionsaleadd(
            @RequestParam Long idClient,
            @RequestParam Long idSubscription,
            @Valid SubscriptionSale subscriptionSale,
            BindingResult bindingResult,
            Model model) {

        Subscription subscription = subscriptionRepository.findById(idSubscription).orElseThrow();
        Client client = clientRepository.findById(idClient).orElseThrow();
        LocalDate startDaySelected = subscriptionSale.getStartDate();

        if (bindingResult.hasErrors()) {
            init(model,idClient,idSubscription);
            return "subscriptionsale/subscriptionsale-add";
        }

        ArrayList<SubscriptionSale> subscriptionSalesArray = subscriptionSaleRepository.findByIdWorkerAndIdSubscription(idClient,idSubscription);

        if (subscriptionSalesArray.size() != 0){
            for (SubscriptionSale subS : subscriptionSalesArray ){
                if (subS.getEndDate().isAfter(startDaySelected)){
                    ObjectError error = new ObjectError("startDate", "У вас есть незаконченный такой абонимент.Выберете другую дату начала абонимента");
                    bindingResult.addError(error);
                    init(model, idClient, idSubscription);
                    return "subscriptionsale/subscriptionsale-add";
                }
            }
        }

        SubscriptionSale subscriptionSaleNew = new SubscriptionSale();
        subscriptionSaleNew.setClient_list(client);
        subscriptionSaleNew.setSubscription_list(subscription);
        subscriptionSaleNew.setStartDate(startDaySelected);
        subscriptionSaleNew.setEndDate(startDaySelected.plusMonths(subscription.getTimePeriod()));

        subscriptionSaleRepository.save(subscriptionSaleNew);

        return "redirect:/subscriptionsale/";
    }

    @GetMapping("/subscriptionsale-view/{id}/edit")
    public String editsubscriptionsaleview(
            SubscriptionSale subscriptionSale,
            @PathVariable(value = "id") Long id,
            Model model)
    {
        if (!subscriptionSaleRepository.existsById(id))
        {
            return "redirect:/subscriptionsale/";
        }

        SubscriptionSale subSale = subscriptionSaleRepository.findById(id).orElseThrow();
        subscriptionSale.setEndDate(subSale.getEndDate());
        subscriptionSale.setStartDate(subSale.getStartDate());

        Subscription sub = subscriptionRepository.findById(subSale.getSubscription_list().getId()).orElseThrow();
        Client client = clientRepository.findById(subSale.getClient_list().getId()).orElseThrow();
        init(model,client.getId(),sub.getId());

        return "subscriptionsale/edit-subscriptionsale";
    }

    @PostMapping("/subscriptionsale-view/{id}/edit")
    public String editsubscriptionsale(
            @Valid SubscriptionSale subscriptionSale,
            BindingResult bindingResult,
            @PathVariable(value = "id") Long id,
            @RequestParam Long idClient,
            @RequestParam Long idSubscription,
            Model model)
    {

        if (bindingResult.hasErrors()) {
            init(model,idClient,idSubscription);
            return "subscriptionsale/edit-subscriptionsale";
        }

        ArrayList<SubscriptionSale> subscriptionSalesArray = subscriptionSaleRepository.findByIdWorkerAndIdSubscription(idClient,idSubscription);
        SubscriptionSale subSale = subscriptionSaleRepository.findById(id).orElseThrow();

        if (subscriptionSalesArray.size() != 0){
            for (SubscriptionSale subS : subscriptionSalesArray ){
                if (subS.getEndDate().isAfter(subSale.getStartDate())){
                    ObjectError error = new ObjectError("startDate", "У вас есть незаконченный такой абонимент.Выберете другую дату начала абонимента");
                    bindingResult.addError(error);
                    init(model, idClient, idSubscription);
                    return "subscriptionsale/edit-subscriptionsale";
                }
            }
        }

        Subscription sub = subscriptionRepository.findById(idSubscription).orElseThrow();
        Client client = clientRepository.findById(idClient).orElseThrow();

        subscriptionSale.setClient_list(client);
        subscriptionSale.setSubscription_list(sub);
        subscriptionSale.setEndDate(subSale.getEndDate());
        subscriptionSale.setStartDate(subSale.getStartDate());

        subscriptionSaleRepository.save(subscriptionSale);

        return "redirect:/subscriptionsale/";
    }

    private void init(Model model,Long idClientSelected,Long idSubscriptionSelect ){
        Iterable<Client> clientIterable = clientRepository.findAll();
        Iterable<Subscription> subscriptionIterable = subscriptionRepository.findAll();
        model.addAttribute("allClient",clientIterable);
        model.addAttribute("allsubscription",subscriptionIterable);
        model.addAttribute("idClientSelected",idClientSelected);
        model.addAttribute("idSubscriptionSelect",idSubscriptionSelect);
    }
}
