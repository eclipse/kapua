# Kapua REST API

Kapua exposes REST API which can be used to access Kapua data and invoke Kapua operations. REST API application is running as a dedicated
Java process.

In order to access API application in OpenShift environment, you need to access API service IP. In order to retrieve REST API IP address, 
execute the following command:

    $ oc get service | grep api
    kapua-api       172.30.200.124   <none>        8080/TCP             1d

In order to retrieve a list of API operations, you can use Swagger UI available the following URL - `http://RESTAPIADDRESSS:8080/doc`. You need to
enter `http://RESTAPIADDRESSS:8080/v1/swagger.json` into Swagger input box in order to get a list of API operations.

![REST API page](images/rest-api.png)