# csye6225-fall2017

Team Information:       

chinmay keskar, 001221409, keskar.c@husky.neu.edu

harshal neelkamal, 001645951, neelkamal.h@husky.neu.edu

snigdha joshi, 001602328, joshi.sn@husky.neu.edu

piyush sharma, 001282198, sharma.pi@husky.neu.edu
  
 Prerequisites for deploying application locally
   
 - Tomcat8
 - MySQL Database for presistance
 - IntelliJ IDE(Prefered)
 - Gradle Build Tool
     
 Building/Deploying Instructions
 -  Clone the repository into a local repository
 -  Edit Application configuration for Tomcat to specify tomact version.
 -  Build the war file for the project from gradle project tab
 -  Deploy the war file on tomcat
 -  Testing is done in Postman for different endpoints('/', '/user/register') of the application with diffrent username-password combinations

Unit/Load Tests:
-  Unit tests are present in .travis.yml file
-  Load tests can be found under Jmeter directory

TravisCI build for the project:
https://travis-ci.com/keskarCJ/csye6225-fall2017
