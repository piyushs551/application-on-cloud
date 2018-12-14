#!/bin/bash

INSTANCE_ID=$1

echo "Starting Terminate Instance Script"

echo "Terminating Instance: $INSTANCE_ID"

aws ec2 terminate-instances --instance-id $INSTANCE_ID

echo "waiting...."

aws ec2 wait instance-terminated --instance-ids $INSTANCE_ID

echo "deleting security group"

aws ec2 delete-security-group --group-name csye6225-fall2017-webapp

echo "Termination Ended"










