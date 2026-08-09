package org.testslotegrator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerResponseDTO {
    private Integer id;
    private String username;
    private String email;
    private String name;
    private String surname;
}
