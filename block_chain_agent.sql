CREATE SCHEMA `block_chain_agent` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;

SET FOREIGN_KEY_CHECKS=0;

drop table if exists `deposit_wallet`;
create table deposit_wallet(
    id bigint,
    mnemonic varchar(250) not null comment 'mnemonic of wallets',
    private_key varchar(66) not null comment 'private key of deposit wallet',
    public_key varchar(66) not null comment 'public key of deposit wallet',
    addr varchar(34) not null comment 'address of deposit wallet',
    max_use int not null comment 'max sum of up chain wallets',
    primary key (`id`)
);

drop table if exists `up_chain_records`;
create table up_chain_records(
    id bigint auto_increment,
    txid varchar(128) not null comment 'up chain txid',
    type varchar (10) not null comment 'up chain type',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key (`id`)
);
