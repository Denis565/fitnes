package com.example.fitnes.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;

//Должность
@Entity
@Table(name = "POST")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Данное поле не должно быть пустым")
    private String name;

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Min(value = 4000 ,message = "Минимальная зарплата 10000 рублей" )
    @Max(value = 1000000 ,message = "Максимальная зарплата 1000000 рублей" )
    private int salary;

    @OneToMany(mappedBy = "post_list",fetch = FetchType.LAZY)
    private Collection<Worker> workers;

    public Post(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    public Post(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(Collection<Worker> workers) {
        this.workers = workers;
    }
}
