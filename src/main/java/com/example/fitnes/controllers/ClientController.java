package com.example.fitnes.controllers;

import com.example.fitnes.models.*;
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
        return "redirect:/client/";
    }

    @GetMapping("/add")
    public String addclientview(Client client, Passport passport,Phone phone, Model model) {
        return "client/client-add";
    }

    @PostMapping("/add")
    public String addclient(
            @Valid Client client,
            BindingResult bindingResult,
            @Valid Passport passport,
            BindingResult bindingResultPassport,
            @Valid Phone phone,
            BindingResult bindingResultPhone,
            Model model) {

        boolean errorsB = true;

        if (bindingResult.hasErrors() || bindingResultPassport.hasErrors() || bindingResultPhone.hasErrors()){
            errorsB = false;
        }

        String phM = phone.getMainPhone().replaceAll("[^\\d]", "");
        String phH = phone.getHomePhone().replaceAll("[^\\d]", "");
        String phAddition = phone.getAdditionalPhone().replaceAll("[^\\d]", "");

        Passport passSeries = passportRepository.findBySeries(passport.getSeries());
        Passport passNumber = passportRepository.findByNumber(passport.getNumber());
        Client cl = clientRepository.findBySurnameAndNameAndPatronymic(client.getSurname(),client.getName(),client.getPatronymic());
        Long idPas = passport.getId();

        if ((!phM.equals("") && phM.length() < 11) || (!phH.equals("") && phH.length() < 11) || (!phAddition.equals("") && phAddition.length() < 11)){
            ObjectError error = new ObjectError("mainPhone","Телефон должен содержать 11 цифр");
            bindingResult.addError(error);
            errorsB = false;
        }

        Phone phones_list = phoneRepository.findByMainPhone(phM);//найденный телефон

        if(phones_list != null){
           // Client clientSearch = clientRepository.findByPhonelist(phones_list.getId());//переделать
           // if (clientSearch == null){//не найден
                ObjectError error = new ObjectError("additionalPhone","Такой телефон уже есть в базе");
                bindingResult.addError(error);
                errorsB = false;
            //}
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

        if (cl!= null && !cl.getId().equals(client.getId())){
            ObjectError error = new ObjectError("surname","Пользователь с таким ФИО уже существует");
            bindingResult.addError(error);
            errorsB = false;
        }

        if (!errorsB){
            return "client/client-add";
        }

        passportRepository.save(passport);
        client.setPassport(passport);
        phoneRepository.save(phone);
        client.setPhone(phone);
        clientRepository.save(client);

        return "redirect:/client/";
    }

    @GetMapping("/client-view/{id}/edit")
    public String editemployeeview(
            Client client,
            Passport passport,
            Phone phone,
            @PathVariable(value = "id") Long id,
            Model model) {

        Client clientes = clientRepository.findById(id).orElseThrow();
        client.setSurname(clientes.getSurname());
        client.setName(clientes.getName());
        client.setPatronymic(clientes.getPatronymic());
        client.setWeight(clientes.getWeight());
        client.setGender(clientes.getGender());

        Phone ph = phoneRepository.findById(clientes.getPhone().getId()).orElseThrow();
        phone.setHomePhone(ph.getHomePhone());
        phone.setAdditionalPhone(ph.getAdditionalPhone());
        phone.setMainPhone(ph.getMainPhone());

        Passport pass = passportRepository.findById(clientes.getPassport().getId()).orElseThrow();
        passport.setNumber(pass.getNumber());
        passport.setSeries(pass.getSeries());

        model.addAttribute("genderselected", client.getGender());

        return "/client/edit-client";

    }
}
