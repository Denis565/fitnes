package com.example.fitnes.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Collection;

//Продажа абонимента
@Entity
@Table(name = "SUBSCRIPTIONSALE")
public class SubscriptionSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Код

    @NotNull(message = "Поле не должно быть пустым")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @FutureOrPresent(message = "Дата должна быть в будущем или настоящем")
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Future(message = "Дата должна быть в будущем")
    private LocalDate endDate;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    private Subscription subscription_list;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    private Client client_list;

    public SubscriptionSale(LocalDate startDate, LocalDate endDate, Subscription subscription_list, Client client_list) {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
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
