package syscode.addressservice.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import syscode.addressservice.dto.Address;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/address")
public class AddressController {

    @GetMapping
    Address getAddress() {
        log.info("Address requested");
        return new Address(UUID.randomUUID(), "Test address");
    }

}