package model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    protected Integer id;
    @Column
    String date;
    @Column
    String username;

    public Ticket(String username) {
        this.date = new Timestamp(System.currentTimeMillis()).toString();
        this.username = username;
    }

    public Ticket() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
