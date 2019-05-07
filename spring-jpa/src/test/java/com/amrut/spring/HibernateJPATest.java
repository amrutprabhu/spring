package com.amrut.spring;

import com.amrut.spring.spring.MainApplication;
import com.amrut.spring.spring.model.Address;
import com.amrut.spring.spring.model.Course;
import com.amrut.spring.spring.model.Customer;
import com.amrut.spring.spring.model.Telephone;
import com.amrut.spring.spring.repository.AddressRepository;
import com.amrut.spring.spring.repository.CourseRepository;
import com.amrut.spring.spring.repository.CustomerRepository;
import com.amrut.spring.spring.repository.TelephoneRepository;
import org.h2.tools.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = MainApplication.class)
@RunWith(SpringRunner.class)
public class HibernateJPATest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TelephoneRepository telephoneRepository;

    @Autowired
    private CourseRepository courseRepository;


    @Test
    @Transactional  // this is required otherwise the address will be detached from the current session.
    public void testNewEntryWithMultipleSaves() {

        int initialCustomerCount = getRepositoryCount(customerRepository);
        int initialTelephoneCount = getRepositoryCount(telephoneRepository);
        int initialAddressCount = getRepositoryCount(addressRepository);
        int initialCourseCount = getRepositoryCount(courseRepository);

        Telephone telephone = saveTelephone();

        Address address = saveAddress();

        Set<String> courseValues = new HashSet<>(Arrays.asList("java", "html"));
        List<Course> courses = saveCourses(courseValues);

        ArrayList<Telephone> telephones = new ArrayList<>();
        telephones.add(telephone);

        Customer customer = Customer.builder()
                .name("User1")
                .address(address)
                .telephone(telephones)
                .courses(courses)
                .build();

        customerRepository.save(customer);

        customer.setName("User2");

        customerRepository.save(customer);


        int currentCustomerCount = getRepositoryCount(customerRepository);
        int currentTelephoneCount = getRepositoryCount(telephoneRepository);
        int currentAddressCount = getRepositoryCount(addressRepository);
        int currentCourseCount = getRepositoryCount(courseRepository);

        assertEquals(currentCustomerCount - initialCustomerCount, 1);
        assertEquals(currentAddressCount - initialAddressCount, 1);
        assertEquals(currentTelephoneCount - initialTelephoneCount, 1);
        assertEquals(currentCourseCount - initialCourseCount, 2);


        System.out.println(initialAddressCount);
        Optional<Customer> custOpt = customerRepository.findById(customer.getCust_id());

        custOpt.ifPresent(resultCustomer -> {

            assertEquals(resultCustomer.getName(), "User2");
            assertEquals(resultCustomer.getAddress().getZipCode(), address.getZipCode());
            assertEquals(resultCustomer.getTelephone().size(), 1);
            assertEquals(resultCustomer.getTelephone().get(0).getNumber(), telephone.getNumber());
            assertEquals(resultCustomer.getCourses().size(), 2);
            boolean present = resultCustomer.getCourses().stream()
                    .map(course -> course.getName())
                    .anyMatch(name -> courseValues.contains(name));
            assertTrue(present);
        });
    }

    private int getRepositoryCount(CrudRepository repository) {
        int count = 0;
        for (Object object : repository.findAll()) {
            count++;
        }
        return count;
    }

    private List<Course> saveCourses(Set<String> courseValues) {

        List<Course> courses = courseValues.stream()
                .map(value -> Course.builder()
                        .name(value)
                        .build())
                .collect(Collectors.toList());

        courses.forEach(course -> courseRepository.save(course));
        return courses;
    }

    private Telephone saveTelephone() {
        Telephone telephone = Telephone.builder()
                .number(99238)
                .build();
        telephoneRepository.save(telephone);
        return telephone;
    }

    private Address saveAddress() {
        Address address = Address.builder().zipCode(1234).build();
        addressRepository.save(address);
        return address;
    }


    @Test
    public void testAddNewEntityWithSingleSave() {

        int initialCustomerCount = getRepositoryCount(customerRepository);
        int initialTelephoneCount = getRepositoryCount(telephoneRepository);
        int initialAddressCount = getRepositoryCount(addressRepository);
        int initialCourseCount = getRepositoryCount(courseRepository);

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

        //first save
        customerRepository.save(customer);

        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder()
                .name("java")
                .build());
        customer.setCourses(courses);

        //second save
        customerRepository.save(customer);


        int currentCustomerCount = getRepositoryCount(customerRepository);
        int currentTelephoneCount = getRepositoryCount(telephoneRepository);
        int currentAddressCount = getRepositoryCount(addressRepository);
        int currentCourseCount = getRepositoryCount(courseRepository);

        assertEquals(currentCustomerCount - initialCustomerCount, 1);
        assertEquals(currentAddressCount - initialAddressCount, 1);
        assertEquals(currentTelephoneCount - initialTelephoneCount, 1);
        assertEquals(currentCourseCount - initialCourseCount, 1);


    }
}
