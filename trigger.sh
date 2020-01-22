#! /bin/bash

kubectl config use-context minikube
kubectl exec -i -t $(kubectl get pod -l "app=producer" -o jsonpath='{.items[0].metadata.name}') -- touch /tmp/trigger
