#!/bin/bash
echo "Please Enter a Name for the stack you want to create"
read stack_name
hosted_id=$(aws route53 list-hosted-zones --query 'HostedZones[0].[Name]' --output text)
vpc_id=$(aws ec2 describe-vpcs --query 'Vpcs[0].[VpcId]' --output text)
cert_arn=$(aws acm list-certificates --query 'CertificateSummaryList[0].[CertificateArn]' --output text)
echo ${hosted_id}

jq '.Resources.WebServerSecurityGroup.Properties.GroupName = "csye6225-fall2017-'$stack_name'-webapp"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.LoadBalancerSecurityGroup.Properties.GroupName = "csye6225-fall2017-'$stack_name'-loadBalancer"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.Listener.Properties.Certificates[0].CertificateArn = "'$cert_arn'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.WebServerSecurityGroup.Properties.VpcId = "'$vpc_id'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.DataBaseServerSecurityGroup.Properties.VpcId = "'$vpc_id'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.subnet1.Properties.VpcId = "'$vpc_id'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.subnet2.Properties.VpcId = "'$vpc_id'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.myTargetGroup.Properties.VpcId = "'$vpc_id'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.LoadBalancerSecurityGroup.Properties.VpcId = "'$vpc_id'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.myDNSRecord.Properties.HostedZoneName = "'$hosted_id'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.myDNSRecord.Properties.RecordSets[0].Name = "'$hosted_id'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.S3Bucket.Properties.BucketName = "'$hosted_id'bucket"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

echo "***Done***"

aws cloudformation create-stack --stack-name ${stack_name} --template-body file://../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json --capabilities=CAPABILITY_NAMED_IAM

placeholder="placeHolder"

jq '.Resources.WebServerSecurityGroup.Properties.GroupName = "csye6225-fall2017-'$placeholder'-webapp"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.LoadBalancerSecurityGroup.Properties.GroupName = "csye6225-fall2017-'$placeholder'-loadBalancer"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.Listener.Properties.Certificates[0].CertificateArn = "'$placeholder'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.WebServerSecurityGroup.Properties.VpcId = "'$placeholder'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.DataBaseServerSecurityGroup.Properties.VpcId = "'$placeholder'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.LoadBalancerSecurityGroup.Properties.VpcId = "'$placeholder'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.subnet1.Properties.VpcId = "'$placeholder'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.subnet2.Properties.VpcId = "'$placeholder'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.myTargetGroup.Properties.VpcId = "'$placeholder'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.myDNSRecord.Properties.HostedZoneName = "'$placeholder'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.myDNSRecord.Properties.RecordSets[0].Name = "'$placeholder'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

jq '.Resources.S3Bucket.Properties.BucketName = "'$placeholder'"' ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json > tmp.$$.json && mv tmp.$$.json ../cloudformation/simple-ec2-instance-securitygroup-cloudformation-stack.json

aws cloudformation wait stack-create-complete --stack-name ${stack_name}


