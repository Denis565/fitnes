package com.example.fitnes.models;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;

//Продажа абонимента
@Entity
@Table(name = "SUBSCRIPTIONSALE")
public class SubscriptionSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Код

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 10,max=10,message = "Дата должна быть длиной в 10 символов")
    @FutureOrPresent(message = "Дата должна быть в будущем")
    private String startDate;

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 10,max=10,message = "Дата должна быть длиной в 10 символов")
    @Future(message = "Дата должна быть в будущем")
    private String endDate;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    private Subscription subscription_list;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    private Client client_list;


    public SubscriptionSale(String startDate, String endDate, Subscription subscription_list, Client client_list) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.subscription_list = subscription_list;
        this.client_list = client_list;
    }

    public SubscriptionSale(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Subscription getSubscription_list() {
        return subscription_list;
    }

    public void setSubscription_list(Subscription subscription_list) {
        this.subscription_list = subscription_list;
    }

    public Client getClient_list() {
        return client_list;
    }

    public void setClient_list(Client client_list) {
        this.client_list = client_list;
    }
}
