package com.amrut.spring.spring;

import com.amrut.spring.spring.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping("/")
    public ResponseEntity perform(){

        long count = customerRepository.count();
        return ResponseEntity.ok().body(count);
    }
}
