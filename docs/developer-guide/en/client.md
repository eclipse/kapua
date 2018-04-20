# Kapua client generation

In order to generate Kapua Java client from Swagger definition you need to start Kapua platform in the first place - see [running Kapua](running.md). 
When Kapua is up and running already, execute the following commands:

    cd kapua/client
    ./generate-client-java.sh
    
The output of the Swagger generator is available in `kapua/client/kapua-java` directory. Remember to check in the code you generated into version control.