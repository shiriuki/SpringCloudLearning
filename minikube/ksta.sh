#!/bin/sh
minikube -p multi start --insecure-registry "192.168.205.0/25"

kubectl config use-context multi

minikube profile docker
kubectl config use-context multi

k patch storageclass standard -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"false"}}}'
k patch storageclass local-path -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'