# mn_caching
Micronaut caching annotations with steps

[original guide] (https://guides.micronaut.io/latest/micronaut-cache-gradle-java.html)


### I) SIMPLE EXAMPLE WITH NEWS

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
#### 3) Add NewsService
```
/**
 * Use jakarta.inject.Singleton to designate a class as a singleton.
 */
@Singleton
/**
 * Specifies the cache name "headlines" to store cache operation values in.
 */
@CacheConfig("headlines")

    Map<Month, List<String>> headlines = new HashMap<Month, List<String>>() {{
        put(Month.NOVEMBER, Arrays.asList("November news: article 1", "November: end article 2"));
        put(Month.OCTOBER, Collections.singletonList("October: On big fat article"));
    }};
/**
 * Indicates a method is cacheable. The cache name headlines specified in @CacheConfig is used. 
 * Since the method has only one parameter, you don’t need to specify the month parameters 
 * attribute of the annotation.
 */
    @Cacheable 
    public List<String> headlines(Month month) {
        try {
            TimeUnit.SECONDS.sleep(3); /*** do some expensive calculations / async calls **/
            return headlines.get(month);
        } catch (InterruptedException e) {
            return null;
        }
    }

/**
 * The return value is cached with name headlines for the supplied month. The method 
 * invocation is never skipped even if the cache headlines for the supplied month already exists.
 */
    @CachePut(parameters = {"month"}) 
    public List<String> addHeadline(Month month, String headline) {
        if (headlines.containsKey(month)) {
            List<String> l = new ArrayList<>(headlines.get(month));
            l.add(headline);
            headlines.put(month, l);
        } else {
            headlines.put(month, Arrays.asList(headline));
        }
        return headlines.get(month);
    }
    
/**
 * Method invocation causes the invalidation of the cache headlines for the supplied month.     
 **/
        @CacheInvalidate(parameters = {"month"}) 
    public void removeHeadline(Month month, String headline) {
        if (headlines.containsKey(month)) {
            List<String> l = new ArrayList<>(headlines.get(month));
            l.remove(headline);
            headlines.put(month, l);
        }
    }
```
#### 3) Add NewsServiceTest
In simple terms the tests should 
1) first not hit cache so the response is saved 
2) the second request should be faster (< 3 Seconds) than our async call
3) If the endpoint delivers more news our method should put them in the cache

- `@TestMethodOrder(MethodOrderer.OrderAnnotation.class)`
Used to configure the test method execution order for the annotated test class.
- `@MicronautTest(startApplication = false)` Annotate the class with @MicronautTest so the Micronaut framework 
will initialize the application context and 
the embedded server.
```
@Inject 
    NewsService newsService;
```
- Inject NewsService bean.
```
    @Timeout(4) 
    @Test
    @Order(1) 
    public void firstInvocationOfNovemberDoesNotHitCache() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(2, headlines.size());
    }

```

- `@Timeout(4)`  annotation fails a test if its execution exceeds a given duration. 
It helps us verify that we are leveraging the cache.

- `@Order` is used to configure the order in which the test method should executed relative to other tests in the class.

#### 4) Add NewsController and NewsControllerTest
A endpoint returns the news for a given month
```
    @Get("/{month}")
    public News index(Month month) {
        return new News(month, newsService.headlines(month));
    }
```
The test passes when 2 request are correctly received 
the async operation takes 3 sec and we make 2 requests in 4 Seconds
```
    @Timeout(4)
    @Test
    void fetchingOctoberHeadlinesUsesCache() {
        HttpRequest request = HttpRequest.GET(UriBuilder.of("/").path(Month.OCTOBER.name()).build());
        News news = client.toBlocking().retrieve(request, News.class);
        String expected = "Oct 1";
        // First request
        assertEquals(Arrays.asList(expected), news.getHeadlines());

        // Second request
        News news2 = client.toBlocking().retrieve(request, News.class);
        assertEquals(Arrays.asList(expected), news2.getHeadlines());
    }
```


### II) CACHING USER NEWS PROVIDERS

To fetch a list of news providers the users needs a set of ids [oid, cid, aid] 
and add to the headers their bearer token providerValue1.

```
tokenKey1
providerValue1
```
#### II 2.0.1) Add ProviderService
For ProviderService the HashMap will store more values give a certain key
```
  Map<String, List<String>> providers =
          new HashMap<>() {
            {
              put("tokenKey1", Arrays.asList("providerValue1a", "providerValue1b"));
              put("tokenKey2", Collections.singletonList("providerValue2"));
            }
          };
          
// This also mean that we replace the List once a new entry is added 

  @CachePut(parameters = {"keyProvider"})
  public List<String> addProvider(String keyProvider, String newValueProvider) {
    if (providers.containsKey(keyProvider)) {
      List<String> l = new ArrayList<>(providers.get(keyProvider));
      l.add(newValueProvider);
      providers.put(keyProvider, l);
    } else {
      providers.put(keyProvider, List.of(newValueProvider));
    }
    return providers.get(keyProvider);
  }
```

























