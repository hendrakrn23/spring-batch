package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "country")
@Getter
@Setter
public class Country {

    @Id
    String code;
    String name;
    String continent;
    String region;

    public String getCode(){
        return code;
    }

    public String getName(){
        return name;
    }

}
