package com.epam.cdp.core.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Geniy00
 */
@Entity
@Table(name = "taxi_dispatcher")
public class TaxiDispatcher implements Serializable {
    private static final long serialVersionUID = -8888243225734454214L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "jmsQueue", nullable = false)
    private String jmsQueue;

    @Column(name = "jmsQueueCapacity", nullable = false)
    private Integer jmsQueueCapacity;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "disabled", nullable = false)
    private Boolean disabled;

    /**
     * TODO: check additional attributes
     * payment account
     * amount
     */

    public TaxiDispatcher() {
    }

    public TaxiDispatcher(String name, String jmsQueue, Integer jmsQueueCapacity, String email, Boolean disabled) {
        this.name = name;
        this.jmsQueue = jmsQueue;
        this.jmsQueueCapacity = jmsQueueCapacity;
        this.email = email;
        this.disabled = disabled;
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

    public String getJmsQueue() {
        return jmsQueue;
    }

    public void setJmsQueue(String jmsQueue) {
        this.jmsQueue = jmsQueue;
    }

    public Integer getJmsQueueCapacity() {
        return jmsQueueCapacity;
    }

    public void setJmsQueueCapacity(Integer jmsQueueCapacity) {
        this.jmsQueueCapacity = jmsQueueCapacity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        TaxiDispatcher that = (TaxiDispatcher) obj;

        return Objects.equals(this.id, that.id)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.email, that.email)
                && Objects.equals(this.disabled, that.disabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, disabled);
    }

    @Override
    public String toString() {
        return "TaxiDispatcher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
