package com.example.fitnes.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

//Работник
@Entity
@Table(name = "WORKER")
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Код

    @NotEmpty(message = "Данное поле не должно быть пустым")
    @Size(min = 6,max=50,message = "Минимальное число символов 6, а максимально 50 символов")
    private String login;

    //@NotEmpty(message = "Данное поле не должно быть пустым")
   // @Size(min = 6,max=50,message = "Минимальное число символов 2, а максимально 50 символов")
    @Column(length = 255)
    private String password;

    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "worker_role",joinColumns = @JoinColumn(name = "worker_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    private Phone phone_list;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    private Employee employee_list;

    @ManyToOne(optional = true,cascade = CascadeType.ALL)
    private Post post_list;

    @OneToMany(mappedBy = "work_list",fetch = FetchType.LAZY)
    private Collection<TrainingSchedule> trainingSchedules;


    public Worker(String login, String password, Phone phone_list, Employee employee_list, Post post_list) {
        this.login = login;
        this.password = password;
        this.employee_list = employee_list;
        this.phone_list = phone_list;
        this.post_list = post_list;
    }

    public Worker(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Phone getPhone_list() {
        return phone_list;
    }

    public void setPhone_list(Phone phone_list) {
        this.phone_list = phone_list;
    }

    public Employee getEmployee_list() {
        return employee_list;
    }

    public void setEmployee_list(Employee employee_list) {
        this.employee_list = employee_list;
    }

    public Post getPost_list() {
        return post_list;
    }

    public void setPost_list(Post post_list) {
        this.post_list = post_list;
    }

    public Collection<TrainingSchedule> getTrainingSchedules() {
        return trainingSchedules;
    }

    public void setTrainingSchedules(Collection<TrainingSchedule> trainingSchedules) {
        this.trainingSchedules = trainingSchedules;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
