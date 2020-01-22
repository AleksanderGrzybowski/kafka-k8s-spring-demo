#! /bin/bash
set -e

./gradlew bootJar

eval $(minikube docker-env)
docker build -t app .

minikube ssh sudo ip link set docker0 promisc on
kubectl config use-context minikube
kubectl delete pod -l app=producer --force --grace-period=0
kubectl delete pod -l app=worker --force --grace-period=0
kubectl apply -f kafka.yml
kubectl apply -f apps.yml
