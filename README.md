# intern.apply-android

Please see the [main repository](https://github.com/DimaMukhin/intern.apply) of the project for more information.

## Code Debt/Bugs

1. the test server runs on every build - for example: when you build project, run app or tests
    - Code debt: we need the test server for system tests but couldn't find a fix, we plan to talk to you about this
     and find a better way if any exists. Also this only happens in the development environment.
2. the test server runs twice - once in configuration phase and then again in execution phase. This however does not affects anything as the second run will terminate immediately.
3. "move the URL configuration out of the Java class and into an external XML configuration file." This is not possible with Android because a context is needed to access the resources in the XML files. There are a few hacky ways to acomplish this, but we decided not to implement them, because they introduce some anti-patterns and code smells. 

## Android Virtual Device to use when evaluating

Please use the following Android Virtual Device when evaluating for best results (or if the one you are using does not work)
"Nexus_5X_API_27_x86"  
Device: Nexus 5x  
Target API level: 27  
For more information see [AVD.md](https://github.com/DimaMukhin/intern.apply-android/blob/master/AVD.md)

## running the application on production mode (Use this when evaluating)

1. Use Android Studio (on windows please)
2. clone the project
3. for some reasons our app requires to be run without instant run. It is on by default, to turn it off
   go to "file" (top left), "Settings", "Build, Execution, Deployment", "Instant Run", and tick off the
   "Enable Instant Run to ..."
3. use android studio to compile and run the application  
4. The very first run sometimes does not work properly, so restart the application.  

If the application does not work, please try to rebuild the whole project (clean + recompile)  
Try to restart the application once if at first nothing shows up (this is an issue with Android Studio). 
Error [:app:transformClassesWithInstantRunForDebug] - Turn off Instant Run (point 2)

## running the application on development mode

1. clone the project
2. run the development server locally (see how to run the server [here](https://github.com/DimaMukhin/intern.apply))
3. change the base url of the api client to point to your local host server (see next topic for more details)
4. For some reasons our app requires to be run without instant run. It is on by default, to turn it off
   go to file (top left), Settings, "Build, Execution, Deployment", "Instant Run", and tick off the
   "Enable Instant Run to ..."
5. use android studio to compile and run the application

Error [:app:transformClassesWithInstantRunForDebug] - Turn off Instant Run (point 2)

## changing api base url to point at local host development server

1. open `InternAPI.java`
2. change `private final String BASE_URL = "https://intern-apply.herokuapp.com/";` to point at `http://<localhost-ip>:3000`
3. example: `private final String BASE_URL = "http://192.168.1.2:3000";`  

Why manually changing the BASE URL of the api? because Retrofit does not support "localhost" yet. We decided to keep using Retrofit anyways since this is not a big deal.

## running integration tests

In the "intern.apply.internapply.integrationTests" package, right click on "AllIntegrationTests.java" and click on "run 'AllIntegrationTests'"

## running system tests

Perform the following one time setup:  
1. First you need to set the path to the folder where the server is located (if you dont have the server on your machine, clone it from the "intern.apply" repository)
2. open "ServerScript.bat" which is located in the root folder of this android application.
3. change the first line from "cd C:\Users\habib\intern.apply" to "cd < path to the server folder you cloned >"

Now simply run the tests by doing the following:
4. In the "intern.apply.internapply.SystemTests" package, right click on "SystemTests.java" and click on "run 'SystemTests'"

## unit tests

There are no units to test. The application is implementing a thin client architecture, all the business logic is in the back-end server.
