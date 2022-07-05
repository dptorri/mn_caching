package example;

import io.micronaut.core.annotation.Introspected;

import java.util.List;

@Introspected
public class Provider {
    private String keyProvider;
    private List<String> valuesProvider;

    public Provider() {
    }

    public Provider(String keyProvider, List<String> valuesProvider) {
        this.keyProvider = keyProvider;
        this.valuesProvider = valuesProvider;
    }

    public String getKeyProvider() {
        return keyProvider;
    }

    public void setKeyProvider(String keyProvider) {
        this.keyProvider = keyProvider;
    }

    public List<String> getValuesProvider() {
        return valuesProvider;
    }

    public void setValuesProvider(List<String> valuesProvider) {
        this.valuesProvider = valuesProvider;
    }
}
