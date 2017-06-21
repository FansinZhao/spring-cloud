#!/usr/bin/env bash
# rabbit container
# 172.17.0.3 5672 user/password
# http://172.17.0.3:15672
#
docker run -d -h my-rabbit --name my-rabbit -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=password rabbitmq:3-management

# kafka container
# 172.17.0.3 5672 user/password
# http://172.17.0.3:15672
#
docker run -d --name my-kafka fansin/kafka