package com.beinghealer.dto;

import com.beinghealer.model.Email;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

@ToString
@EqualsAndHashCode
public final class EmailParams {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(EmailParams.class);

    @NotNull
    @Size(min=1, max=255)
    @org.hibernate.validator.constraints.Email
    private final String from;

    @NotNull
    @Size(min = 1, max = 50)
    private final String name;

    @NotNull
    @Size(min=8, max=100)
    private final String message;


    public EmailParams(@JsonProperty("from") String from,
                       @JsonProperty("name") String name,
                       @JsonProperty("message") String message) {
        this.from = from;
        this.name = name;
        this.message = message;
    }

    public Optional<String> getFrom() {
        return Optional.ofNullable(from);
    }


    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }


    public Email toEmail() {
        return new Email(name,from+ " sent a query message: "+ message);
    }


}
