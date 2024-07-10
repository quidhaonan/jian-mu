#!/bin/bash
echo `docker stop jian_mu`
echo `docker rm jian_mu`
echo `docker rmi jian_mu`
echo `docker compose up -d`