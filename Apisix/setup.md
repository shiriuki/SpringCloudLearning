This document explains how to use Apisix as a K8s ingress controller.
It assumes you have a minikube cluster installed and running the microservices
created by this repo.

More about Apisix ingress controller: 
https://apisix.apache.org/docs/ingress-controller/getting-started/
https://navendu.me/posts/hands-on-set-up-ingress-on-kubernetes-with-apache-apisix-ingress-controller/
https://navendu.me/posts/custom-plugins-in-apisix-ingress/

To try this you will need these JWTs, created using this page: https://jwt.io/
````
token: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkiOiJleHRhcHAiLCJzdWIiOiIxIiwibmFtZSI6Ik1hbnVlbCBDYWx2byIsInJvbGVzIjpbIkVESVRPUiJdLCJleHAiOjcyNTgxMTg0MDB9.IVfRgYQRTOguqPs_lsRP-cwCQ3GMyduJKYuPw9qAlGZxTRmFOc-g_erZ3V8qgX5Do15dGrT6moKG5TbKi6dSDSDWnMvYtryy8Z5CTNFAqP9abiNg4ZvvoJolq-0xhsJXCtr1tXr0oN48t2sJ3NIm64JHfFCVWCoJuzKNykrB4QjvSmR8P958JCLUsLdxVL-hihLAHbcrJDHTu_hzi2tW5cRKJ_dyVMZE0RPWihre_GiAGzHkbqlUelg38_8EEmWasAEqd328bYPpKY238idskFcRp-4DtI5S-pl5zimlcpQIMgLBPk5hIVamkvhJVP4Pa3aQiUk6-8dB-vFIARKsuw
{
"key": "extapp",
"sub": "1",
"name": "Manuel Calvo",
"roles": ["EDITOR"],
"exp": 7258118400
}
````
````
token: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkiOiJleHRhcHAiLCJzdWIiOiIyIiwibmFtZSI6IlBlZHJvIFBlcmV6Iiwicm9sZXMiOlsiVklFV0VSIl0sImV4cCI6NzI1ODExODQwMH0.PE51CI-S3VyhcBqeN4wSJe2CNePvU8H4immnTS6e0JFZMadAyQ3_bgIO9WYDZlMHYZnXIPY9A1uFzPwgm4FMTRFNajRY7yI7uHkIJ_-_Ez6Xq23E_MbwhWqysrwDKFXIywr5exUboRFxFDhBpNjuxFypjHoh551of-h8WrwTSXwGI2PWpxvTwnfBPxbEXK-m-SaBC_UuISGvtBSjIPAJY53fMlUgCMs9qBUJkWjDMGS2KOZ7Bo3skyA0k1f7JzyfJUGrWawGP5ZzaW9MPAngb2GKoctD_pFlfncgam_N4BfaF1TIXEl7hHDtqjv9kJ3zETsx-i8PER4Vx8WuGIcfjw
{
"key": "extapp",
"sub": "2",
"name": "Pedro Perez",
"roles": ["VIEWER"],
"exp": 7258118400
}
````
````
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu1SU1LfVLPHCozMxH2Mo
4lgOEePzNm0tRgeLezV6ffAt0gunVTLw7onLRnrq0/IzW7yWR7QkrmBL7jTKEn5u
+qKhbwKfBstIs+bMY2Zkp18gnTxKLxoS2tFczGkPLPgizskuemMghRniWaoLcyeh
kd3qqGElvW/VDL5AaWTg0nLVkjRo9z+40RQzuVaE8AkAFmxZzow3x+VJYKdjykkJ
0iT9wCS0DRTXu269V264Vf/3jvredZiKRkgwlL9xNAwxXFg0x/XFw005UWVRIkdg
cKWTjpBP2dPwVZ4WWC+9aGVd+Gyn1o0CLelf4rEjGoXbAAEgAqeGUxrcIlbjXfbc
mwIDAQAB
-----END PUBLIC KEY-----

