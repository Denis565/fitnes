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

    ArrayList<Long> idserviceSelect = new ArrayList<Long>();
    ArrayList<Service> serviceSelectServ = new ArrayList<Service>();


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
        model.addAttribute("allServiceSelect",serviceSelectServ);
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

        if (serviceSelectServ.size() == 0){
            ObjectError error = new ObjectError("name","Вы должны выбрать услуги для абонимента.");
            bindingResult.addError(error);
            errorsB = false;
        }

        if (!errorsB){
            Iterable<Service> ser = serviceRepository.findAll();
            model.addAttribute("allService",ser);
            model.addAttribute("allServiceSelect",serviceSelectServ);
            return "subscription/subscription-add";
        }

        subscriptionRepository.save(subscription);
        for (Service i : serviceSelectServ){
            serviceRepository.findById(i.getId()).orElseThrow().getSubscriptions().add(subscription);
        }

        subscriptionRepository.save(subscription);

        return "redirect:/subscription/";
    }

    @PostMapping("/addService")
    public String subscriptionaddserviceview(@RequestParam Long idService,  @Valid Subscription subscription,
                                             BindingResult bindingResult, Model model) {

        if (!idserviceSelect.contains(idService)){
            String nameService = serviceRepository.findById(idService).orElseThrow().getName();
            idserviceSelect.add(idService);
            //serviceSelectServ.add(new Service(idService,nameService,null));
            serviceSelectServ.add(serviceRepository.findById(idService).orElseThrow());
        }
        return "redirect:/subscription/add";
    }

    @PostMapping("subscription-view/{id}/delService")
    public String deleteemployee(
            @PathVariable(value = "id") Long id,
            Model model)
    {
        serviceSelectServ.remove(idserviceSelect.indexOf(id));
        idserviceSelect.remove(id);
        return "redirect:/subscription/add";
    }


}
