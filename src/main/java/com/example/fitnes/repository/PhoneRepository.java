package com.example.fitnes.repository;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.Phone;
import org.springframework.data.repository.CrudRepository;

public interface PhoneRepository extends CrudRepository<Phone,Long> {
    Phone findByMainPhone (String mainPhone);
}
