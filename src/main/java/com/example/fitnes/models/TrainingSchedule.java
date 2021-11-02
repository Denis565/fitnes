package com.example.fitnes.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

//График проведения тренеровок
@Entity
@Table(name = "TRAININGSCHEDULE")
public class TrainingSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Код

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 5,max=5,message = "Время должно быть длиной в 5 символов")
    private String startTime;

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 5,max=5,message = "Время должно быть длиной в 5 символов")
    private String endTime;

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 10,max=10,message = "Дата должна быть длиной в 10 символов")
    private String date;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    private SubscriptionSale subscriptionSale_list;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    private Worker work_list;

    public TrainingSchedule(String startTime, String endTime, String date, SubscriptionSale subscriptionSale_list, Worker work_list) {
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
