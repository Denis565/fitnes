package com.example.fitnes.controllers;

import com.example.fitnes.models.Employee;
import com.example.fitnes.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/")
    public String employeeview(Model model) {
        Iterable<Employee> emp = employeeRepository.findAll();
        model.addAttribute("allEmployee",emp);
        return "employee/employee-view";
    }
}
