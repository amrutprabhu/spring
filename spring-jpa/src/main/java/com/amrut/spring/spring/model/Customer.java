package com.amrut.spring.spring.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue
    private Integer cust_id;

    private String name;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    private Address address;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Telephone> telephone;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "customer_course",
                joinColumns = @JoinColumn(name = "customerId"),
                inverseJoinColumns = @JoinColumn(name = "courseId"))
    private List<Course> courses;


}
