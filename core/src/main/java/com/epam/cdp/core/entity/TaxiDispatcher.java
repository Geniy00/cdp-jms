package com.epam.cdp.core.entity;

import javax.persistence.*;
import java.io.Serializable;

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

    //TODO: change naming to jmsQueue
    @Column(name = "jms_queue", nullable = false)
    private String jmsQueue;

    @Column(name = "jms_queue_capacity", nullable = false)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaxiDispatcher that = (TaxiDispatcher) o;

        if (!disabled.equals(that.disabled)) return false;
        if (!email.equals(that.email)) return false;
        if (!id.equals(that.id)) return false;
        if (!jmsQueue.equals(that.jmsQueue)) return false;
        if (!jmsQueueCapacity.equals(that.jmsQueueCapacity)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + jmsQueue.hashCode();
        result = 31 * result + jmsQueueCapacity.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + disabled.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TaxiDispatcher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
