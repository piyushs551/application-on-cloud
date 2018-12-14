#!/bin/bash

INSTANCE_NAME=$1

echo "Starting gcloud Launch Instance Script"

gcloud compute instances create $INSTANCE_NAME --image-family ubuntu-1604-lts --image-project ubuntu-os-cloud --zone us-east1-c

echo "Instance Launched"

