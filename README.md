# Initial project setup.
a) Import this project to intellij. As this is play framework, it use sbt as build tool. Intial build will talk time. All the standford related import will fail.

b) Download the Standford jar from : https://stanfordnlp.github.io/CoreNLP/download.html
   Extract the jar file. And add this jar file in the module section as external directory.

c) Download couchbase from https://docs.couchbase.com/server/6.0/install/macos-install.html. Install it. 
The couchbase once run will start with the following url http://127.0.0.1:8091.

Create a cluster with name dps. Then do the following 

Please create the following in the Database :
1) username="Administrator"
2) password="couchbase"
3) bucket="dps" 

If you change these properties then update them accordingly to helper.Couchbase.java file.


# To launch the application
Then run the ConsoleRunner in the test folder.

Note before running the files :
    a) There is flag <b>preLoad</b> id true, which will populate the couchbase database.  It will take around 8 -10 mins to populate the sentences file.
    b) For future runs set <b>preLoad</b> tio false.
    

# Test cases :
Few unit test cases in test folder.

Also some basic sample out for various 4 kinds of search is present in DependencyParserCoreNLPDemo.java.  
It will populate the testSentences file to couchase and run some sample search queries. 
  


