package com.amrut.spring.spring.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class Telephone {

    @Id
    @GeneratedValue
    private Integer id_tel;

    private String type;
    private Integer number;

    @ManyToOne
    private Customer customer;
}
