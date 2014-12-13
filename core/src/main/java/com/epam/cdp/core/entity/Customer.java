package com.epam.cdp.core.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Geniy00
 */
@Entity
@Table(name = "customer")
public class Customer implements Serializable {
    private static final long serialVersionUID = -8888243225759654214L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    public Customer() {
    }

    public Customer(String phone, String name) {
        super();
        this.phone = phone;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Customer [name=" + name + ", phone=" + phone + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Customer other = (Customer) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.phone, other.name);
    }
}
