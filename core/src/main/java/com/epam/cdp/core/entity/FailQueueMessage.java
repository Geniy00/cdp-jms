package com.epam.cdp.core.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Geniy00
 */
@Entity
@Table(name = "fail_queue_message")
public class FailQueueMessage implements Serializable {

    private static final long serialVersionUID = 1820235621121505292L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message", length = 700)
    private String message;

    @Column(name = "created", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime created;

    public FailQueueMessage() {
    }

    public FailQueueMessage(String message) {
        this.message = message;
    }

    @PrePersist
    protected void updateDates() {
        created = new DateTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FailQueueMessage that = (FailQueueMessage) o;

        return Objects.equals(this.id, that.id) && Objects.equals(this.message, that.message) && Objects.equals(
                this.created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, created);
    }
}
