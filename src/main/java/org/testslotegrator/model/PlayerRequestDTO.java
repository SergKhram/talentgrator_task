package org.testslotegrator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerRequestDTO {
    @JsonProperty("currency_code")
    private String currencyCode;
    private String email;
    private String name;
    @JsonProperty("password_change")
    private String passwordChange;
    @JsonProperty("password_repeat")
    private String passwordRepeat;
    private String surname;
    private String username;
}
