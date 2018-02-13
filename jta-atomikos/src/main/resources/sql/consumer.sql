# DROP DATABASE  consumer;
CREATE DATABASE consumer;
USE consumer;

CREATE TABLE consumer (
  id        BIGINT NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  name      VARCHAR(40) NOT NULL
  COMMENT '名称',
  recv_time TIMESTAMP   NOT NULL
  COMMENT '接收时间',
  result    TINYINT     NOT NULL
  COMMENT '返回结果',
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100
  CHARSET = utf8
  COMMENT '客户表';
