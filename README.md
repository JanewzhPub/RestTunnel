This is the source code for restful tunnel. 

# Modules
| Module Name | Description |
| ------ | ------ |
| RestTunnelCommon | including common utilities for other modules. |
| RestTunnelClient | restful tunnel client is a java project, can be run through a bat tool. |
| RestTunnelServer | restful tunnel server is a webapplication and need to be deployed to webserver |

# Build
`mvn clean` -- clean all the modules
`mvn install` -- build all the modules

# Run
Client: target folder contains all the files you need to run the client.
Server: target folder contains .war files which you need to deploy to your webserver.