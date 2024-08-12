#!/bin/bash
echo `docker stop jian-mu`
echo `docker rm jian-mu`
echo `docker rmi jian-mu`
echo `docker compose -f ../docker-compose.yml up -d`