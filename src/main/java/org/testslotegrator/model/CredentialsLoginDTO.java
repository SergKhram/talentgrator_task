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
public class CredentialsLoginDTO { // вообще непонятно к чему эта модель в схеме
    @JsonProperty("grant_type")
    private String grantType;
    private String password;
    private String username;
}

