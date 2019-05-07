package com.amrut.spring.spring.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Entity
public class Course {

    @Id
    @GeneratedValue
    private Integer courseId;

    private String name;

    @ManyToMany(mappedBy = "courses")
    private List<Customer> customers;
}
