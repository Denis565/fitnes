package com.example.fitnes.controllers;

import com.example.fitnes.models.*;
import com.example.fitnes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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

    Long idPhone;

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
            BindingResult bindingResultWorker,
            @Valid Phone phone,
            BindingResult bindingResult,
            Model model) {

        boolean errorsB = true;

        String phM = phone.getMainPhone().replaceAll("[^\\d]", "");
        String phH = phone.getHomePhone().replaceAll("[^\\d]", "");
        String phAddition = phone.getAdditionalPhone().replaceAll("[^\\d]", "");

        if (bindingResult.hasErrors() || bindingResultWorker.hasErrors()){
            errorsB = false;
        }

        if ((!phM.equals("") && phM.length() < 11) || (!phH.equals("") && phH.length() < 11) || (!phAddition.equals("") && phAddition.length() < 11)){
            ObjectError error = new ObjectError("mainPhone","Телефон должен содержать 11 цифр");
            bindingResult.addError(error);
            errorsB = false;
        }

        Phone phones_list = phoneRepository.findByMainPhone(phM);//найденный телефон

        if(phones_list != null){
            Worker workSearch = workerRepository.findByPhonelistAndEmployeelist(phones_list.getId(),idEmployee);
            if (workSearch == null){//не найден
                ObjectError error = new ObjectError("additionalPhone","Такой телефон уже есть в базе");
                bindingResult.addError(error);
                errorsB = false;
            }
        }

        if (worker.getPassword().equals("")){
            ObjectError error = new ObjectError("password","Поле с паролем не должно быть пустым");
            bindingResult.addError(error);
            errorsB = false;
        }

        if (!errorsB){

            Employee employeers = employeeRepository.findById(idEmployee).orElseThrow();
            Post post = postRepository.findById(idPost).orElseThrow();

            Iterable<Employee> emp = employeeRepository.findAll();
            Iterable<Post> posts = postRepository.findAll();
            model.addAttribute("allEmployee",emp);
            model.addAttribute("allPost",posts);
            model.addAttribute("employeeselected", employeers.getPassport().getId());
            model.addAttribute("postselected", post.getName());
            return "worker/worker-add";
        }

        if(phones_list == null) {
            phoneRepository.save(phone);
        }else {
            phone = phones_list;
        }

        worker.setEmployee_list(employeeRepository.findById(idEmployee).orElseThrow());
        worker.setPost_list(postRepository.findById(idPost).orElseThrow());
        worker.setActive(true);
        worker.setRoles(Collections.singleton(Role.USER));
        worker.setPassword(passwordEncoder.encode(worker.getPassword()));
        worker.setPhone_list(phone);
        workerRepository.save(worker);
        return "redirect:/worker/";
    }

    @PostMapping("/worker-view/{id}/del")
    public String deleteemployee(
            @PathVariable(value = "id") Long id,
            Model model)
    {
        Worker workerDelete = workerRepository.findById(id).orElseThrow();
        workerDelete.setPost_list(null);
        workerDelete.setEmployee_list(null);
        workerRepository.delete(workerDelete);
        return "redirect:/worker/";
    }

    @GetMapping("/worker-view/{id}/edit")
    public String editemployeeview(
            Worker worker,
            Phone phone,
            @PathVariable(value = "id") Long id,
            Model model)
    {
        if (!workerRepository.existsById(id))
        {
            return "redirect:/worker/";
        }

        Worker work = workerRepository.findById(id).orElseThrow();
        worker.setLogin(work.getLogin());
        worker.setPassword("");

        Phone ph = phoneRepository.findById(work.getPhone_list().getId()).orElseThrow();
        phone.setHomePhone(ph.getHomePhone());
        phone.setAdditionalPhone(ph.getAdditionalPhone());
        phone.setMainPhone(ph.getMainPhone());
        idPhone = ph.getId();

        Employee employeers = employeeRepository.findById(work.getEmployee_list().getId()).orElseThrow();
        Post post = postRepository.findById(work.getPost_list().getId()).orElseThrow();

        Iterable<Employee> emp = employeeRepository.findAll();
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("allEmployee",emp);
        model.addAttribute("allPost",posts);
        model.addAttribute("employeeselected", employeers.getPassport().getId());
        model.addAttribute("postselected", post.getName());


        return "worker/edit-worker";
    }

    @PostMapping("/worker-view/{id}/edit")
    public String editemployeeview(
            @PathVariable(value = "id") Long id,
            @RequestParam Long idPost,
            @RequestParam Long idEmployee,
            @Valid Worker worker,
            BindingResult bindingResult,
            @Valid Phone phone,
            BindingResult bindingResultPhone,
            Model model
           )
    {
        boolean errorsB = true;

        String phM = phone.getMainPhone().replaceAll("[^\\d]", "");
        String phH = phone.getHomePhone().replaceAll("[^\\d]", "");
        String phAddition = phone.getAdditionalPhone().replaceAll("[^\\d]", "");

        if (bindingResult.hasErrors() || bindingResultPhone.hasErrors()){
            errorsB = false;
        }

        if ((!phM.equals("") && phM.length() < 11) || (!phH.equals("") && phH.length() < 11) || (!phAddition.equals("") && phAddition.length() < 11)){
            ObjectError error = new ObjectError("mainPhone","Телефон должен содержать 11 цифр");
            bindingResult.addError(error);
            errorsB = false;
        }

        Phone phones_list = phoneRepository.findByMainPhone(phM);//найденный телефон

        if(phones_list != null){
            Worker workSearch = workerRepository.findByPhonelistAndEmployeelist(phones_list.getId(),idEmployee);
            if (workSearch == null){//не найден
                ObjectError error = new ObjectError("additionalPhone","Такой телефон уже есть в базе");
                bindingResult.addError(error);
                errorsB = false;
            }
        }

        if (!errorsB){

            Employee employeers = employeeRepository.findById(idEmployee).orElseThrow();
            Post post = postRepository.findById(idPost).orElseThrow();

            Iterable<Employee> emp = employeeRepository.findAll();
            Iterable<Post> posts = postRepository.findAll();
            model.addAttribute("allEmployee",emp);
            model.addAttribute("allPost",posts);
            model.addAttribute("employeeselected", employeers.getPassport().getId());
            model.addAttribute("postselected", post.getName());
            return "worker/edit-worker";
        }

        phone.setId(idPhone);

        worker.setEmployee_list(employeeRepository.findById(idEmployee).orElseThrow());
        worker.setPost_list(postRepository.findById(idPost).orElseThrow());
        worker.setActive(true);
        worker.setRoles(Collections.singleton(Role.USER));
        if (worker.getPassword() == null) {
            worker.setPassword(workerRepository.findById(id).orElseThrow().getPassword());
        }else {
            worker.setPassword(passwordEncoder.encode(worker.getPassword()));
        }
        worker.setPhone_list(phone);
        phoneRepository.save(phone);
        workerRepository.save(worker);
        return "redirect:/worker/";
    }

    @GetMapping("/worker-information/{id}")
    public String viewinformationworker(@PathVariable(value = "id") Long id, Model model) {
        Optional<Worker> workerOptional = workerRepository.findById(id);
        Worker work = workerOptional.orElseThrow();
        if (work.getPhone_list().getHomePhone() == null || work.getPhone_list().getHomePhone().equals("") ){
            work.getPhone_list().setHomePhone("Нет данных");
        }
        if (work.getEmployee_list().getPlaceResidence() == null ||work.getEmployee_list().getPlaceResidence().equals("") ){
            work.getEmployee_list().setPlaceResidence("Нет данных");
        }
        if (work.getPhone_list().getAdditionalPhone() == null || work.getPhone_list().getAdditionalPhone().equals("")){
            work.getPhone_list().setAdditionalPhone("Нет данных");
        }
        ArrayList<Worker> res = new ArrayList<>();
        workerOptional.ifPresent(res::add);
        model.addAttribute("oneworker",res);
        return "worker/information-worker";
    }

}
