# cs223_middleware_of_PostgreSQL

This project aims to schedule transactions outside database, using optimistic concurrency control.  
### Basic installation:  
Java 18, Spring Framework, PostgreSQL Database, JDBC for PostgreSQL, log4j  
### Database configuration:  
create 3 different database servers, named "replicate_db", "replicate_db2", "replicate_db3". Corresponding server port is 
5432, 5433, 5434. For each database server, create a table named "table_test"  
### Client 
Compile program with
```javac Application.java```   
And start client from Application with parameter " client Agent1 " to start client connecting to Agent1(default leader).  
### Agent
Start Agent from Application with parameter " agent Agent1" to start agent1. Here we have set configurations for each agent, 
for example, Agent1 has port 6666 to communicate with client and listen to database at port 5432
### Send a Transaction
To send a transaction, in the terminal that runs client, after seen ">>>", then write your transaction.  
Your transaction should be like: "R=X;W=X,4;R=Y;R=X", in which "R" represent read operation and followed "=" to split operation and object.
So in "R=X;", we request a read operation on object X. Character ";" means the end of operation.  
After you finish your transaction, enter return key to add a "\n" at the end. Our program will separate transactions with "\n"
### Crash an agent
To crash an agent, simply ctrl-c the terminal that runs agent1.
