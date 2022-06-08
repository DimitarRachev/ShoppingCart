package Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "vegetables")
public class Vegetable extends Product implements Serializable {
    @Transient
    public static final String TEXT_RESET = "\u001B[0m";
    @Transient
    public static final String TEXT_RED = "\u001B[31m";

    @Column(name = "expiration_date")
    private Date expirationDate;

    public Vegetable(String name, double cost, int unitsAvailable, Date expirationDate) {
        super(name, cost, unitsAvailable);
        this.expirationDate = expirationDate;
    }

    public Vegetable() {
        super();
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return String.format("%-60s %-60s %s", (TEXT_RED + "Vegetable: " + TEXT_RESET + getName()),
                (TEXT_RED + " Expiration Date: " + TEXT_RESET + expirationDate),
                (TEXT_RED + " Price: " + TEXT_RESET + "$" + getCost()));
    }
}
