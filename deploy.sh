#!/bin/bash

git pull

docker build -t master-for-an-hour-backend-6-image .

docker stop master_for_an_hour_backend_container

docker rm master_for_an_hour_backend_container

docker run -d --name master_for_an_hour_backend_container \
   -p 8081:8081 \
   -v /root/media:/root/media \
   master-for-an-hour-backend-6-image

docker logs -f master_for_an_hour_backend_container
