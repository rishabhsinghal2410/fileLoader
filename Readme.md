**FileLoader web application:**

It is used to upload a CSV file containing the deals information to Database.

File upload process is done in async manner, providing flexibility to the user to upload file even when another file is in the process to get loaded.



**Steps to deploy the Application:**

Execute following command to generate docker image `mvn clean package docker:build`

After that execute following command to start docker container `docker-compose up`

Once the `docker-compose up` has been executed it will start 2 container one with MySql and other with the fileloader app.

Application is accessible on url :- *http://localhost:8080/fileLoader*


*Sample CSV file can be found in sampleCSVFile directory
    -> 10_deals.csv - contains 5 legitimate deal, and 5 corrupted deals
    -> 100_deals.csv - contains 62 legitimate deal, and 38 corrupted deals
    -> 1000_deals.csv - contains 541 legitimate deal, and 459 corrupted deals
    -> 10000_deals.csv - contains 5627 legitimate deal, and 4373 corrupted deals
    -> 100000_deals.csv - contains 56118 legitimate deal, and 43882 corrupted deals


