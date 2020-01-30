# cf-demo-application

Java application using Spring boot framework to be deployed in Cloud Foundry. Developed using the 12 factors principle.

Check the branches to follow the implementation of the factors. 

## Factor 1 - Codebase 

Initializes the git repository, enables everyone to have access to the code, and ensures only developers with the proper rights can change the code.

## Factor 2 - Dependencies

Convert project to Maven. The application download the dependencies from the [maven repository](https://mvnrepository.com/). It also helps to ([avoid surprises](https://kodfabrik.com/journal/i-ve-just-liberated-my-modules/)).

By converting our application to maven we can already build running `mvn clean install`. This command will generate the `cf-demo-application-0.0.1-SNAPSHOT.jar` at the `target` folder and install it to our maven local repository (check your `.m2` folder).

## Factor 5 -  Build, release, run​

We add the Spring Boot as a dependency for the project, since it eases the development of java applications. Once we add the `DemoApplication.class` we are able to run our application. There are also tests to ensure our application is working as expected.  

We also start to use the [spring boot maven plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/usage.html) to repackage our project with auto-executable `.jar`.

It also enables us to run the application from the command line. `mvn spring-boot:run`.

Now, we add the `spring-boot-starter-web` (Spring MVC) dependency to our application, it will add the web flavor to our app. By default we will have a Apache Tomcat running in the port 8080.

By adding the `DemoController` class, we start to expose the endpoint `/v1/helloWorld`. After you started the app, you can access the resource by sending an HTTP GET to `http://localhost:8080/v1/helloWorld`.

## Factor 3 - Configuration && Factor 9 - Disposability​

The informations about our application and the platform's dependencies are described in the `manifest.yml`.

We are providing the name of our application, the path to its executable and the [buildpack](https://docs.cloudfoundry.org/buildpacks/) we want to use. 

You can describe the cloud foundry buildpacks by executing `cf buildpacks`.

Since we are not specifying any routes, cf will determine our route once we push the application. `cf push`.

Once we push the application to the platform, the platform will start our application. It also download any dependency (buildpack or services required). The log below shows the output of the command.

``` console
Updating app with these attributes...
  name:                demo-application
  path:                ...\target\cf-demo-application-0.0.1-SNAPSHOT.jar
  buildpacks:
    java_buildpack
  command:             JAVA_OPTS="-agentpath:$PWD/.java-buildpack/open_jdk_jre/bin/jvmkill-1.16.0_RELEASE=printHeapHistogram=1 -Djava.io.tmpdir=$TMPDIR -XX:ActiveProcessorCount=$(nproc) -Djava.ext.dirs=$PWD/.java-buildpack/container_security_provider:$PWD/.java-buildpack/open_jdk_jre/lib/ext -Djava.security.properties=$PWD/.java-buildpack/java_security/java.security $JAVA_OPTS" && CALCULATED_MEMORY=$($PWD/.java-buildpack/open_jdk_jre/bin/java-buildpack-memory-calculator-3.13.0_RELEASE -totMemory=$MEMORY_LIMIT -loadedClasses=17322 -poolType=metaspace -stackThreads=250 -vmOptions="$JAVA_OPTS") && echo JVM Memory Configuration: $CALCULATED_MEMORY && JAVA_OPTS="$JAVA_OPTS $CALCULATED_MEMORY" && MALLOC_ARENA_MAX=2 SERVER_PORT=$PORT eval exec $PWD/.java-buildpack/open_jdk_jre/bin/java $JAVA_OPTS -cp $PWD/. org.springframework.boot.loader.JarLauncher
  disk quota:          1G
  health check type:   port
  instances:           1
  memory:              1G
  stack:               cflinuxfs3
  services:
    txs-br-log
    txs-cms
  routes:
    demo-application.cfapps.sap.hana.ondemand.com
```

The command `cf app <application-name>` will show health information about the application. You may notice it was not required any sort of clean up or start up activity. Starting the application is super fast.

## Factor 6 - Processes && Factor 8 - Concurrency

We can manage the number of instances running by executing `cf scale -i <numberOfInstances>`.

Then running the `cf app <application-name>` will show the information about the processes.

```console
     state     since                  cpu    memory         disk           details
#0   running   2019-04-25T22:52:23Z   0.1%   256.9M of 1G   145.3M of 1G
#1   running   2019-04-25T22:57:01Z   0.1%   265.4M of 1G   145.3M of 1G
```

## Factor 4 - Backing Services && Factor 7 – Port binding​ && Factor 10 – Dev/prod parity && Factor 11 – Logs​

At last we are adding two more endpoints to our application. An HTTP POST `/v1/developer` to create a developer​ and an HTTP GET `/v1/developer` to get the list of existing developers.

We start by adding the `Developer` model. Then we use the `spring-data` dependency to `extends CrudRepository` and have a JPA implementation available. [Hibernate](http://hibernate.org/orm/) is the default implementation of JPA for Spring. 

The `DeveloperRepository` will be consumed by the `DeveloperService` which will contain the business logic. Finally, the `DeveloperController` will expose to the internet the business methods available in the `DeveloperService`.

All these dependencies are injected using the `@Autowired` feature of the spring.

To execute locally, we'll be using the `h2` database that run in memory. Everytime we restart the application it'll lose the data.

In the cloud foundry, we binded our application to a [postgreSQL](https://www.postgresql.org/) instance calle `txs-cms` you can find the relationship in the `manifest.yml`. 

The connection of the backing service will be done automatically, spring boot understands there is a postgreeSQL connected to our application and reads from the cf `VCAP_SERVICES` the information about how to connect to the database.

Therefore, any changes the instance suffers (port, ip, ...) won't affect the application.

And we can use the same code to run locally and in the cloud foundry.

The application is also binded to a logging service, you can find logging information in the kibana dashboard.
 
   
