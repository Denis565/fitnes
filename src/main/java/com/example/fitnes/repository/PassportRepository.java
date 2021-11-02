package com.example.fitnes.repository;

import com.example.fitnes.models.Passport;
import org.springframework.data.repository.CrudRepository;

public interface PassportRepository extends CrudRepository<Passport,Long> {
    Passport findBySeries(String series);
    Passport findByNumber(String number);
    Passport findBySeriesAndNumber (String series,String number);
}

