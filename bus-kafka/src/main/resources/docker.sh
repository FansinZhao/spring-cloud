#!/usr/bin/env bash
# kafka container
# 172.17.0.3:9092
# 172.17.0.3:2181
#
docker run -d --name my-kafka fansin/kafka


curl -d "bus.key='hi bus'&bus.key1='hi bus'" http://127.0.0.1:8015/bus/env
curl -d "" http://127.0.0.1:8015/bus/refresh
