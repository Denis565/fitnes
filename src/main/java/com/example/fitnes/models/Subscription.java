package com.example.fitnes.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.List;

//Абонимент
@Entity
@Table(name = "SUBSCRIPTION")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Код

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 2,max=100,message = "Миниму 2 символа, максимум 100 символов")
    private String name; //Наименование абонимента

    @NotNull(message = "Данное поле не должно быть пустым")
    @Max(value = 12,message = "Максимум 12 месяцев")
    @Min(value = 1, message = "Минимум 1 месяцев" )
    private Integer timePeriod;

    @NotNull(message = "Данное поле не должно быть пустым")
    @Max(value = 50000,message = "Максимум 50000 рублей")
    @Min(value = 500, message = "Минимум 500 рублей" )
    private Integer price;

    @NotNull(message = "Данное поле не должно быть пустым")
    @Max(value = 50000,message = "Максимум 50000")
    @Min(value = 1, message = "Минимум 1" )
    private Integer subscriptionNumber;

    @ManyToMany
    @JoinTable(name = "service_subscription",
            joinColumns = @JoinColumn(name = "subscription_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<Service> service;

    @OneToMany(mappedBy = "subscription_list",fetch = FetchType.EAGER)
    private Collection<SubscriptionSale> subscriptionSales;

    public Subscription(String name, Integer timePeriod, Integer price, Integer subscriptionNumber) {
        this.name = name;
        this.timePeriod = timePeriod;
        this.price = price;
        this.subscriptionNumber = subscriptionNumber;
    }

    public Subscription(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(Integer timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSubscriptionNumber() {
        return subscriptionNumber;
    }

    public void setSubscriptionNumber(Integer subscriptionNumber) {
        this.subscriptionNumber = subscriptionNumber;
    }

    public List<Service> getService_list() {
        return service;
    }

    public void setService_list(List<Service> service) {
        this.service = service;
    }

    public Collection<SubscriptionSale> getSubscriptionSales() {
        return subscriptionSales;
    }

    public void setSubscriptionSales(Collection<SubscriptionSale> subscriptionSales) {
        this.subscriptionSales = subscriptionSales;
    }
}
