package com.example.fitnes.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

//График проведения тренеровок
@Entity
@Table(name = "TRAININGSCHEDULE")
public class TrainingSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Код

    @NotNull(message = "Поле не должно быть пустым")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startTime;

    @NotNull(message = "Поле не должно быть пустым")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime endTime;

    @NotNull(message = "Данное поле не должно быть пустым")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Future(message = "Дата должна быть в будущем")
    private LocalDate date;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    private SubscriptionSale subscriptionSale_list;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    private Worker work_list;

    public TrainingSchedule(LocalTime startTime, LocalTime endTime, LocalDate date, SubscriptionSale subscriptionSale_list, Worker work_list) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.subscriptionSale_list = subscriptionSale_list;
        this.work_list = work_list;
    }

    public TrainingSchedule(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public SubscriptionSale getSubscriptionSale_list() {
        return subscriptionSale_list;
    }

    public void setSubscriptionSale_list(SubscriptionSale subscriptionSale_list) {
        this.subscriptionSale_list = subscriptionSale_list;
    }

    public Worker getWork_list() {
        return work_list;
    }

    public void setWork_list(Worker work_list) {
        this.work_list = work_list;
    }
}
