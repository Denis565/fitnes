package com.example.fitnes.models;

import net.bytebuddy.implementation.bind.annotation.Empty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Table(name = "EMPLOYEE")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 2,max=50,message = "Минимальное число символов 2, а максимально 50 символов")
    private String name,surname;

    private String patronymic;

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 10,max=10,message = "Дата рождения должна быть длиной в 10 символов")
    private String dateBirth;

   // @NotEmpty(message = "Данное поле не должно быть пустым")
    private String placeResidence;

    @NotEmpty(message = "Данное поле не должно быть пустым")
    private String gender;//Пол

    @OneToOne(optional = true,cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id")
    private Passport passport;

    @OneToMany(mappedBy = "employee_list",fetch = FetchType.LAZY)
    private Collection<Worker> workers;

    public Employee(String name, String surname, String patronymic, String dateBirth, String placeResidence, String gender, Passport passport) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.dateBirth = dateBirth;
        this.placeResidence = placeResidence;
        this.gender = gender;
        this.passport = passport;
    }

    public Employee(){}

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

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getPlaceResidence() {
        return placeResidence;
    }

    public void setPlaceResidence(String placeResidence) {
        this.placeResidence = placeResidence;
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

    public Collection<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(Collection<Worker> workers) {
        this.workers = workers;
    }
}
