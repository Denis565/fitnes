package com.example.fitnes.controllers;

import com.example.fitnes.models.*;
import com.example.fitnes.repository.EmployeeRepository;
import com.example.fitnes.repository.PhoneRepository;
import com.example.fitnes.repository.PostRepository;
import com.example.fitnes.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String regview(){
        return "registration";
    }

    @PostMapping("/registration")
    public String reg(Worker user, Model model){
        Worker userFromDB = workerRepository.findByLogin(user.getLogin());
        if (userFromDB != null){
            model.addAttribute("error","Пользователь с таким логином существует");
            return "registration";
        }

        Long l = 1L;

        Post post = postRepository.findById(l).orElseThrow();
        Employee emp =  employeeRepository.findById(l).orElseThrow();
        Phone ph =  phoneRepository.findById(l).orElseThrow();

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPost_list(post);
        user.setEmployee_list(emp);
        user.setPhone_list(ph);
        workerRepository.save(user);
        return "redirect:/login";
    }
}
