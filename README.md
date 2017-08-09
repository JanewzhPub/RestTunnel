This is the source code for restful tunnel. 

# Modules
| Module Name | Description |
| ------ | ------ |
| RestTunnelCommon | including common utilities for other modules. |
| RestTunnelClient | restful tunnel client is a java project, can be run through a bat tool. |
| RestTunnelServer | restful tunnel server is a webapplication and need to be deployed to webserver |

# Build
> **Note:** Pre-requirement
> tomcat: 8+
> jdk: 1.8+
> mvn: 3+
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
- Server
You can find <RestTunnelServer>/target/api.war after build, and then you need to deploy this war file to your web server.  
- Client
You can find <RestTunnelClient>/target/security.policy after build, and you need to update the ip addresses for SocketPermission. One is your FMS's ip:port, and the other one is your web server's ip:port.
You can find <RestTunnelClient>/target/startClient.bat after build, and you need to update the JAVA_HOME, RestTunnelServer's connection url (wss) and FMS's connection url (http). 
Then in the command line, start client from startClient.bat.

# Request Access Key
For requesting the api of Foglight Restful APIs, you need to provide an Access-Key. This key can be retrieve from API provided by Server web application.
|                  | 		                      |
 ----------------- | ----------------------------
| **PROTOCOL** | https |
| **URL** | /accesskey |
| **METHOD**| GET |
| **HEADER**| Custom-Code |
> **Note:**  An example of success response as below:
> ```json
{
    "status": 1,
    "data": {
        "accessKey": "SjmdqbeeqkvLV/SnpzGa8v0e5Us="
    }
}
```
