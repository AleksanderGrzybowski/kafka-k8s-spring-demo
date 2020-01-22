# Kafka + Kubernetes demo

This project is a simple system with two microservices - one producer and multiple consumers. It can be used to show, how Kubernetes can be used to scale a backend service responsible for processing Kafka messages, and how horizontal scaling is good for performance tuning.

* `minikube start`
* `./deploy.sh`
* in one terminal: `./trigger.sh`, in another terminal: `kubectl logs -f producer-xxx`

