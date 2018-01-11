#!/usr/bin/env bash
#rabbitmq
docker run -itd --name springcloud-rabbitmq --net springcloud-net --ip 172.20.0.2 --restart always --hostname springcloud-rabbitmq -e RABBITMQ_ERLANG_COOKIE='secret cookie here' fansin/rabbitmq-cluster
