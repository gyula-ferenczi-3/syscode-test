package syscode.profileservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Student {

    @Id
    @Column
    private UUID id;

    @NotEmpty(message = "Name can not be empty")
    @Column
    private String name;

    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid email format")
    @Column
    private String email;

    //Did not know where I meant to use the address service, so I just put this here, If you wanted to try it
    // @Transient
    // @JsonInclude(JsonInclude.Include.NON_NULL)
    // private Address address;

}

