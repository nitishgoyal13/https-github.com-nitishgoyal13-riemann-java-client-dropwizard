# Dropwizard Riemann Bundle [![Travis build status](https://travis-ci.org/nitishgoyal13/riemann-java-client-dropwizard.svg?branch=master)](https://travis-ci.org/nitishgoyal13/riemann-java-client-dropwizard)

This bundle simplifies integrating dropwizard metrics with [Riemann](http://riemann.io/).
This bundle compiles only on Java 8.
 
## Dependencies
* [metrics3-riemann-reporter](https://github.com/riemann/riemann-java-client/tree/master/metrics3-riemann-reporter) 0.4.6 

## Usage
The bundle integrates dropwizard metrics with [Riemann](http://riemann.io/) with a simple configuration. 
 
### Build instructions
  - Clone the source:

        git clone https://github.com/nitishgoyal13/riemann-java-client-dropwizard.git

  - Build

        mvn install

### Maven Dependency
Use the following repository:
```xml
<repository>
    <id>clojars</id>
    <name>Clojars repository</name>
    <url>https://clojars.org/repo</url>
</repository>
```
Use the following maven dependency:
```xml
    <dependency>
            <groupId>io.riemann</groupId>
            <artifactId>riemann-java-client-parent</artifactId>
            <version>0.4.6</version>
    </dependency>
```

### Using Riemann bundle

#### Bootstrap
```java
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        bootstrap.addBundle(new RiemannBundle() {
            
            @Override
            public RiemannConfig getRiemannConfiguration(Configuration configuration) {
                ...
            }
        });
    }
```

### Configuration
```
riemann:
  host: my.riemann.host
  port: 5556
  prefix: mycompany.myenvironment.myservice
  pollingInterval: 60 
  tags:
    - mytag1
    - mytag2
    - mytag3
```
