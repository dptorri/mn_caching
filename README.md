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

