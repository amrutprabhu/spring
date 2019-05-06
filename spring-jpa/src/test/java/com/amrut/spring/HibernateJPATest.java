package com.amrut.spring;

import com.amrut.spring.spring.MainApplication;
import com.amrut.spring.spring.model.Address;
import com.amrut.spring.spring.model.Customer;
import com.amrut.spring.spring.model.Telephone;
import com.amrut.spring.spring.repository.AddressRepository;
import com.amrut.spring.spring.repository.CustomerRepository;
import com.amrut.spring.spring.repository.TelephoneRepository;
import org.h2.tools.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = MainApplication.class)
@RunWith(SpringRunner.class)
public class HibernateJPATest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TelephoneRepository telephoneRepository;

    @Test
    @Transactional  // this is required otherwise the address will be detached from the current session.
    public void testNewEntryWithMultipleSaves() {

        Telephone telephone = Telephone.builder()
                .number(99238)
                .build();
        telephoneRepository.save(telephone);


        Address address = Address.builder().zipCode(1234).build();
        addressRepository.save(address);

        ArrayList<Telephone> telephones = new ArrayList<>();
        telephones.add(telephone);
        Customer customer = Customer.builder()
                .name("User1")
                .address(address)
                .telephone(telephones)
                .build();

        customerRepository.save(customer);

        customer.setName("User2");

        customerRepository.save(customer);

        Optional<Customer> custOpt = customerRepository.findById(customer.getCust_id());

        custOpt.ifPresent(resultCustomer->{
            assertEquals(resultCustomer.getAddress().getZipCode(),address.getZipCode());
            assertEquals(resultCustomer.getTelephone().size(),1);
            assertEquals(resultCustomer.getTelephone().get(0).getNumber(),telephone.getNumber());
        });
    }


    @Test
    public void testAddNewEntityWithSingleSave() {

        addressRepository.findAll().forEach(address1 -> {
            System.out.println(address1.getAddress_id());
        });

        Address address = Address.builder()
                .zipCode(1234567)
                .build();

        Telephone mobile = Telephone.builder()
                .number(123212)
                .type("mobile")
                .build();

        Customer customer = Customer.builder()
                .name("user1")
                .address(address)
                .telephone(Arrays.asList(mobile))
                .build();

        customerRepository.save(customer);

        addressRepository.findAll().forEach(address1 -> {
            System.out.println(address1.getAddress_id() + " - " + address1.getZipCode());
            assertEquals(address1.getZipCode(), address.getZipCode());
        });

        customerRepository.findAll().forEach(customer1 -> {
            assertEquals(customer1.getName(), customer.getName());
            System.out.println("cust" + customer1.getCust_id());
        });


    }
}
