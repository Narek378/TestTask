Project Structure 
Tests are based in src/test/ folder both web and mobile 

POM class and utils are in src/main folder

Make sure have following installed 
JAVA 11 or higher , Allure ,Android studio, XCUI test for mobile emulators 

Running tests 

mvn clean test -DsuiteXmlFile=testngmobile.xml for Mobile 

mvn clean test -DsuiteXmlFile=testngweb.xml for Web 


Generating Allure report 

allure generate allure-results --clean  
allure serve allure-results          
