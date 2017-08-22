This is the source code for restful tunnel. 
> **Note:**  Hypothesis
> You already get a user credential from the company. Such user credential including the following items
> **Custom Code**: a code to identify you, using in RestTunnelClient side.
> **Access Token**: or say tunnel client access key, a code to verify yourself, using in RestTunnelClient side.
> **Access Key**: or say mobile client access key, a code to use as a header when you request for restful apis.
> For this sample, you can get this credential only from running the source code (run RestTunnelServer/com.quest.forge.rest.tunnel.server.service.impl.AccessKeyManagerImpl) currently.

# Modules
| Module Name | Description |
| ------ | ------ |
| RestTunnelCommon | including common utilities for other modules. |
| RestTunnelClient | restful tunnel client is a java project, can be run through a bat tool. |
| RestTunnelServer | restful tunnel server is a webapplication and need to be deployed to webserver |

# Build
`mvn clean install`
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

# Deployment/Run

Server
------

After build, you can find <RestTunnelServer>/target/api.war after build, and then you need to deploy this war file to your web server.  
> **Note:**  For support WSS please refer to section WSS Configure

Client
---------
After build, you can find a jar file named <RestTunnelClient>/target/RestTunnelClient-0.0.1-SNAPSHOT.jar, then you need to run it as below. It will startup a embedded web server at 8080 by default, and you can access localhost:8080 later.
```
java -jar RestTunnelClient-0.0.1-SNAPSHOT.jar
```
or
```
java -jar RestTunnelClient-0.0.1-SNAPSHOT.jar --server.port=8085 --logging.file=<log file path>
```
> **Note:**  There are two parameters you can modify for your environment
> server.port -- web server listening port
> logging.file -- log file path