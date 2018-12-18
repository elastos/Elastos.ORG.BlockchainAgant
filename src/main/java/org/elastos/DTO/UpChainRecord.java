package org.elastos.DTO;

import org.elastos.POJO.ElaHdWallet;

import javax.persistence.*;

@Entity
@Table(name="up_chain_records")
public class UpChainRecord {
    public enum UpChainType{
        Deposit_Wallets_Transaction,
        Raw_Data_Up_Chain,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="txid")
    private String txid;
    @Column(name="type")
    private UpChainType type;

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
}
