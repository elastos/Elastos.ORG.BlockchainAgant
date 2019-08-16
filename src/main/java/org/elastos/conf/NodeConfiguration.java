/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.conf;

import org.elastos.entity.ChainType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * clark
 *
 * 9/3/18
 *
 *
 */
@Component
@ConfigurationProperties("node")
public class NodeConfiguration {
    private String didPrefix           ;
    private String connectionCount     ;
    private String state               ;
    private String blockTxByHeight     ;
    private String blockByHeight       ;
    private String blockByhash         ;
    private String blockHeight         ;
    private String blockHash           ;
    private String transaction         ;
    private String asset               ;
    private String balanceByAddr       ;
    private String balanceByAsset      ;
    private String utxoByAsset         ;
    private String utxoByAddr          ;
    private String sendRawTransaction  ;
    private String transactionPool     ;
    private String restart             ;
    private ChainType chainType       = ChainType.DID_SIDECHAIN;

    public String getRestart() {
        return restart;
    }

    public void setRestart(String restart) {
        this.restart = restart;
    }

    public String getConnectionCount() {
        return didPrefix + connectionCount;
    }

    public String getState() {
        return didPrefix + state;
    }

    public String getBlockTxByHeight() {
        return didPrefix + blockTxByHeight;
    }

    public String getBlockByHeight() {
        return didPrefix + blockByHeight;
    }

    public String getBlockByhash() {
        return didPrefix + blockByhash;
    }

    public String getBlockHeight() {
        return didPrefix + blockHeight;
    }

    public String getBlockHash() {
        return didPrefix + blockHash;
    }

    public String getTransaction() {
        return didPrefix + transaction;
    }

    public String getAsset() {
        return didPrefix + asset;
    }

    public String getBalanceByAddr() {
        return didPrefix + balanceByAddr;
    }

    public String getBalanceByAsset() {
        return didPrefix + balanceByAsset;
    }

    public String getUtxoByAsset() {
        return didPrefix + utxoByAsset;
    }

    public String getUtxoByAddr() {
        return didPrefix + utxoByAddr;
    }

    public String sendRawTransaction() {
        return didPrefix + sendRawTransaction;
    }

    public String getTransactionPool() {
        return didPrefix + transactionPool;
    }

    public void setConnectionCount(String connectionCount) {
        this.connectionCount = connectionCount;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setBlockTxByHeight(String blockTxByHeight) {
        this.blockTxByHeight = blockTxByHeight;
    }

    public void setBlockByHeight(String blockByHeight) {
        this.blockByHeight = blockByHeight;
    }

    public void setBlockByhash(String blockByhash) {
        this.blockByhash = blockByhash;
    }

    public void setBlockHeight(String blockHeight) {
        this.blockHeight = blockHeight;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public void setBalanceByAddr(String balanceByAddr) {
        this.balanceByAddr = balanceByAddr;
    }

    public void setBalanceByAsset(String balanceByAsset) {
        this.balanceByAsset = balanceByAsset;
    }

    public void setUtxoByAsset(String utxoByAsset) {
        this.utxoByAsset = utxoByAsset;
    }

    public void setUtxoByAddr(String utxoByAddr) {
        this.utxoByAddr = utxoByAddr;
    }

    public void setSendRawTransaction(String sendRawTransaction) {
        this.sendRawTransaction = sendRawTransaction;
    }

    public String getSendRawTransaction() {
        return sendRawTransaction;
    }

    public void setTransactionPool(String transactionPool) {
        this.transactionPool = transactionPool;
    }

    public String getDidPrefix() {
        return didPrefix;
    }

    public void setDidPrefix(String didPrefix) {
        this.didPrefix = didPrefix;
    }

    public ChainType getChainType() {
        return chainType;
    }

    public void setChainType(ChainType chainType) {
        this.chainType = chainType;
    }
}
