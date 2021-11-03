package com.example.fitnes.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;

//Телефон
@Entity
@Table(name = "PHONE")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 11,max=11,message = "В основном телефоне длжно быть 11 цифр")
    private String mainPhone;

    @Size(min = 0,max=11,message = "Миниму 2 символа, максимум 11 символов")
    private String additionalPhone;

    @Size(min = 0,max=11,message = "Миниму 2 символа, максимум 11 символов")
    private String homePhone;

    @OneToOne(optional = true,mappedBy = "phone")
    private Client client;

    @OneToMany(mappedBy = "phone_list",fetch = FetchType.EAGER)
    private Collection<Worker> workers;

    public Phone(String mainPhone, String additionalPhone, String homePhone) {
        this.mainPhone = mainPhone;
        this.additionalPhone = additionalPhone;
        this.homePhone = homePhone;
    }

    public Phone(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMainPhone() {
        return mainPhone;
    }

    public void setMainPhone(String mainPhone) {
        this.mainPhone = mainPhone;
    }

    public String getAdditionalPhone() {
        return additionalPhone;
    }

    public void setAdditionalPhone(String additionalPhone) {
        this.additionalPhone = additionalPhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Collection<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(Collection<Worker> workers) {
        this.workers = workers;
    }
}
