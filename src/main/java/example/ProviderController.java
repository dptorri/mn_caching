package example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @Get("providers/{keyProvider}")
    public Provider index(String keyProvider) {
        return new Provider(keyProvider, providerService.getProviders(keyProvider));
    }
}
