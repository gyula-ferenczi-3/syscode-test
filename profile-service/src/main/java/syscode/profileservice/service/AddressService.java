package syscode.profileservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import syscode.profileservice.dto.Address;

@Service
public class AddressService {

    @Value("${address.service.url}")
    private String addressServiceUrl;

    @Value("${address.service.user}")
    private String addressServiceUser;

    @Value("${address.service.password}")
    private String addressServicePassword;

    private final WebClient webClient;

    public AddressService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Address getAddressData() {
        return webClient.get()
                .uri(addressServiceUrl)
                .headers(headers -> headers.setBasicAuth(addressServiceUser, addressServicePassword))
                .retrieve()
                .bodyToMono(Address.class)
                .block();
    }

}
