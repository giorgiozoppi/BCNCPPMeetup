# TransferService REST WebService for money transfer.
TransferService is a rest web service 
written in Java using Spring Framework and JWT 
authentication, even if the specification 
will not mention any authentication, nowadays 
in the web enviroment the authentication is 
essential.
It exposes the following endpoints:
1. http://yourhost:8080/sign-up - User registration and 
JWT token assignement (POST)
2. http://yourhost:8080/users/login - user session authentication
It will provide the authentication token for further operations (POST)
3. http://yourhost:8080/users/{username}. 
{username} is a valid user in the system. 
It provides the metadata for the user (uid, username) 
4. http://yourhost:8080/transferservice/create 
(POST).It provide a way to create a new account to an authenticated user
we follow the principle to separate authentication mechanism 
from the account creation. The account can be created only 
with a positive balance.
5.  http://yourhost:8080/transferservice/transfer (method POST)
We transfer the value from an account to another, with 
the following validation rules. 
# Validation Rules.
1. Any negative transfer will be detected
2. The user who is doing the transfer shall be authenticated.
3. The user cannot transfer arbitrary accounts.
4. The user account amount after the tranfer shall be positive or zero.
If this doesn't happen the system will rollback to previous balance.
#SSL Notes

We don't provide yet support for https even the keystore is there.
It is enough to change application.properties with the following values:

server.ssl.key-password=Minnie1977 
server.ssl.key-store=classpath:keystore.jks
server.ssl.key-store-type=JKS

#User Story: Account transfering
1. Bob API client sign-up to the system.
2. Bob API client logs-in to the system with a post at /login
3. Bob API client asks create an account with a post at /create with an initial amount 
or zero
4. Bob API client asks for Alice user accounts at /users/alice
5. Bob API client chooses an identifier from the accounts belonging to Alice,
converting from MongoDB ObjectId to an hexstring.
6. Bob API client transfer from its account using ObjectId hexstring to Alice 
ObjectId hexstring with a given amount. 
7. The system validate the transfer and throw BAD_REQUEST in case of violation 
of the business rules.

#Code organization.

Since the number of entities we don't follow the DDD pattern or use DTO.
We have decided to keep it simple as possible but consistent. So we the code is 
organized in the following directories:
a./src/main/java  - source code
b./test/main/java - integration tests using MockMvc+Mockito+Junit.
c./python - quick python scripts that covers:
           1. registeruser.py registration of a new user
           2. login.py all the operation in arbitrary way.
           3. widthdrawtests.py loop transfer until the account is negative
           4. reverswithdraw.py transfer from one account to another.
 Packages:
 com.jozoppi.business - Business rules.
 com.jozoppi.common - Common utility
 com.jozoppi.controllers - Spring controlles.
 com.jozoppi.exceptions  - Exception annotated with HTTP return code.
 com.jozoppi.models  - Entities
 com.jozoppi.repositories - Repositories with the backend of MongoDB
 com.jozoppi.secuirty - Security and extensions for JSON Web Token.
 #Mongo DB configuration
 Mongo DB has been choosed for its semplicity of use and since 
 we don't have complex relational operation to do. 
 There we have created a database called transferService and two collections
 account and user. For configuring the mongodb look at application.properties.
 In our setup we have not used a password, in a production setup is mandatory.
 Spring connects at boot into MongoDB when it is configured so it is quick and 
 easy to deploy.
#Build from source
You need gradle and maven both for building the stuff:
1. gradlew compileJava
2. gradlew bootRun

Tested on Windows 10 with 
Java(TM) SE Runtime Environment (build 1.8.0_131-b11)

#Thanks 
 Thanks to the opportunity that had to create this stuff.
 
#Areas of improvements.
Some tasks are needed still to have a more robust solution.
1. Robust support for SSL.
2. Self healing. 
3. Monitoring of the service.
4. Scalability tests.

