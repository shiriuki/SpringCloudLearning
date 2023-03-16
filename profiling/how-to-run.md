To do the profiling you can use VisualVM
https://visualvm.github.io/download.html

First you need to deploy the Helm chart with the value
that enable JMX monitoring:
```
general:
  jmx:
    enabled: true
```
Choose the Pod you want to profile. You can get the list of pods with this:
```
k get pods
```
For the JMX client to be able to connect to your pod use this command:
```
k port-forward <POD-NAME> 30500
```

To configure VisualVM do this:
1. Right click ``Local``
5. Select ``Add JMX Connection...``
6. In ``connection`` field add port 30500. So it will have ``localhost:30500``
7. Select ``Do not require SSL Connection``
8. Click OK.

Happy profiling!

