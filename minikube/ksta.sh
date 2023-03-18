#!/bin/sh
minikube -p multi start --insecure-registry "192.168.205.0/25" --nodes=4

kubectl config use-context multi
kubectl label nodes multi-m02 mecc.com/node-type=backend

minikube profile docker
kubectl config use-context multi