package com.pathfinder.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CompareRequest(
        @NotEmpty(message = "At least one algorithm is required") List<@NotNull AlgorithmType> algorithms,
        @NotNull(message = "Grid is required") @Valid GridRequest grid,
        @NotNull(message = "Start position is required") @Valid Position start,
        @NotNull(message = "End position is required") @Valid Position end) { }
