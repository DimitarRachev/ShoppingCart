package Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "fruits")
public class Fruit extends Product implements Serializable {
    @Transient
    public static final String TEXT_RESET = "\u001B[0m";
    @Transient
    public static final String TEXT_RED = "\u001B[31m";

    @Column(name = "expiration_date")
    private Date expirationDate;

    public Fruit(String name, double cost, int unitsAvailable, Date expirationDate) {
        super(name, cost, unitsAvailable);
        this.expirationDate = expirationDate;

    }

    public Fruit() {
        super();
    }

       @Override
    public String toString() {
        return String.format("%-60s %-60s %s",
                (TEXT_RED + "Fruit: " + TEXT_RESET + getName()),
                (TEXT_RED + " Expiration Date: " + TEXT_RESET + expirationDate),
                (TEXT_RED + " Price: " + TEXT_RESET + "$" + getCost()));
    }
}
