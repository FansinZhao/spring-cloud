#!/usr/bin/env bash
# rabbit container
# 172.17.0.3 5672 user/password
# http://172.17.0.3:15672
#
docker run -d -h my-rabbit --name my-rabbit -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=password rabbitmq:3-management

curl -d "bus.key='hi bus'&bus.key1='hi bus'" http://127.0.0.1:8013/bus/env
curl -d "" http://127.0.0.1:8013/bus/refresh
