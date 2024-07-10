#!/bin/bash
echo `sudo yum install -y yum-utils device-mapper-persistent-data lvm2`
echo `sudo yum-config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo`
echo `yum makecache fast`
echo `sudo yum -y install docker-ce docker-ce-cli containerd.io`
echo `sudo systemctl start docker`
echo `sudo systemctl enable docker`
echo `docker version`
echo `docker compose up -d`