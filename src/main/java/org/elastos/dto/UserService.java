package org.elastos.dto;

import javax.persistence.*;

@Entity
@Table(name="user_service",
        indexes = {@Index(name = "user_idx",  columnList="user_id"),
                   @Index(name="service_idx", columnList = "service_id")})
public class UserService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_id", nullable = false)//index
    private Long userId;
    @Column(name="service_id", nullable = false)//index
    private Long serviceId;
    @Column(name="rest", nullable = false)
    private Long rest = 0L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRest() {
        return rest;
    }

    public void setRest(Long rest) {
        this.rest = rest;
    }
}

