package example;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MicronautTest(startApplication = false)
class ProviderServiceTest {

    @Inject
    ProviderService providerService;

    @Timeout(4)
    @Test
    @Order(1)
    public void firstInvocationFirstKeyDoesNotHitCache() {
        List<String> providerFirstInvocationFirstKey = providerService.getProviders("tokenKey1");
        assertEquals(Arrays.asList("providerValue1a", "providerValue1b"), providerFirstInvocationFirstKey);
    }

    @Timeout(1)
    @Test
    @Order(2)
    public void secondInvocationFirstKeyHitsCache() {
        List<String> providerSecondInvocationFirstKey = providerService.getProviders("tokenKey1");
        assertEquals(Arrays.asList("providerValue1a", "providerValue1b"), providerSecondInvocationFirstKey);
    }

    @Timeout(4)
    @Test
    @Order(3)
    public void firstInvocationSecondKeyDoesNotHitCache() {
        List<String> providerFirstInvocationSecondKey = providerService.getProviders("tokenKey2");
        assertEquals(List.of("providerValue2"), providerFirstInvocationSecondKey);
    }

    @Timeout(1)
    @Test
    @Order(4)
    public void secondInvocationSecondKeyHitsCache() {
        List<String> providerSecondInvocationSecondKey = providerService.getProviders("tokenKey2");
        assertEquals(List.of("providerValue2"), providerSecondInvocationSecondKey);
    }

    @Timeout(1)
    @Test
    @Order(5)
    public void addingProviderUpdatesCache() {
        List<String> providers = providerService.addProvider("tokenKey1", "providerValue1c");
        assertEquals(3, providers.size());
    }

    @Timeout(1)
    @Test
    @Order(6)
    public void thirdKeyIsRetrievedFromUpdatedCache() {
        List<String> providers = providerService.getProviders("tokenKey1");
        assertEquals(3, providers.size());
    }

    @Timeout(1)
    @Test
    @Order(7)
    public void invalidateFirstKeyWithCacheInvalidate() {
        assertDoesNotThrow(() -> {
            providerService.removeProvider("tokenKey1", "providerValue1c");
        });
    }

    @Timeout(1)
    @Test
    @Order(9)
    public void secondKeyCacheIsStillValid() {
        List<String> providerSecondKeyFromCache = providerService.getProviders("tokenKey2");
        assertEquals(List.of("providerValue2"), providerSecondKeyFromCache);
    }







}