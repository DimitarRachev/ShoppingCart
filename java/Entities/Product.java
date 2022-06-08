package Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Product  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "cost")
    private double cost;

    @Column(name = "available")
    private int unitsAvailable;

//    @Column(name = "icon")
//    String picturePath;

    public Product(String name, double cost, int unitsAvailable, String icon) {
        this.name = name;
        this.cost = cost;
        this.unitsAvailable = unitsAvailable;
//        this.picturePath = icon;
    }

    public Product(String name, double cost, int unitsAvailable) {
        this.name = name;
        this.cost = cost;
        this.unitsAvailable = unitsAvailable;
//        this.picturePath = "C:\\Users\\Dimitar Rachev\\Pictures\\icons\\default.png";
    }

    public Product() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public int getUnitsAvailable() {
        return unitsAvailable;
    }

    public void setUnitsAvailable(int unitsAvailable) {
        this.unitsAvailable = unitsAvailable;
    }

//    public String getPicturePath() {
//        return picturePath;
//    }
//
//    public void setPicturePath(String picturePath) {
//        this.picturePath = picturePath;
//    }

    public boolean isAvailable() {
        return unitsAvailable > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Double.compare(product.cost, cost) == 0 && name.equals(product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cost);
    }
}
