weatherpro
==========

This project is a prototype to retrieve weather information from Yahoo Weather Service, run Map Reduce to compute statistics ( ex. average temperature of Texas).

It consists of three applications/modules each of which run as a service. HBase has been used as datastore and Hadoop has been used for Map Reduce jobs.

1. Data Collector : It periodically queries Yahoo Weather Service to retrieve weather information and stores the raw data in HBase.
To accomplish this goal it needs to know zipcodes. You can upload zipcode using its REST Api. The file should be in the format as below.

"STATENAME", "ZIPCODE","WOEID"

2. Data Processor: It runs Map Reduce periodically to compute statistics and store it in HBase.

3. Web Service: This is client facing and it queries DataStore (HBase in this case) to retreive computed statistics. It has 
REST Api to output weather information of any zipcode which can be aggregated by the computed statistics.

Notes:

1. This project has the basic skeleton of three different applications and with further modification someone can easily turn it into 
scalable, fault tolerant perfect distributed system. :-). 

Good Luck !!!

How to build
=============

From top directory run

mvn clean install

How to run
============

1. Data Collector:

cd datacollector
mvn -Prun

2. Data Processor:

cd dataprocessor
mvn -Prun

3. Service

cd service
mvn -Prun


