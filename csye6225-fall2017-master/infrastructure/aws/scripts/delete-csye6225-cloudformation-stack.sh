

#!/bin/bash

echo "Please Enter a Name for the stack you want to create"
read stack_name

id=$(aws cloudformation list-stack-resources --stack-name ${stack_name} --query 'StackResourceSummaries[?LogicalResourceId==`EC2Instance`].[PhysicalResourceId]' --output text)

aws ec2 modify-instance-attribute --instance-id ${id} --disable-api-termination "{\"Value\":false}"
aws cloudformation delete-stack --stack-name ${stack_name} 
aws cloudformation wait stack-delete-complete --stack-name ${stack_name}


