#!/bin/sh
minikube -p docker delete
minikube -p multi delete

minikube config set memory 3g
minikube config set cpus 2
minikube config set driver hyperkit
minikube config set container-runtime docker

minikube -p docker start --insecure-registry "192.168.205.0/25"
minikube -p docker addons enable registry

minikube -p multi start --insecure-registry "192.168.205.0/25" --nodes=4
minikube -p multi addons enable metrics-server

kubectl config use-context multi
#kubectl label nodes multi-m02 mecc.com/node-type=backend

minikube profile docker
kubectl config use-context multi

k patch storageclass standard -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"false"}}}'
k apply -f https://raw.githubusercontent.com/rancher/local-path-provisioner/v0.0.24/deploy/local-path-storage.yaml
k patch storageclass local-path -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'