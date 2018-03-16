# intern.apply-android

Please see the [main repository](https://github.com/DimaMukhin/intern.apply) of the project for more information.

## running the application on production mode

1. clone the project
2. use android studio to compile and run the application

## running the application on development mode

1. clone the project
2. run the development server locally (see how to run the server [here](https://github.com/DimaMukhin/intern.apply))
3. change the base url of the api client to point to your local host server (see next topic for more details)
4. use android studio to compile and run the application

## changing api base url to point at local host development server

1. open `InternAPI.java`
2. change `private final String BASE_URL = "https://intern-apply.herokuapp.com/";` to point at `http://<localhost-ip>:3000`
3. example: `private final String BASE_URL = "http://192.168.1.2:3000";`  

Why manualy changing the BASE URL of the api? because Retrofit does not support "localhost" yet. We decided to keep using Retrofit anyways since this is not a big deal.

## running tests

right click on "AcceptanceTests" and click on "run 'AcceptanceTests'"
