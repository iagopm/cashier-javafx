package model;

import org.springframework.test.annotation.IfProfileValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="products")
public class Product implements Serializable {
    @Id
    Long barcode;
    @Column
    String name;
    @Column
    Float price;

    public Product(Long barcode, String name, Float price) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
    }

    public Product() {

    }

    public Long getBarcode() {
        return barcode;
    }

    public void setBarcode(Long barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
