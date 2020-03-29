## Final project. CS-DOUGH<br/>
* CS-DOUGH web application is CS:GO 1v1 tournaments platform. Users can participate or organize their 
own tournaments here. There is money system in the app to make tournaments more competitive. You can earn money
either creating a tournament or winning it.<br/>
<br/>
### Technologies Used:<br/>
* Storing data with MySQL database.<br/>
* Logging with Log4j.<br/>
* App units were tested with JUnit4.<br/>
* Usage of JSP pages was implemented with JSTL and EL, there is no scriptlets in the project.<br/>
* UI was created with bootstrap library and some raw js and jquery scripts.<br/>
<br/>
### Main Features:<br/>
* EN-RU localization.<br/>
* Twitch.tv API was used for random match online view. <br/>
* Web filter implements cross site scripting protection.<br/>
* "F5"-protection enabled.<br/>
* Input data validation present both on the client and server side.<br/>
<br/>
###How to start the project:<br/>
- Requirements:<br/>
    1. PostgreSQL 9+.<br/>
    2. Java 8+.<br/>
    3. Maven 3+.<br/>
    4. Apache Tomcat 7+.<br/>
- Steps to start:<br/>
    1. Set up database with script inside ./sql/cs_dough_sql_creation_script.sql. To make it in an appropriate way, you 
    should sync Database Settings block in ./src/main/resources/settings.properties with your novel database credentials.<br/>
    2. Start tomcat server.<br/>
    3. Run `maven clean install`.<br/>
    4. Deploy war package on started tomcat server.<br/>
    5. Index path is `[server path]/index.jsp`.


