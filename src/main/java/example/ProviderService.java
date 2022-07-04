package example;

import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.CachePut;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Singleton;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Singleton
@CacheConfig("providers")
public class ProviderService {

  Map<String, List<String>> providers =
          new HashMap<>() {
            {
              put("tokenKey1", Arrays.asList("providerValue1a", "providerValue1b"));
              put("tokenKey2", Collections.singletonList("providerValue2"));
            }
          };

  @Cacheable
  public List<String> getProviders(String key) {
    try {
      TimeUnit.SECONDS.sleep(3);
      return providers.get(key);
    } catch (InterruptedException e) {
      return null;
    }
  }

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

  @CacheInvalidate(parameters = {"keyProvider"})
  public void removeProvider(String keyProvider, String providerValue) {
    if(providers.containsKey(keyProvider)) {
      List<String> l = new ArrayList<>(providers.get(keyProvider));
      l.remove(providerValue);
      providers.put(keyProvider, l);
    }
  }
}