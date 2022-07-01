# mn_caching
Micronaut caching annotations with steps

[original guide] (https://guides.micronaut.io/latest/micronaut-cache-gradle-java.html)

#### 0) Enable annotation processor
```
Build > Compiler >  Enable annotation processor
```
#### 1) Create an application with CLI
- If you don’t specify the --build argument, Gradle is used as the build tool.
- If you don’t specify the --lang argument, Java is used as the language.
```
mn create-app example.caching --build=gradle --lang=java
```
#### 2) Add Caffeine dependency and configure 
- Add dependency
- Configure caches for "headline" with some sample values
  - `micronaut.caches..maximum-size`: Specifies the maximum number of entries the cache may contain
  - `micronaut.caches..charset`:The charset used to serialize and deserialize values
```
implementation("io.micronaut.cache:micronaut-cache-caffeine")

// src/main/resources/application.yml
micronaut:
  caches:
    headlines: 
      charset: 'UTF-8'
      maximum-size: 20
```
