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
import java.util.Optional;

@Controller
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/")
    public String subscriptionview(Model model){
        Iterable<Subscription> subscriptions = subscriptionRepository.findAll();
        model.addAttribute("allsubscription", subscriptions);
        return  "subscription/subscription-view";
    }

    @PostMapping("subscription-view/{id}/del")
    public String delsubscription(
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
       // model.addAttribute("allServiceSelect",serviceSelectServ);
        return "subscription/subscription-add";
    }

    @PostMapping("/add")
    public String subscriptionadd(
            @Valid Subscription subscription,
            BindingResult bindingResult,
            @Valid Service service,
            BindingResult bindingResultService,
            Model model) {

        boolean errorsB = true;

        if (bindingResult.hasErrors() || bindingResultService.hasErrors()){
            errorsB = false;
        }

        if (subscriptionRepository.findBySubscriptionNumber(subscription.getSubscriptionNumber()) != null){
            ObjectError error = new ObjectError("subscriptionNumber","Такой номер абанимента уже существует..");
            bindingResult.addError(error);
            errorsB = false;
        }

        if (subscriptionRepository.findByName(subscription.getName()) != null){
            ObjectError error = new ObjectError("name","Такое название абонимента уже существует.");
            bindingResult.addError(error);
            errorsB = false;
        }

        if (!errorsB){
            Iterable<Service> ser = serviceRepository.findAll();
            model.addAttribute("allService",ser);
           // model.addAttribute("allServiceSelect",serviceSelectServ);
            return "subscription/subscription-add";
        }

        subscriptionRepository.save(subscription);
        return "redirect:/subscription/";
    }

    @GetMapping("/subscription-view/{id}/edit")
    public String editsubscriptionview(
            Subscription subscription,
            @PathVariable(value = "id") Long id,
            Model model)
    {
        if (!subscriptionRepository.existsById(id))
        {
            return "redirect:/subscription/";
        }

        Subscription sub = subscriptionRepository.findById(id).orElseThrow();
        subscription.setSubscriptionNumber(sub.getSubscriptionNumber());
        subscription.setName(sub.getName());
        subscription.setTimePeriod(sub.getTimePeriod());
        subscription.setPrice(sub.getPrice());

        return "subscription/edit-subscription";
    }

    @PostMapping("/subscription-view/{id}/edit")
    public String editsubscription(
            @Valid Subscription subscription,
            BindingResult bindingResult,
            @PathVariable(value = "id") Long id,
            Model model)
    {
        boolean errorsB = true;

        if (bindingResult.hasErrors()){
            errorsB = false;
        }

        Subscription subNumber = subscriptionRepository.findBySubscriptionNumber(subscription.getSubscriptionNumber());
        Subscription subName =subscriptionRepository.findByName(subscription.getName());

        if (subNumber != null && !subNumber.getId().equals(subscriptionRepository.findById(id).orElseThrow().getId())){
            ObjectError error = new ObjectError("subscriptionNumber","Такой номер абанимента уже существует..");
            bindingResult.addError(error);
            errorsB = false;
        }

        if (subName != null && !subName.getId().equals(subscriptionRepository.findById(id).orElseThrow().getId())){
            ObjectError error = new ObjectError("name","Такое название абонимента уже существует.");
            bindingResult.addError(error);
            errorsB = false;
        }

        if (!errorsB){
            return "subscription/edit-subscription";
        }

        subscriptionRepository.save(subscription);

        return "redirect:/subscription/";
    }

    @GetMapping("/addService/{id}")
    public String subscriptionaddserviceview(@PathVariable(value = "id") Long id,Model model) {
        ArrayList<Service> servic = serviceRepository.findByIDSubscription(id);
        model.addAttribute("allServiceSelect",servic);
        Iterable<Service> ser = serviceRepository.findAll();
        model.addAttribute("allService",ser);
        model.addAttribute("namesubscription",subscriptionRepository.findById(id).orElseThrow().getName());
        model.addAttribute("idscription",id);
        return "subscription/subscription-service-add";
    }

    @PostMapping("/addService/{id}")
    public String subscriptionaddservice(@RequestParam Long idService,@PathVariable(value = "id") Long id,Model model) {

        if (serviceRepository.findByIDSubscriptionIDService(id,idService) == null) {
            Subscription sub = subscriptionRepository.findById(id).orElseThrow();
            serviceRepository.findById(idService).orElseThrow().getSubscriptions().add(sub);
            subscriptionRepository.save(sub);
        }
        return "redirect:/subscription/addService/"+id;
    }

    @PostMapping("subscription-view/{idService}/delService/{idSubscription}")
    public String deleteemployee(
            @PathVariable(value = "idService") Long idService,
            @PathVariable(value = "idSubscription") Long idSubscription,
            Model model)
    {
        Subscription sub = subscriptionRepository.findById(idSubscription).orElseThrow();
        serviceRepository.findById(idService).orElseThrow().getSubscriptions().remove(sub);
        subscriptionRepository.save(sub);
        return "redirect:/subscription/addService/"+idSubscription;
    }

    @GetMapping("/subscription-information/{id}")
    public String viewinformationworker(@PathVariable(value = "id") Long id, Model model) {
        ArrayList<Service> allServiceSelected = serviceRepository.findByIDSubscription(id);
        Optional<Subscription> subscription = subscriptionRepository.findById(id);
        ArrayList<Subscription> res = new ArrayList<>();
        subscription.ifPresent(res::add);

        model.addAttribute("onesubscription",res);
        model.addAttribute("allService",allServiceSelected);
        return "subscription/information-subscription";
    }


}
