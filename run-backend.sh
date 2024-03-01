#!/bin/bash

mvn clean spring-boot:build-image -Dspring-boot.build-image.imageName=${groupId}/${artifactId} -DskipTests \
--projects core # `--project p1,p2,p3` is comma seperated list of runnable modules

docker-compose up -d