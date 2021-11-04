package com.example.fitnes.controllers;

import com.example.fitnes.models.*;
import com.example.fitnes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PassportRepository passportRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/")
    public String clientview(Model model){
        Iterable<Client> clients = clientRepository.findAll();
        model.addAttribute("allclients", clients);
        return  "client/client-view";
    }
    @PostMapping("/client-view/{id}/del")
    public String deleteemployee(
            @PathVariable(value = "id") Long id,
            Model model)
    {
        Client client = clientRepository.findById(id).orElseThrow();
        Phone phone = phoneRepository.findById(client.getPhone().getId()).orElseThrow();
        Passport passport = passportRepository.findById(client.getPassport().getId()).orElseThrow();
        clientRepository.delete(client);
        phoneRepository.delete(phone);
        passportRepository.delete(passport);
        return "redirect:/worker/";
    }

    @GetMapping("/add")
    public String addemployeeview(Client client, Passport passport,Phone phone, Model model) {
        return "client/client-add";
    }
}
