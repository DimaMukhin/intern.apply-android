# intern.apply-android

Please see the [main repository](https://github.com/DimaMukhin/intern.apply) of the project for more information.

## Code Debt/Bugs

1. the test server run's on every build - for example: when you build project, run app or tests
    - Code debt: we need the test server for system tests but couldn't find a fix, we plan to talk to you about this
     and find a better way if any exists. Also this only happens in the development environment.
2. the test server run's twice - once in configuration phase and then again in execution phase. This however does not affects anything as the second run will terminate immediately.

## running the application on production mode

1. clone the project
2. for some reasons our app requires to be run without instant run. It is on by default, to turn it off
   go to file (top left), Settings, "Build, Execution, Deployment", "Instant Run", and tick off the
   "Enable Instant Run to ..."
3. use android studio to compile and run the application

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

right click on "AllIntegrationTests" and click on "run 'AllIntegrationTests'"

## running system tests

1. Set the path of local web server (intern.apply) in intern.apply.android/ServerScript.bat on first line.
2. Right click on "SystemTests" and click on "run 'SystemTests'"
