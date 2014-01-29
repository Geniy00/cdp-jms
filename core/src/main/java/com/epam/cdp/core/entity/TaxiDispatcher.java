package com.epam.cdp.core.entity;

import javax.persistence.*;

@Entity
@Table(name="taxi_dispatcher")
public class TaxiDispatcher {
    private static final long serialVersionUID = -8888243225734454214L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jms_queue")
    private String jmsQueue;

    @Column(name = "jms_queue_capacity")
    private Long jmsQueueCapacity;

    @Column(name = "email")
    private String email;

    @Column(name = "disabled")
    private Boolean disabled;
    /**
     * payment account
     * amount
     */

    public TaxiDispatcher() {
    }

    public TaxiDispatcher(String name, String jmsQueue, Long jmsQueueCapacity, String email, Boolean disabled) {
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

    public Long getJmsQueueCapacity() {
        return jmsQueueCapacity;
    }

    public void setJmsQueueCapacity(Long jmsQueueCapacity) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaxiDispatcher)) return false;

        TaxiDispatcher that = (TaxiDispatcher) o;

        if (disabled != null ? !disabled.equals(that.disabled) : that.disabled != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (jmsQueue != null ? !jmsQueue.equals(that.jmsQueue) : that.jmsQueue != null) return false;
        if (jmsQueueCapacity != null ? !jmsQueueCapacity.equals(that.jmsQueueCapacity) : that.jmsQueueCapacity != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "TaxiDispatcher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
