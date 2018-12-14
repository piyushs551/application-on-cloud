#!/bin/bash
echo "Please Enter a Name"
read stack_name
echo "Please Enter a Domain-Name"
read domain_name

gcloud compute instances create csye6225-${stack_name}-1 \
    --image-family https://www.googleapis.com/compute/v1/projects/ubuntu-os-cloud/global/images/family/ubuntu-1604-lts \
    --image-project harshal-project-1 \
    --zone us-east1-b \
    --tags fire-tag \
    --metadata startup-script="#! /bin/bash
      sudo apt-get update
      sudo apt-get install apache2 -y
      sudo service apache2 restart
      echo '<!doctype html><html><body><h1>www1</h1></body></html>' | tee /var/www/html/index.html
      EOF"

gcloud compute instances create csye6225-${stack_name}-2 \
    --image-family https://www.googleapis.com/compute/v1/projects/ubuntu-os-cloud/global/images/family/ubuntu-1604-lts \
    --image-project harshal-project-1 \
    --zone us-east1-b \
    --tags fire-tag \
    --metadata startup-script="#! /bin/bash
      sudo apt-get update
      sudo apt-get install apache2 -y
      sudo service apache2 restart
      echo '<!doctype html><html><body><h1>www1</h1></body></html>' | tee /var/www/html/index.html
      EOF"

gcloud compute instances create csye6225-${stack_name}-3 \
    --image-family https://www.googleapis.com/compute/v1/projects/ubuntu-os-cloud/global/images/family/ubuntu-1604-lts \
    --image-project harshal-project-1 \
    --zone us-east1-b \
    --tags fire-tag \
    --metadata startup-script="#! /bin/bash
      sudo apt-get update
      sudo apt-get install apache2 -y
      sudo service apache2 restart
      echo '<!doctype html><html><body><h1>www1</h1></body></html>' | tee /var/www/html/index.html
      EOF"

gcloud compute firewall-rules create compute-inbounds \
    --target-tags fire-tag --allow tcp:80,tcp:8080,tcp:443,tcp:22

gcloud compute instances list

gcloud compute addresses create lb-ip-1 \
    --region us-east1

gcloud compute http-health-checks create basic-check

gcloud compute target-pools create csye6225-pool \
    --region us-east1 --http-health-check basic-check

gcloud compute target-pools add-instances csye6225-pool \
    --instances csye6225-${stack_name}-1,csye6225-${stack_name}-2,csye6225-${stack_name}-3 \
    --instances-zone us-east1-b

gcloud compute forwarding-rules create csye-rule \
    --region us-east1 \
    --ports 80 \
    --address lb-ip-1 \
    --target-pool csye6225-pool

gcloud beta bigtable instances create csye-bigtable \
    --cluster csye-6225-cluster \
    --cluster-zone us-east1-b \
    --description bigtable-description \
    --cluster-num-nodes 3

gcloud dns managed-zones create csye6225-dns-${stack_name} \
    --dns-name csye6225-fall2017-${domain_name}.me. \
    --description xyz

gcloud dns record-sets transaction start -z csye6225-dns-${stack_name}

IP=`gcloud compute forwarding-rules describe csye-rule --region us-east1 | grep IPAddress | cut -c 12-`

gcloud dns record-sets transaction add \
    --zone csye6225-dns-${stack_name} \
    --name csye6225-fall2017-${domain_name}.me \
    --ttl 60 \
    --type A $IP

gcloud dns record-sets transaction execute -z csye6225-dns-${stack_name}

gsutil mb -p "chinmay-console" -c "regional" -l "us-east1" gs://"csye-6225-${stack_name}-bucket"

gcloud alpha pubsub topics create mytopic

gcloud beta functions deploy helloGET --trigger-http

gcloud sql instances create instance4-${stack_name} --tier=db-n1-standard-1 --region us-east1

gcloud sql instances describe instance4-${stack_name}

gcloud sql users set-password root % --instance instance4-${stack_name} --password root

gcloud sql databases create test3 --instance instance4-${stack_name}
