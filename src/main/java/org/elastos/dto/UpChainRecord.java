package org.elastos.dto;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="up_chain_records")
public class UpChainRecord {
    public enum UpChainType{
        Deposit_Wallets_Transaction,
        Raw_Data_Up_Chain,
        Wallets_Deposit_Transaction,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="txid")
    private String txid;
    @Column(name="type")
    private UpChainType type;
    @Column(name = "create_time", updatable = false)
    @CreatedDate
    private Date time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public UpChainType getType() {
        return type;
    }

    public void setType(UpChainType type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
