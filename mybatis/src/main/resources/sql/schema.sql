-- 初始化数据库
-- 创建数据库
CREATE DATABASE mybatis;
-- 使用数据库
USE mybatis;
-- 创建表
-- 第二个时间戳不能插入问题
# set explict_defaults_for_timestamp=1;
-- 创建秒杀表
CREATE TABLE mybatis (
  `mybatis_id`  BIGINT       NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `name`        VARCHAR(250) NOT NULL
  COMMENT '名称',
  `number`      INT          NOT NULL
  COMMENT '数字',
  `create_time` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  PRIMARY KEY (mybatis_id),
  KEY idx_create_time (create_time)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8
  COMMENT 'mybatis表';

-- 初始化数据
INSERT INTO mybatis (name, number, create_time)
VALUES
  ('初始第一条', 1234, CURRENT_DATE ),
  ('初始第二条', 5678, CURRENT_DATE );

-- connect msql
-- docker
-- docker run --name mysql-server -e MYSQL_ROOT_PASSWORD=root -d mysql
-- -- 查看docker mysql ip
-- docker inspect mysql-server | grep IP
-- -- 连接数据库
-- mysql -h 172.16.0.6 -u root -proot