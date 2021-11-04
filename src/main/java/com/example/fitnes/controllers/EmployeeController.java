package com.example.fitnes.controllers;

import com.example.fitnes.models.Employee;
import com.example.fitnes.models.Passport;
import com.example.fitnes.models.Post;
import com.example.fitnes.models.Worker;
import com.example.fitnes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PassportRepository passportRepository;

    @GetMapping("/add")
    public String addemployeeview(Employee employee,Passport passport, Model model) {
        return "employee/employee-add";
    }

    @PostMapping("/add")
    public String addemployee(
            @Valid Employee employee,
            BindingResult bindingResult,
            @Valid Passport passport,
            BindingResult bindingResultPass,
            Model model) throws ParseException {

        boolean errorsB = true;

        if (bindingResult.hasErrors() || bindingResultPass.hasErrors()){
            errorsB = false;
        }

        if (passportRepository.findBySeries(passport.getSeries()) != null){
            ObjectError error = new ObjectError("series","Паспорт с такой серией уже существует");
            bindingResult.addError(error);
            errorsB = false;
        }

        if (passportRepository.findByNumber(passport.getNumber()) != null){
            ObjectError error = new ObjectError("number","Паспорт с таким номером уже существует");
            bindingResult.addError(error);
            errorsB = false;
        }

        if (employeeRepository.findBySurnameAndNameAndPatronymic(employee.getSurname(),employee.getName(),employee.getPatronymic()) != null){
            ObjectError error = new ObjectError("surname","Пользователь с таким ФИО уже существует");
            bindingResult.addError(error);
            errorsB=false;
        }

        if(!errorsB){
            return "employee/employee-add";
        }

        passportRepository.save(passport);
        Passport pasSerach = passportRepository.findBySeriesAndNumber(passport.getSeries(),passport.getNumber());
        employee.setPassport(pasSerach);
        employeeRepository.save(employee);
        return "redirect:/";
    }

    @PostMapping("/employee-view/{id}/del")
    public String deleteemployee(
            @PathVariable(value = "id") Long id,
            Model model)
    {
        Employee employeeDelete = employeeRepository.findById(id).orElseThrow();
        Passport passport = passportRepository.findById(employeeDelete.getPassport().getId()).orElseThrow();
        employeeRepository.delete(employeeDelete);
        passportRepository.delete(passport);
        return "redirect:/";
    }

    @GetMapping("/employee-view/{id}/edit")
    public String editemployeeview(
            Employee employee,
            Passport passport,
            @PathVariable(value = "id") Long id,
            Model model)
    {
        if (!employeeRepository.existsById(id))
        {
            return "redirect:/employee/";
        }

        Employee emp = employeeRepository.findById(id).orElseThrow();
        employee.setName(emp.getName());
        employee.setSurname(emp.getSurname());
        employee.setPatronymic(emp.getPatronymic());
        employee.setDateBirth(emp.getDateBirth());
        employee.setPlaceResidence(emp.getPlaceResidence());
        employee.setGender(emp.getGender());

        Passport pass = passportRepository.findById(emp.getPassport().getId()).orElseThrow();
        passport.setNumber(pass.getNumber());
        passport.setSeries(pass.getSeries());

        return "employee/edit-employee";
    }

    @PostMapping("/employee-view/{id}/edit")
    public String editemployee(
            @PathVariable(value = "id") Long id,
            @Valid Employee employee,
            BindingResult bindingResult,
            @Valid Passport passport,
            BindingResult bindingResultPass,
            Model model){

        boolean errorsB = true;

        Passport passSeries = passportRepository.findBySeries(passport.getSeries());
        Passport passNumber = passportRepository.findByNumber(passport.getNumber());
        Employee emp = employeeRepository.findBySurnameAndNameAndPatronymic(employee.getSurname(),employee.getName(),employee.getPatronymic());
        Long idPas = passport.getId();

        if (bindingResult.hasErrors() || bindingResultPass.hasErrors()){
            errorsB = false;
        }

        if (passSeries != null && !passSeries.getId().equals(idPas)){
            ObjectError error = new ObjectError("series","Паспорт с такой серией уже существует");
            bindingResult.addError(error);
            errorsB = false;
        }

        if (passNumber != null && !passNumber.getId().equals(idPas)){
            ObjectError error = new ObjectError("number","Паспорт с таким номером уже существует");
            bindingResult.addError(error);
            errorsB = false;
        }

        if (emp!= null && !emp.getId().equals(employee.getId())){
            ObjectError error = new ObjectError("surname","Пользователь с таким ФИО уже существует");
            bindingResult.addError(error);
            errorsB = false;
        }

        if(!errorsB){
            return "employee/edit-employee";
        }

        employee.setPassport(passport);

        passportRepository.save(passport);
        employeeRepository.save(employee);
        return "redirect:/";
    }
}
