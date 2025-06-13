
package com.elearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestAttemptStatusDTO {
    private int attemptsRemaining;
    private Double lastScorePercentage;
}
