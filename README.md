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
> **Note:**  Pre-requirement
> tomcat: 8+
> jdk: 1.8+
> maven: 3+

# WSS Configure
We use security websocket for the client-server communication. So we need some configuration to your web server to support wss. 
You can find the keystore file at "<RestTunnelClient>/src/main/resources/mfoglight.pfx", and the password for keystore is "foglight".
- Tomcat
Add following configuration to tomcat's server.xml (<TOMCAT_HOME>/conf/server.xml).
```xml
<Connector port="8443" maxHttpHeaderSize="8192" maxThreads="150" 
           minSpareThreads="25" maxSpareThreads="75" enableLookups="false" 
           disableUploadTimeout="true" acceptCount="100" scheme="https" 
           secure="true" SSLEnabled="true" clientAuth="false" sslProtocol="TLS" 
           keystoreFile="conf/mfoglight.pfx" keystorePass="foglight" 
           keystoreType="PKCS12"/>
```
> **Note:** We will us mfoglight.pfx to secure the communication between client and server in our application. You can generate yours and change the server.xml configuration for server and change TunnelClient reference for client. 

# Request Access Key
There are two kind of access key, one we called "mtoken" is used to identify the mobile client and the other one we called "ttoken" is used to identify the client java application. 
First, you need to request a ttoken by your custom code, then you can use this custom code and ttoken to change the client.bat/.sh arguments before startup the client application.
Then, before calling the FMS restful APIs, you need to request a mtoken with your custom code and then you can use this mtoken to request the FMS restful APIs.

Request ttoken
--------------
|                  | 		                      |
 ----------------- | ----------------------------
| **PROTOCOL** | https |
| **URL** | /ttoken |
| **METHOD**| GET |
| **HEADER**| Custom-Code |
 ----------------- | ----------------------------
> **Note:**  An example of success response as below:
 >```json
> {
>     "status": 1,
>     "data": {
>         "ttoken": "ZxdGqSdsV9nsG0XWvsM4F0Qt0LhpX6cbYiCxuSwOo6ON3p4KJEZG9go88+TCpAGrd6wTjStc9PGjHmTXFWA+hnDfUjgDyQT/R+ybQBfRIR/UX+imn3SvVrDwnJeAptvFXUnI/nGNbr/zAdobmHABh9G79cIIblUvVWzEgP1jXZo="
>     }
> }
>```

Request mtoken
--------------
|                  | 		                      |
 ----------------- | ----------------------------
| **PROTOCOL** | https |
| **URL** | /mtoken |
| **METHOD**| GET |
| **HEADER**| Custom-Code |

> **Note:**  An example of success response as below:
> ```json
> {
>     "status": 1,
>     "data": {
>         "mtoken": "SjmdqbeeqkvLV/SnpzGa8v0e5Us="
>     }
> }
>```

# Run
- Server
You can find <RestTunnelServer>/target/api.war after build, and then you need to deploy this war file to your web server.  
- Client
You can find <RestTunnelClient>/target/security.policy after build, and you need to update the ip addresses for SocketPermission. One is your FMS's ip:port, and the other one is your web server's ip:port.
You can find <RestTunnelClient>/target/startClient.bat after build, and you need to update the JAVA_HOME, RestTunnelServer's connection url (wss) and FMS's connection url (http). 
Then in the command line, start client from startClient.bat.
