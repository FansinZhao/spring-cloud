CREATE DATABASE publisher;
USE publisher;

CREATE TABLE publisher (
  id        BIGINT NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  name      VARCHAR(40) NOT NULL
  COMMENT '名称',
  send_time TIMESTAMP   NOT NULL
  COMMENT '发送时间',
  result    TINYINT     NOT NULL
  COMMENT '返回结果',
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100
  CHARSET = utf8
  COMMENT '客户表';