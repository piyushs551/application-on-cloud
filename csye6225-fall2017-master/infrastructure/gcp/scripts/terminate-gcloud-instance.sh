#!/bin/bash

INSTANCE_NAME=$1

echo "Starting gcloud Terminate Instance Script"

gcloud compute instances delete $INSTANCE_NAME

echo "Instance Terminated"
