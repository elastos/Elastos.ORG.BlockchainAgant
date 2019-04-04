package org.elastos.dto;

import javax.persistence.*;

@Entity
@Table(name="service_access_key",
        indexes = {@Index(name = "key_idx",  columnList="key_id", unique = true),
                   @Index(name="user_service_idx", columnList = "user_service_id")})
public class ServiceAccessKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="key_id", nullable = false, length = 64)
    private String keyId;
    @Column(name="key_secret", nullable = false, length = 64)
    private String keySecret;
    @Column(name="status", nullable = false, length = 10)
    private String status;
    @Column(name="time", nullable = false, length = 32)
    private String time;
    @Column(name="note", length = 100)
    private String note;
    @Column(name="user_service_id", nullable = false)
    private Long userServiceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getKeySecret() {
        return keySecret;
    }

    public void setKeySecret(String keySecret) {
        this.keySecret = keySecret;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getUserServiceId() {
        return userServiceId;
    }

    public void setUserServiceId(Long userServiceId) {
        this.userServiceId = userServiceId;
    }
}

