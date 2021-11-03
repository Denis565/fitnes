package com.example.fitnes.controllers;

import com.example.fitnes.models.*;
import com.example.fitnes.repository.*;
import org.hibernate.criterion.Example;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;

@Controller
@RequestMapping("/worker")
public class WorkerController {

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

    @GetMapping("/")
    public String workerview(Model model){
        Iterable<Worker> workers = workerRepository.findAll();
        model.addAttribute("allWorker", workers);
        return  "worker/worker-view";
    }

    @GetMapping("/add")
    public String workeraddview(Worker worker, Phone phone, Model model) {

        Iterable<Employee> emp = employeeRepository.findAll();
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("allEmployee",emp);
        model.addAttribute("allPost",posts);
        return "worker/worker-add";
    }

    @PostMapping("/add")
    public String workeradd(
            @RequestParam Long idPost,
            @RequestParam Long idEmployee,
            @Valid Worker worker,
            BindingResult bindingResultW,
            @Valid Phone phone,
            BindingResult bindingResult,
            Model model) {

        boolean phoneB = true;
        boolean mainB = true;

        String phM = phone.getMainPhone().replaceAll("[^\\d]", "");
        String phH = phone.getHomePhone().replaceAll("[^\\d]", "");
        String phAddition = phone.getAdditionalPhone().replaceAll("[^\\d]", "");

        if (bindingResult.hasErrors()){
            mainB = false;
        }

        if ((!phM.equals("") && phM.length() < 11) || (!phH.equals("") && phH.length() < 11) || (!phAddition.equals("") && phAddition.length() < 11)){
            ObjectError error = new ObjectError("mainPhone","Телефон должен содержать 11 цифр");
            bindingResult.addError(error);
            phoneB = false;
        }

        Phone phones_list = phoneRepository.findByMainPhone(phM);//найденный телефон

        if(phones_list != null){
            Worker workSearch = workerRepository.findByPhonelistAndEmployeelist(phones_list.getId(),idEmployee);
            if (workSearch == null){//не найден
                ObjectError error = new ObjectError("additionalPhone","Такой телефон уже есть в базе");
                bindingResult.addError(error);
                phoneB = false;
            }
        }

        if (!mainB || !phoneB){
            Iterable<Employee> emp = employeeRepository.findAll();
            Iterable<Post> posts = postRepository.findAll();
            model.addAttribute("allEmployee",emp);
            model.addAttribute("allPost",posts);
            return "worker/worker-add";
        }

        if(phones_list == null) {
            phoneRepository.save(phone);
        }

        worker.setEmployee_list(employeeRepository.findById(idEmployee).orElseThrow());
        worker.setPost_list(postRepository.findById(idPost).orElseThrow());
        worker.setActive(true);
        worker.setRoles(Collections.singleton(Role.USER));
        worker.setPassword(passwordEncoder.encode(worker.getPassword()));
        worker.setPhone_list(phoneRepository.findByMainPhone(phM));

        workerRepository.save(worker);
        return "redirect:/worker/";
    }

    @PostMapping("/worker-view/{id}/del")
    public String deleteemployee(
            @PathVariable(value = "id") Long id,
            Model model)
    {
        Worker workerDelete = workerRepository.findById(id).orElseThrow();
        workerRepository.delete(workerDelete);
        return "redirect:/worker/";
    }


}
