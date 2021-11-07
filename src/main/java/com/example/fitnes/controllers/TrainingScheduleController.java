package com.example.fitnes.controllers;

import com.example.fitnes.models.*;
import com.example.fitnes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

@Controller
@RequestMapping("/trainingschedule")
public class TrainingScheduleController {

    @Autowired
    private TrainingScheduleRepository trainingScheduleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private SubscriptionSaleRepository subscriptionSaleRepository;

    @GetMapping("/")
    public String subscriptionsaleview(Model model){
        ArrayList<TrainingSchedule> trainingSchedules = trainingScheduleRepository.findByAllDate();
        model.addAttribute("alltrainingSchedules", trainingSchedules);
        return  "trainingschedule/trainingschedule-view";
    }

    @GetMapping("/add")
    public String trainingscheduleaddview(TrainingSchedule trainingSchedule,Model model){
        Iterable<SubscriptionSale> subscriptionSaleIterable = subscriptionSaleRepository.findBySubscriptionSalesList();
        Iterable<Worker> workerIterable = workerRepository.findByPostCoach();
        model.addAttribute("allClient",subscriptionSaleIterable);
        model.addAttribute("allWorker",workerIterable);
        return "trainingschedule/trainingschedule-add";
    }

    @PostMapping("/add")
    public String trainingscheduleadd(
            @RequestParam Long idSubscriptionSale,
            @RequestParam Long idWorker,
            @Valid TrainingSchedule trainingSchedule,
            BindingResult bindingResult,
            Model model){

        boolean erorsB = true;
        if (bindingResult.hasErrors()) {
            init(model, idSubscriptionSale, idWorker);
            return "trainingschedule/trainingschedule-add";
        }

        LocalTime timeStartSelected = trainingSchedule.getStartTime();
        LocalTime timeEndSelected = trainingSchedule.getEndTime();

        if (timeStartSelected != null && timeEndSelected != null) {
            if (trainingScheduleRepository.findByTimeRangesAreOverlaping(timeStartSelected.toString(), timeEndSelected.toString(),trainingSchedule.getDate().toString(),idWorker).size() != 0) {
                ObjectError error = new ObjectError("startTime", "В этот промежуток времени уже есть тренеровка у другого человека.");
                bindingResult.addError(error);
                erorsB = false;
            }
        }

        LocalTime localmax = LocalTime.parse("22:00");
        LocalTime localmin = LocalTime.parse("10:00");

        assert timeStartSelected != null;
        assert timeEndSelected != null;
        if ((localmax.isBefore(timeStartSelected) || localmin.isAfter(timeStartSelected)) || (localmax.isBefore(timeEndSelected) || localmin.isAfter(timeEndSelected))){
            ObjectError error = new ObjectError("startTime", "Время для тренеровок с 10:00 по 22:00");
            bindingResult.addError(error);
            erorsB = false;
        }

        if (timeEndSelected.isBefore(timeStartSelected)){
            ObjectError error = new ObjectError("startTime", "Время начала должно быть меньше времени окончания тренеровки.");
            bindingResult.addError(error);
            erorsB = false;
        }

        SubscriptionSale subscriptionSale = subscriptionSaleRepository.findById(idSubscriptionSale).orElseThrow();

        if (trainingSchedule.getDate().isAfter(subscriptionSale.getEndDate())){
            String formatDate = subscriptionSale.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            ObjectError error = new ObjectError("startTime", "Вы не можете создать запись на этот день, так как ваш абонимент действует до "+ formatDate);
            bindingResult.addError(error);
            erorsB = false;
        }

        if (erorsB && timeStartSelected.plusHours(1).isAfter(timeEndSelected)){
            ObjectError error = new ObjectError("startTime", "Минимальное время индивидуальной тренеровки 1 час.");
            bindingResult.addError(error);
            erorsB = false;
        }

        if (!erorsB){
            init(model, idSubscriptionSale, idWorker);
            return "trainingschedule/trainingschedule-add";
        }

        trainingSchedule.setSubscriptionSale_list(subscriptionSale);
        trainingSchedule.setWork_list(workerRepository.findById(idWorker).orElseThrow());

        trainingScheduleRepository.save(trainingSchedule);
        return "redirect:/trainingschedule/";
    }

    private void init(Model model,Long idSubscriptionSaleSelected,Long idWorkerSelect){
        Iterable<SubscriptionSale> subscriptionSaleIterable = subscriptionSaleRepository.findBySubscriptionSalesList();
        Iterable<Worker> workerIterable = workerRepository.findByPostCoach();
        model.addAttribute("allClient",subscriptionSaleIterable);
        model.addAttribute("allWorker",workerIterable);
        model.addAttribute("idSubscriptionSaleSelected",idSubscriptionSaleSelected);
        model.addAttribute("idWorkerSelect",idWorkerSelect);
    }

