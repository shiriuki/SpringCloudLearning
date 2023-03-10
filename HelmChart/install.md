To validate this Helm Chart
```
helm install --dry-run --debug -f ./values.yaml mecc .
```
Before install use this to tell Kubectl to connect `multi` cluster 
and do the installation there.
```
k config use-context multi
```
To install - if using local registry. Replace with correct IP.
```
helm install --set general.imagePrefix=x.x.x.x:5000 --set general.imagePullPolicy=Always -f ./values.yaml mecc .
```

To install - if NOT using local registry
```
helm install -f ./values.yaml mecc .
```

The services can override their configuration from a ConfigMap.
See: https://docs.spring.io/spring-cloud-kubernetes/docs/2.1.5/reference/html/#kubernetes-propertysource-implementations
The ConfigMap name must be {Helm release name}-{Spring app name}
I can be like this:
````
apiVersion: v1
kind: ConfigMap
metadata:
  name: mecc-simple-rest
data:
  application.yaml: |-
    msg: Hi from K8s
    feign:
      client:
        config:
          default:
            connectTimeout: 1000
````
Which can be deployed like this:
````
kubectl apply -f cm.yaml
````
