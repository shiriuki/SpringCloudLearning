For the development of this project I use Minikube as a replacement 
of Docker Desktop. This how I configure it.
```
m config set memory 8g
m config set cpus 8
m config set driver hyperkit
m config set container-runtime docker

m start -p docker --docker-opt=experimental=true --insecure-registry "192.168.205.0/25"
m profile docker
m addons enable registry
```
That created a Minikube instance with one cluster (`docker`) and one node in it. This instance
runs a Docker daemon. The `--insecure-registry` flag allows that daemon to access image repositories
without using TLS if they are in the specified network range.
Then in this instance I installed the `registry` addon that sets up a local image repository. The
image repository is accessible at the IP of the node and port 5000. You can get the IP of the node 
with this command: 
````
m ip
````
You can check if the registry is working correctly by using this command:
```
curl http://x.x.x.x:5000/v2/_catalog
```
With this setup if you set the corresponding environment variables by using this command:
````
eval $(m -p docker docker-env)
````
the docker client will be able to communicate with the Docker daemon running in Minikube, 
through the remote api. That will allow gradle plugins to use that daemon to build and 
publish images.
For the images to be published to this local registry they need to start with 
the IP of the node in the `docker` cluster that is running the registry server.
So for example if your image name is `accounts:1.0.0` then you need to prefix it with
`x.x.x.x:5000/accounts:1.0.0` where `x.x.x.x` is the IP of your local registry server.

Now, if besides using Minikube as a replacement of Docker Desktop you want to use it
to work with Kubernetes, you can set up a multi node cluster in addition to the `docker` 
cluster that we are using for the local registry and for the docker client to interact 
with a Docker daemon.
```
m start -p multi --nodes 3 --insecure-registry "192.168.205.0/25"
```
That will create an additional cluster with 3 nodes with the name `multi`. We are using 
again the flag `-insecure-registry`, that will allow the Docker daemon running in this
instance to access images repositories without using TLS. The nodes in this cluster can 
access the node running in the `docker` cluster, and for that reason can use its local
registry to download that images from there, if they were prefixed with `x.x.x.x:5000`
in you Kubernetes manifest like this:
```
│ Containers:                                                                                                                                           │
│   currency:                                                                                                                                           │                                                      │
│     Image: 192.168.205.43:5000/accounts:1.0.0 
```

Finally, to deploy the services to Minikube you can use the Helm chat of this project. Check
its documentation in the corresponding folder of this repository.
