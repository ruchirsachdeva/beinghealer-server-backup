package com.beinghealer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SBI on 4/2/2018.
 */
@Getter
@Entity
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="from_email")
    private String from = "help@beinghealer.com";

    /*anita1sachdeva@gmail.com,*/
    @Transient
    private List<String> to = Arrays.asList(splitByComma("ruchirsachdeva@gmail.com,help@beinghealer.com,anita1sachdeva@gmail.com"));

    private String name;

    private String message;

    @Transient
    private boolean isHtml = false;

    public Email() {

    }
    public Email(String name, String message) {
        this();
        this.name = name;
        this.message = message;
    }

    //getters and setters not mentioned for brevity

    private String[] splitByComma(String toMultiple) {
        String[] toSplit = toMultiple.split(",");
        return toSplit;
    }

    public String getToAsList() {
        return this.to + ",";
    }
}