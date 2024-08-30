#!/bin/bash
echo `sudo yum install -y yum-utils device-mapper-persistent-data lvm2`
echo `sudo yum-config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo`
echo `yum makecache fast`
echo `sudo yum -y install docker-ce docker-ce-cli containerd.io`
echo `sudo systemctl start docker`
echo `sudo systemctl enable docker`
echo `docker version`

# 换源
# 目标文件路径
FILE=/etc/docker/daemon.json
# 检查文件是否已存在
if [ -f "$FILE" ]; then
    echo "$FILE exists, creating a backup."
    sudo cp $FILE ${FILE}.bak
else
    echo "$FILE does not exist, creating new file."
fi
# 写入新内容到 daemon.json
sudo bash -c "cat > $FILE <<EOF
{
    \"registry-mirrors\": [
        \"https://docker.m.daocloud.io\",
        \"https://docker.1panel.live\",
        \"https://noohub.ru\",
        \"https://huecker.io\",
        \"https://dockerhub.timeweb.cloud\"
    ]
}
EOF"
echo "Contents written to $FILE"

echo `sudo service docker restart`
echo `docker compose up -d`