package Entities;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "books")
public class Book extends Product implements Serializable{
    @Transient
    public static final String TEXT_RESET = "\u001B[0m";
    @Transient
    public static final String TEXT_RED = "\u001B[31m";

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;


    public Book() {
    }

    public Book(String name, Author author, double cost, int unitsAvailable) {
        super(name, cost, unitsAvailable);
        this.author = author;
    }


    @Override
    public String toString() {
        return String.format("%-60s %-60s %s", (TEXT_RED + "Name: " + TEXT_RESET + getName()),
                (TEXT_RED + " Author: " + TEXT_RESET + author.getFirstName() + " " + author.getLastName()),
                (TEXT_RED + " Price: " + TEXT_RESET + "$" + getCost()));
    }
}
