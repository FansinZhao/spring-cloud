CREATE DATABASE provider;
USE provider;

# bytetcc-supports.jar/bytetcc.sql
CREATE TABLE bytejta (
  xid  varchar(32),
  gxid varchar(40),
  bxid varchar(40),
  ctime bigint(20),
  PRIMARY KEY (xid)
);
# user
CREATE TABLE tb_account_one (
  acct_id varchar(16),
  amount double(10, 2),
  frozen double(10, 2),
  PRIMARY KEY (acct_id)
) ENGINE=InnoDB;

insert into tb_account_one (acct_id, amount, frozen) values('1001', 10000.00, 0.00);