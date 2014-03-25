package com.epam.cdp.core.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Geniy00
 */
@Entity
@Table(name = "fail_queue_message")
public class FailQueueMessage implements Serializable {

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
        created = new DateTime();
    }

    public FailQueueMessage(String message) {
        this.message = message;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FailQueueMessage that = (FailQueueMessage) o;

        if (!created.equals(that.created)) return false;
        if (!id.equals(that.id)) return false;
        if (!message.equals(that.message)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + created.hashCode();
        return result;
    }
}
