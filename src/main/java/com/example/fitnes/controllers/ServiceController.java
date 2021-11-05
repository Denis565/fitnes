package com.example.fitnes.controllers;

import com.example.fitnes.models.*;
import com.example.fitnes.repository.ServiceRepository;
import com.example.fitnes.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;


    @GetMapping("/")
    public String serviceview(Model model){
        Iterable<Service> services = serviceRepository.findAll();
        model.addAttribute("allservice", services);
        return  "service/service-view";
    }

    @PostMapping("service-view/{id}/del")
    public String deleteemployee(
            @PathVariable(value = "id") Long id,
            Model model)
    {
        Service service = serviceRepository.findById(id).orElseThrow();
        serviceRepository.delete(service);
        return "redirect:/service/";
    }

    @GetMapping("/add")
    public String serviceaddview(Service service, Model model) {
        return "service/service-add";
    }

    @PostMapping("/add")
    public String serviceadd(@Valid Service service, BindingResult bindingResult, Model model) {

        boolean errorsB = true;

        if (bindingResult.hasErrors()){
            errorsB = false;
        }
        if (serviceRepository.findByName(service.getName()) != null){
            ObjectError error = new ObjectError("name","Такая услуга уже существует в базе.");
            bindingResult.addError(error);
            errorsB = false;
        }
        if (!errorsB){
            return "service/service-add";
        }

        serviceRepository.save(service);
        return "redirect:/service/";
    }

    @GetMapping("/service-view/{id}/edit")
    public String editserviceview(
            Service service,
            @PathVariable(value = "id") Long id,
            Model model) {

        if (!serviceRepository.existsById(id)) {
            return "redirect:/service/";
        }

        Service servces = serviceRepository.findById(id).orElseThrow();
        service.setName(servces.getName());

        return "/service/edit-service";
    }

    @PostMapping("/service-view/{id}/edit")
    public String editservice(
            @Valid Service service,
            BindingResult bindingResult,
            @PathVariable(value = "id") Long id,
            Model model) {

        boolean errorsB = true;

        if (bindingResult.hasErrors()){
            errorsB = false;
        }

        Service serv = serviceRepository.findByName(service.getName());

        if (serv != null && !serv.getId().equals(serviceRepository.findById(id).orElseThrow().getId())){
            ObjectError error = new ObjectError("name","Такая услуга уже существует в базе.");
            bindingResult.addError(error);
            errorsB = false;
        }
        if (!errorsB){
            return "/service/edit-service";
        }

        if (serv == null){
            serviceRepository.save(service);
        }

        return "redirect:/service/";
    }
}
