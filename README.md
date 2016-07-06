# CloudClusterSim (CCS)
CloudClusterSim is a cluster formation simulator. The main objective of CCS is to allow testing of different algorithms for cluster formation. The simulator can use traces from real world in order to test the performance of cluster formation algorithms.

## Installation
CCS is written in Java 1.8 and has the following requirements for building and running:
* maven 3.3.3 or newer
* JDK 1.8 or newer
* MongoDB 3.2 or newer

The project can be build by maven using 
`mvn package`

## Usage
The simulator allows users to define their cluster and parameters for their simulation in JSON files. Once the parameters are defined, the program can be started with `java -jar cloud-simulator.jar`
