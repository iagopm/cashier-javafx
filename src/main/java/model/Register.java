package model;

import org.springframework.context.annotation.Bean;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class Register implements Serializable {
    @Id
    private String username;
    @Column
    private String password;

    public Register(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public Register() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Id
    public String getUsername() {
        return username;
    }
}
