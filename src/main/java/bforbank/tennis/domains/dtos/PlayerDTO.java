package bforbank.tennis.domains.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {
    private Long id;

    @NotBlank(message = "Player name is required")
    private String name;
    private int age;
    private int rank;
    private String country;
}

