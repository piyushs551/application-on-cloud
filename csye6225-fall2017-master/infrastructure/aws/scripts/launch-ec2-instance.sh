#!/bin/bash

echo "Starting Launch Instance Script"

#$(aws ec2 delete-security-group --group-name csye6225-fall2017-webapp)
#echo "deleted"

VPC_ID=$(aws ec2 describe-vpcs --query 'Vpcs[0].[VpcId]' --output text)

echo "VPC_ID: $VPC_ID"

SGID=$(aws ec2 create-security-group --group-name csye6225-fall2017-webapp --description "My security group" --vpc-id $VPC_ID --output text)
echo "SGID: $SGID"

aws ec2 authorize-security-group-ingress --group-id $SGID --protocol tcp --port 22 --cidr 0.0.0.0/0

aws ec2 authorize-security-group-ingress --group-id $SGID --protocol tcp --port 80 --cidr 0.0.0.0/0

aws ec2 authorize-security-group-ingress --group-id $SGID --protocol tcp --port 443 --cidr 0.0.0.0/0

INSTANCE_ID=$(aws ec2 run-instances --image-id ami-cd0f5cb6 --instance-type t2.micro --count 1 --security-group-ids $SGID --key-name csye6225-aws --instance-initiated-shutdown-behavior stop --block-device-mappings file://mapping.json --query 'Instances[0].[InstanceId]' --output text)

#aws ec2 modify-instance-attribute --instance-id $INSTANCE_ID -
echo "INSTANCE_ID: $INSTANCE_ID"
echo "pending...." 
aws ec2 wait instance-running --instance-ids $INSTANCE_ID
echo "running!"

PUBLIC_IP=$(aws ec2 describe-instances --instance-ids $INSTANCE_ID | grep PublicIpAddress | awk -F ":" '{print $2}' | sed 's/[",]//g')

HOSTED_ZONE_NAME=$(aws route53 list-hosted-zones --max-items 1 --query 'HostedZones[0].[Name]' --output text)

echo "HOSTED_ZONE_NAME: $HOSTED_ZONE_NAME"

echo "PUBLIC_IP: $PUBLIC_IP"
jq '.Changes[0].ResourceRecordSet.ResourceRecords[0].Value = "'"$PUBLIC_IP"'"' recordset.json > tmp.$$.json && mv tmp.$$.json recordset.json
jq '.Changes[0].ResourceRecordSet.Name = "ec2.'$HOSTED_ZONE_NAME'"' recordset.json > tmp.$$.json && mv tmp.$$.json recordset.json

HOSTED_ZONE_ID=$(aws route53 list-hosted-zones --max-items 1 --query 'HostedZones[0].Id' --output text)

#HOSTED_ZONE_ID=$(aws route53 list-hosted-zones --max-items 1 | grep Id | awk ) Z44GXF6AM1NBV

echo "HOSTED_ZONE_ID: $HOSTED_ZONE_ID"

aws route53 change-resource-record-sets --hosted-zone-id $HOSTED_ZONE_ID --change-batch file://recordset.json
