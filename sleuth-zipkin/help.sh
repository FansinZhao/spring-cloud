#!/usr/bin/env bash

# elasticsearch
# 健康检查
curl 'http://172.16.0.3:9200/_cat/health?v'
# 查看所有节点
curl 'http://172.16.0.3:9200/_cat/nodes?v'
# 查看所有索引
curl 'http://172.16.0.3:9200/_cat/indices?v'

#filebeat


#kibana
#Discover 界面导入es的索引正则
#elasticsearch index