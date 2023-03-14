Download JMeter, and put it in this same folder.
https://jmeter.apache.org/download_jmeter.cgi

Rename the downloaded folder to ``jmeter``.
Move file ``jmeter-grpc-request.jar`` to ``./jmeter/lib/ext/``

If you are running JMeter from a machine that is different from the one
that hosts the minikube cluster, go to the machine that runs the cluster and:
- Create a port forward: ``socat TCP-LISTEN:30002,fork TCP:192.168.205.52:30002``
Port 30002 (you can change it), is the port this machine will use to listen for 
JMeter requests and then forwards them to the cluster. The IP is the IP of one of
the nodes in the cluster. You can get them with ``k get nodes -o wide``, and the
final 30002 is the port set in the node port that allows access to the service.

Now back to the machine that is running JMeter, edit file GRPCLoad.jmx, change these to match above.
``<stringProp name="GRPCSampler.host">192.168.2.7</stringProp>
<stringProp name="GRPCSampler.port">30002</stringProp>``

Change this to set the number of concurrent request:
``<stringProp name="ThreadGroup.num_threads">100</stringProp>``

Finally run
``./jmeter/bin/jmeter -n -t GRPCLoad.jmx``

Note:
``k top pods``
Has a delay, and doesn't show the actual load in the pods.
