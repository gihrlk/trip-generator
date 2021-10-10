# Assumptions
1. Input JSON file is well formed and is not missing data. Therefore, input data is not validated.
2. There are only three bus stops. To keep it simple the cost to travel between the stops are stored in TripCostStore class. Ideally, these costs should come from a datasource or a configuration file.
3. 
4. 
5. 

# How to run the application
Application code is shared at Github repo. 

Download the code and import it in to your IDE

Build jar file is available in target directory

Application need Java 8. To run the jar file use below command from root project directory

##### java -jar . 

To run the Maven test cases

# Test data
Application has been tested with sample input files located at src/main/resources/tests/input/ directory.

Description of each test data set is below.

##### tap-data-1.json
File contains two taps with the same primary account number. This will generate a COMPLETED trip.

##### tap-data-2.json
File contains four taps using the same card. Results in two completed trips.

##### tap-data-3.json
File contains five taps using the same card. Results in two completed trips and one incomplete trip.

##### tap-data-4.json
File contains seven taps using the same card. Results in two completed trips, one incomplete trip and one cancelled trip.

##### tap-data-5.json
File contains seven taps using multiple cards. Results in two completed trips, one incomplete trip and one cancelled trip.

##### tap-data-6.json
File contains 100 taps using multiple cards. Results in ? completed trips, ? incomplete trips and ? cancelled trips.
