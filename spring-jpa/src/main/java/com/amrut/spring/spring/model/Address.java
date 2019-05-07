package com.amrut.spring.spring.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue
    private Integer address_id;
    private Integer zipCode;

    @OneToOne(mappedBy = "address")
    private Customer customer;
}