-----BEGIN PRIVATE KEY-----
MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC7VJTUt9Us8cKj
MzEfYyjiWA4R4/M2bS1GB4t7NXp98C3SC6dVMvDuictGeurT8jNbvJZHtCSuYEvu
NMoSfm76oqFvAp8Gy0iz5sxjZmSnXyCdPEovGhLa0VzMaQ8s+CLOyS56YyCFGeJZ
qgtzJ6GR3eqoYSW9b9UMvkBpZODSctWSNGj3P7jRFDO5VoTwCQAWbFnOjDfH5Ulg
p2PKSQnSJP3AJLQNFNe7br1XbrhV//eO+t51mIpGSDCUv3E0DDFcWDTH9cXDTTlR
ZVEiR2BwpZOOkE/Z0/BVnhZYL71oZV34bKfWjQIt6V/isSMahdsAASACp4ZTGtwi
VuNd9tybAgMBAAECggEBAKTmjaS6tkK8BlPXClTQ2vpz/N6uxDeS35mXpqasqskV
laAidgg/sWqpjXDbXr93otIMLlWsM+X0CqMDgSXKejLS2jx4GDjI1ZTXg++0AMJ8
sJ74pWzVDOfmCEQ/7wXs3+cbnXhKriO8Z036q92Qc1+N87SI38nkGa0ABH9CN83H
mQqt4fB7UdHzuIRe/me2PGhIq5ZBzj6h3BpoPGzEP+x3l9YmK8t/1cN0pqI+dQwY
dgfGjackLu/2qH80MCF7IyQaseZUOJyKrCLtSD/Iixv/hzDEUPfOCjFDgTpzf3cw
ta8+oE4wHCo1iI1/4TlPkwmXx4qSXtmw4aQPz7IDQvECgYEA8KNThCO2gsC2I9PQ
DM/8Cw0O983WCDY+oi+7JPiNAJwv5DYBqEZB1QYdj06YD16XlC/HAZMsMku1na2T
N0driwenQQWzoev3g2S7gRDoS/FCJSI3jJ+kjgtaA7Qmzlgk1TxODN+G1H91HW7t
0l7VnL27IWyYo2qRRK3jzxqUiPUCgYEAx0oQs2reBQGMVZnApD1jeq7n4MvNLcPv
t8b/eU9iUv6Y4Mj0Suo/AU8lYZXm8ubbqAlwz2VSVunD2tOplHyMUrtCtObAfVDU
AhCndKaA9gApgfb3xw1IKbuQ1u4IF1FJl3VtumfQn//LiH1B3rXhcdyo3/vIttEk
48RakUKClU8CgYEAzV7W3COOlDDcQd935DdtKBFRAPRPAlspQUnzMi5eSHMD/ISL
DY5IiQHbIH83D4bvXq0X7qQoSBSNP7Dvv3HYuqMhf0DaegrlBuJllFVVq9qPVRnK
xt1Il2HgxOBvbhOT+9in1BzA+YJ99UzC85O0Qz06A+CmtHEy4aZ2kj5hHjECgYEA
mNS4+A8Fkss8Js1RieK2LniBxMgmYml3pfVLKGnzmng7H2+cwPLhPIzIuwytXywh
2bzbsYEfYx3EoEVgMEpPhoarQnYPukrJO4gwE2o5Te6T5mJSZGlQJQj9q4ZB2Dfz
et6INsK0oG8XVGXSpQvQh3RUYekCZQkBBFcpqWpbIEsCgYAnM3DQf3FJoSnXaMhr
VBIovic5l0xFkEHskAjFTevO86Fsz1C2aSeRKSqGFoOQ0tmJzBEs1R6KqnHInicD
TQrKhArgLXX4v3CddjfTRJkFWDbE/CkvKZNOrcf1nhaGCPspRJj2KUkj1Fhl9Cnc
dn/RsYEONbwQSjIfMPkvxF+8HQ==
-----END PRIVATE KEY-----
````

Install local-path volume provisioner
------
````
k patch storageclass standard -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"false"}}}'
k apply -f https://raw.githubusercontent.com/rancher/local-path-provisioner/v0.0.24/deploy/local-path-storage.yaml
k patch storageclass local-path -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'
````

Create custom plugin
------
````
k create ns ingress-apisix
k -n ingress-apisix create configmap plugin-in-jwt-list-claim --from-file=./in-jwt-list-claim.lua 
````

Install apisix as ingress controller
------
````
h repo add apisix https://charts.apiseven.com
h repo add bitnami https://charts.bitnami.com/bitnami
h repo update
h -n ingress-apisix install apisix apisix/apisix  --values ./apisix-values.yaml
````

Create testing routes
------
````
k apply -f ./apisix-crd-generic-role.yaml
````

