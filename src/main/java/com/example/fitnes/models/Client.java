package com.example.fitnes.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Collection;

//Клиент
@Entity
@Table(name = "CLIENT")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Код

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 2,max=50,message = "Минимальное число символов 2, а максимально 50 символов")
    private String name,surname;

    private String patronymic;

    @NotNull(message = "Поле для вееса не должно быть пустым")
    @DecimalMax(value = "200",message = "Максимум 200 кг")
    @DecimalMin(value = "30", message = "Минимум 30 кг" )
    private BigDecimal weight;//Вес

    @NotEmpty(message = "Данное поле не должно быть пустым")
    private String gender;//Пол

    @OneToOne(optional = true,cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id")
    private Passport passport;

    @OneToOne(optional = true,cascade = CascadeType.ALL)
    @JoinColumn(name = "phone_id")
    private Phone phone;

    @OneToMany(mappedBy = "client_list",fetch = FetchType.LAZY)
    private Collection<SubscriptionSale> subscriptionSales;

    public Client(String name, String surname, String patronymic, BigDecimal weight, String gender, Passport passport, Phone phone) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.weight = weight;
        this.gender = gender;
        this.passport = passport;
        this.phone = phone;
    }

    public Client(){}

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Collection<SubscriptionSale> getSubscriptionSales() {
        return subscriptionSales;
    }

    public void setSubscriptionSales(Collection<SubscriptionSale> subscriptionSales) {
        this.subscriptionSales = subscriptionSales;
    }
}
