# SecurityProject
Health care records with blockchain 



## Required Dependinces

- java 18
- org.apache.commons.codec (External Library)
- Junit 5
## How to run
refer to Test.java in main package in the main method there is an example. And also refer to unit tests in test package with multible tests.

## Description
> ### Main Components
1. ***Patient*** : Object representing the patient, and it has its general information ( Name, Age, Height, Weight, Sex, BloodPressure, Pulse). 
2. ***Clinic*** : An entity that represent the clinics that will save its patients' data in the blockchain, it must register to the Controller authority with its public signing key.
3. ***Controller*** : Central authority that register all the clinics, verifies all the data coming from the clinics and communicate with the blockchain. 
> ### Important points
- Every clinic has its own encryption for the data to be confidential to it, and it always encrypts any data before sending it to the blockchain. 
- The Controller always checks the validity of the blockchain as follows.
    - Checks that every block contains good hash with the required difficulty.
    - Checks that every record in the chain is from a registered clinic by checking the clinic address with and the signing of the data.
>### Flow
![This is an image](/imgs/0.jpeg)
![This is an image](/imgs/1.jpeg)
![This is an image](/imgs/2.jpeg)
![This is an image](/imgs/3.jpeg)
## what next?
