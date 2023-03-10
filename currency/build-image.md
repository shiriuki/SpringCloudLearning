Run this minikube command first. 
```
eval $(m -p docker docker-env)
```
It sets these environment variables: `DOCKER_HOST`, `DOCKER_CERT_PATH`
which are used by the Docker client to connect through the remote api to
the Docker daemon running in Minikube.
To build the image use this:
```
gradle buildImage
```
If you have a local registry setup. The `pushImage` task will push
the image to the local registry running at `x.x.x.x:5000`. It will
get the IP from the env variable `DOCKER_HOST`
To push the image use this:
```
gradle pushImage
```