    @PostMapping("trainingschedule-view/{id}/del")
    public String delsubscription(
            @PathVariable(value = "id") Long id,
            Model model)
    {
        TrainingSchedule trainingSchedule = trainingScheduleRepository.findById(id).orElseThrow();
        trainingSchedule.setWork_list(null);
        trainingSchedule.setSubscriptionSale_list(null);
        trainingScheduleRepository.delete(trainingSchedule);
        return "redirect:/trainingschedule/";
    }

    @GetMapping("/trainingschedule-view/{id}/edit")
    public String edittrainingscheduleview(
            TrainingSchedule trainingSchedule,
            @PathVariable(value = "id") Long id,
            Model model) {

        if (!trainingScheduleRepository.existsById(id)) {
            return "redirect:/trainingschedule/";
        }

        TrainingSchedule trainingScheduleSelected = trainingScheduleRepository.findById(id).orElseThrow();
        trainingSchedule.setDate(trainingScheduleSelected.getDate());
        trainingSchedule.setStartTime(trainingScheduleSelected.getStartTime());
        trainingSchedule.setEndTime(trainingScheduleSelected.getEndTime());
        trainingSchedule.setWork_list(trainingScheduleSelected.getWork_list());
        trainingSchedule.setSubscriptionSale_list(trainingScheduleSelected.getSubscriptionSale_list());

        init(model,trainingSchedule.getSubscriptionSale_list().getId(),trainingSchedule.getWork_list().getId());

        return "trainingschedule/edit-trainingschedule";
    }

    @PostMapping("/trainingschedule-view/{id}/edit")
    public String edittrainingschedule(
            @Valid TrainingSchedule trainingSchedule,
            BindingResult bindingResult,
            @RequestParam Long idSubscriptionSale,
            @RequestParam Long idWorker,
            @PathVariable(value = "id") Long id,
            Model model) {

        boolean erorsB = true;

        if (bindingResult.hasErrors()) {
            init(model, idSubscriptionSale, idWorker);
            return "trainingschedule/trainingschedule-add";
        }

        LocalTime timeStartSelected = trainingSchedule.getStartTime();
        LocalTime timeEndSelected = trainingSchedule.getEndTime();

        if (timeStartSelected != null && timeEndSelected != null) {
            if (trainingScheduleRepository.findByTimeRangesAreOverlapingUpdate(timeStartSelected.toString(), timeEndSelected.toString(),trainingSchedule.getDate().toString(),idWorker,trainingSchedule.getId()).size() != 0) {
                ObjectError error = new ObjectError("startTime", "В этот промежуток времени уже есть тренеровка у другого человека.");
                bindingResult.addError(error);
                erorsB = false;
            }
        }

        LocalTime localmax = LocalTime.parse("22:00");
        LocalTime localmin = LocalTime.parse("10:00");

        assert timeStartSelected != null;
        assert timeEndSelected != null;
        if ((localmax.isBefore(timeStartSelected) || localmin.isAfter(timeStartSelected)) || (localmax.isBefore(timeEndSelected) || localmin.isAfter(timeEndSelected))){
            ObjectError error = new ObjectError("startTime", "Время для тренеровок с 10:00 по 22:00");
            bindingResult.addError(error);
            erorsB = false;
        }

        if (timeEndSelected.isBefore(timeStartSelected)){
            ObjectError error = new ObjectError("startTime", "Время начала должно быть меньше времени окончания тренеровки.");
            bindingResult.addError(error);
            erorsB = false;
        }

        SubscriptionSale subscriptionSale = subscriptionSaleRepository.findById(idSubscriptionSale).orElseThrow();

        if (trainingSchedule.getDate().isAfter(subscriptionSale.getEndDate())){
            String formatDate = subscriptionSale.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            ObjectError error = new ObjectError("startTime", "Вы не можете создать запись на этот день, так как ваш абонимент действует до "+ formatDate);
            bindingResult.addError(error);
            erorsB = false;
        }

        if (erorsB && timeStartSelected.plusHours(1).isAfter(timeEndSelected)){
            ObjectError error = new ObjectError("startTime", "Минимальное время индивидуальной тренеровки 1 час.");
            bindingResult.addError(error);
            erorsB = false;
        }

        if (!erorsB){
            init(model, idSubscriptionSale, idWorker);
            return "trainingschedule/trainingschedule-add";
        }

        trainingSchedule.setSubscriptionSale_list(subscriptionSale);
        trainingSchedule.setWork_list(workerRepository.findById(idWorker).orElseThrow());
        trainingScheduleRepository.save(trainingSchedule);
        return "redirect:/trainingschedule/";
    }

    private boolean checkErrors(){
        boolean errorsB = true;

        return errorsB;
    }
    /*private static boolean dateRangesAreOverlaping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return (((end1 == null) || (start2 == null) || end1.isAfter(start2)) &&
                ((start1 == null) || (end2 == null) || start1.isBefore(end2)));
    }

    private static boolean timerange(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2){
        return (start1.isBefore(end2) || start1.equals(end2)) && (start2.isBefore(end1) || start2.equals(end1));
    }*/
}
