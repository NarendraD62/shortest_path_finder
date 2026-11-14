package com.pathfinder.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record GridRequest(
        @Min(value = 1, message = "Grid rows must be at least 1") @Max(value = 200, message = "Grid rows cannot exceed 200") int rows,
        @Min(value = 1, message = "Grid columns must be at least 1") @Max(value = 200, message = "Grid columns cannot exceed 200") int columns,
        @NotNull(message = "Obstacles must be provided (use an empty array when there are none)") List<List<Integer>> obstacles) { }
