**FileLoader web application:**

It is used to upload a CSV file containing the deals information to Database.

File upload process is done in async manner, providing flexibility to the user to upload file even when another file is in the process to get loaded.



**Steps to deploy the Application:**

Execute following command to generate docker image `mvn clean package docker:build`

After that execute following command to start docker container `docker-compose up`

Once the `docker-compose up` has been executed it will start 2 container one with MySql and other with the fileloader app.

Application is accessible on url :- *http://localhost:8080/fileLoader*



