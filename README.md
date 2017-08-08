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

# WSS Configure
We use security websocket for the client-server communication. So we need some configuration to your web server to support wss. 
You can find the keystore file at "<RestTunnelClient>/src/main/resources/foglightwss.keystore", and the password for keystore is "questfoglight".
- Tomcat
Add following configuration to tomcat's server.xml (<TOMCAT_HOME>/conf/server.xml).
`<Connector
    protocol="org.apache.coyote.http11.Http11NioProtocol"
    port="8443" maxThreads="200"
    scheme="https" secure="true" SSLEnabled="true"
    keystoreFile="<path to keystore file>" keystorePass="<keystore password>"
    clientAuth="false" sslProtocol="TLS" />`
`<Connector protocol="org.apache.coyote.http11.Http11NioProtocol"
    port="8443" />`
> **Note:** We will us foglightwss.keystore to secure the communication between client and server in our application. You can generate yours and change the server.xml configuration for server and change TunnelClient reference for client. 

# Run
Client: target folder contains all the files you need to run the client.

Server: target folder contains .war files which you need to deploy to your webserver.