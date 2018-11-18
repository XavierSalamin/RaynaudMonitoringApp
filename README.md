# Raynaud Monitoring App
Description du projet :




## Prerequisites
The following pieces need to be in place in order to run the application.

1. Application Android
2. Couchbase Server
3. Sync Gateway
4. Backend Java Spring Application
5. Frontend Application




Note that the project uses Lombok, so code generation through annotation processing must be enabled.

## Running the Application
At first make sure that Couchbase Server is Running
Then launch "Sync Gateway" with the right configuration

```
$ cd path_to_sync_gateway
$ sync_gateway sync-gateway-config.json
```

If all goes well, this will start a admin rest api on http://localhost:4985/_admin/
and the couchbase server interface on http://localhost:8091
Login credentials are as follows
```
Username : Administrator
Password : password

Then start the Backend Java Spring Application with the command below 
```

```

$