package com.example.fitnes.controllers;

import com.example.fitnes.models.*;
import com.example.fitnes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Entity;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@Controller
@RequestMapping("/trainingschedule")
public class TrainingScheduleController {

    @Autowired
    private TrainingScheduleRepository trainingScheduleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @GetMapping("/")
    public String subscriptionsaleview(Model model){
        ArrayList<TrainingSchedule> trainingSchedules = trainingScheduleRepository.findByAllDate();
        model.addAttribute("alltrainingSchedules", trainingSchedules);
        return  "trainingschedule/trainingschedule-view";
    }

    @GetMapping("/add")
    public String trainingscheduleaddview(TrainingSchedule trainingSchedule,Model model){
        Iterable<Client> clientIterable = clientRepository.findAll();
        Iterable<Worker> workerIterable = workerRepository.findByPostCoach();
        model.addAttribute("allClient",clientIterable);
        model.addAttribute("allWorker",workerIterable);
        return "trainingschedule/trainingschedule-add";
    }

    @PostMapping("/add")
    public String trainingscheduleadd(
            @RequestParam Long idClient,
            @RequestParam Long idWorker,
            @Valid TrainingSchedule trainingSchedule,
            BindingResult bindingResult,
            Model model){

        boolean erorsB = true;

        /*LocalTime l1 = LocalTime.parse("10:00");
        LocalTime l2 = LocalTime.parse("11:00");
        LocalTime r1 = LocalTime.parse("10:06");
        LocalTime r2 = LocalTime.parse("12:30");
        //true если пересекаються
        //false если не пересекаються
        boolean j = dateRangesAreOverlaping(l1,l2,r1,r2);
        boolean j2 = timerange(l1,l2,r1,r2);*/

        if (bindingResult.hasErrors()) {
            init(model, idClient, idWorker);
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
            ObjectError error = new ObjectError("startTime", "Время начала должно быть меньше времени окончания ьренеровки.");
            bindingResult.addError(error);
            erorsB = false;
        }

        if (!erorsB){
            init(model, idClient, idWorker);
            return "trainingschedule/trainingschedule-add";
        }

        return "redirect:/trainingschedule/";
    }

    private void init(Model model,Long idClientSelected,Long idWorkerSelect){
        Iterable<Client> clientIterable = clientRepository.findAll();
        Iterable<Worker> workerIterable = workerRepository.findByPostCoach();
        model.addAttribute("allClient",clientIterable);
        model.addAttribute("allWorker",workerIterable);
        model.addAttribute("idClientSelected",idClientSelected);
        model.addAttribute("idWorkerSelect",idWorkerSelect);
    }

    //LocalTime start1, 10
    // LocalTime end1, 11
    // LocalTime start2, 10:10
    // LocalTime end2 11:10

    private static boolean dateRangesAreOverlaping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return (((end1 == null) || (start2 == null) || end1.isAfter(start2)) &&
                ((start1 == null) || (end2 == null) || start1.isBefore(end2)));
    }

    private static boolean timerange(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2){
        return (start1.isBefore(end2) || start1.equals(end2)) && (start2.isBefore(end1) || start2.equals(end1));
    }
}
