package com.amrut.spring.spring.repository;

import com.amrut.spring.spring.model.Telephone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelephoneRepository extends CrudRepository<Telephone,Integer> {
}
