package com.example.fitnes.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;

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

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 2,max=100,message = "Миниму 2 символа, максимум 100 символов")
    private String timePeriod;

    @NotNull(message = "Данное поле не должно быть пустым")
    @Max(value = 50000,message = "Максимум 50000 рублей")
    @Min(value = 500, message = "Минимум 500 рублей" )
    private int price;

    @NotNull(message = "Данное поле не должно быть пустым")
    @Max(value = 50000,message = "Максимум 50000")
    @Min(value = 1, message = "Минимум 1" )
    private int subscriptionNumber;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    private Service service_list;

    @OneToMany(mappedBy = "subscription_list",fetch = FetchType.EAGER)
    private Collection<SubscriptionSale> subscriptionSales;

    public Subscription(String name, String timePeriod, int price, int subscriptionNumber, Service service_list) {
        this.name = name;
        this.timePeriod = timePeriod;
        this.price = price;
        this.subscriptionNumber = subscriptionNumber;
        this.service_list = service_list;
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

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSubscriptionNumber() {
        return subscriptionNumber;
    }

    public void setSubscriptionNumber(int subscriptionNumber) {
        this.subscriptionNumber = subscriptionNumber;
    }

    public Service getService_list() {
        return service_list;
    }

    public void setService_list(Service service_list) {
        this.service_list = service_list;
    }


}
