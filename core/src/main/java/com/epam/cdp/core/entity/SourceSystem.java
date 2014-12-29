package com.epam.cdp.core.entity;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Geniy00
 */
@Entity
@Table(name = "source_system")
@Immutable
public class SourceSystem implements Serializable {

    private static final long serialVersionUID = 1820235676221506291L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "systemId", nullable = false)
    private SystemId systemId;

    @Column(name = "jmsResponseQueue", nullable = false)
    private String jmsResponseQueue;

    public static enum SystemId {
        WEB,
        DESKTOP,
        MOBILE
    }

    public SourceSystem() {
    }

    public SourceSystem(final SystemId systemId, final String jmsResponseQueue) {
        this.systemId = systemId;
        this.jmsResponseQueue = jmsResponseQueue;
    }

    public SystemId getSystemId() {
        return systemId;
    }

    public String getJmsResponseQueue() {
        return jmsResponseQueue;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final SourceSystem that = (SourceSystem) obj;

        return Objects.equals(this.systemId, that.systemId)
                && Objects.equals(this.jmsResponseQueue, that.jmsResponseQueue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemId, jmsResponseQueue);
    }

    @Override
    public String toString() {
        return "SourceSystem{" +
                "systemId=" + systemId +
                ", jmsResponseQueue='" + jmsResponseQueue + '\'' +
                '}';
    }
}
