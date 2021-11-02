package com.example.fitnes.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

//Паспорт
@Entity
@Table(name = "PASSPORT")
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 4,max=4,message = "Серия паспорта должна состоять из 4 цифр")
    private String series;

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 6,max=6,message = "Номер паспорта должна состоять из 6 цифр")
    private String number;

    @OneToOne(optional = true,mappedBy = "passport")
    private Employee employee;
    @OneToOne(optional = true,mappedBy = "passport")
    private Client client;

    public Passport(String series, String number) {
        this.series = series;
        this.number = number;
    }

    public Passport(){}

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getSeries() {return series;}

    public void setSeries(String series) {this.series = series;}

    public String getNumber() {return number;}

    public void setNumber(String number) {this.number = number;}

    public Employee getEmployee(){return employee;}
    public void  setEmployee(Employee employee){this.employee = employee;}

    public Client getClient(){return client;}
    public void  setClient(Client client){this.client = client;}
}
