#!/bin/bash

sudo systemctl stop tomcat8
sudo systemctl stop awslogs.service
sudo rm -rf /var/lib/tomcat8/webapps
sudo rm /var/awslogs/etc/awslogs.conf

