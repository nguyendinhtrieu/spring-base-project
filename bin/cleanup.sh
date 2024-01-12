#!/bin/bash

docker stop springbootproject
docker rm springbootproject
docker image rm springbootproject:v1
yes | docker image prune -a
