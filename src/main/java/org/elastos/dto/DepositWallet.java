package org.elastos.dto;

import org.elastos.pojo.ElaHdWallet;

import javax.persistence.*;

@Entity
@Table(name="deposit_wallet")
public class DepositWallet {
    @Id
    private Long id;
    @Column(name="mnemonic")
    private String mnemonic;
    @Column(name="private_key")
    private String privateKey;
    @Column(name="public_key")
    private String publicKey;
    @Column(name="addr")
    private String address;
    @Column(name="max_use")
    private Integer maxUse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getMaxUse() {
        return maxUse;
    }

    public void setMaxUse(Integer maxUse) {
        this.maxUse = maxUse;
    }

    public void wallteToDepoist(ElaHdWallet wallet) {
        this.privateKey = wallet.getPrivateKey();
        this.publicKey = wallet.getPublicKey();
        this.address = wallet.getPublicAddress();
    }
}
