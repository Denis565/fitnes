package com.example.fitnes.controllers;

import com.example.fitnes.models.SubscriptionSale;
import com.example.fitnes.models.TrainingSchedule;
import com.example.fitnes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping("/trainingschedule")
public class TrainingScheduleController {

    @Autowired
    private TrainingScheduleRepository trainingScheduleRepository;

    @Autowired
    private SubscriptionSaleRepository subscriptionSaleRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @GetMapping("/")
    public String subscriptionsaleview(Model model){
        ArrayList<TrainingSchedule> trainingSchedules = trainingScheduleRepository.findByAllDate(LocalDate.now().toString());
        model.addAttribute("alltrainingSchedules", trainingSchedules);
        return  "trainingschedule/trainingschedule-view";
    }
}
