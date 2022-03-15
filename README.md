# Zippy The Werewolf

## About
A service storing files in the cloud, providing a link for downloading.

## The basic idea
Zippy The Werewolf will accept any files. He will zip them and put them in the database. When this process is done, he will send the user a mail with a link to the zip. However, He knows that when he transforms, he will be super angry at all the zip files in the database, and go crush them.

## To run locally 
1. create application-dev.properties inside resources directory and add the following:
```
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
spring.servlet.multipart.enabled=true
MAIL_ADDRESS=
MAIL_PASSWORD=
LINK_URL=http://localhost:8080/files/
spring.data.mongodb.uri=
```
2. Fill in the blanks, set "dev" as Active profiles in Run Config, and you should be up and running.

## Stack
* Server: Spring Boot
* Host: Heroku
* Database: MongoDB
