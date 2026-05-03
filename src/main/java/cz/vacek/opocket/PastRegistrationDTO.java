package cz.vacek.opocket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PastRegistrationDTO {
    private Registration registration;
    private int rank;
}
