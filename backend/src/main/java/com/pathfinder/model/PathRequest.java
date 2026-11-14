package com.pathfinder.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record PathRequest(
        @NotNull(message = "Algorithm is required") AlgorithmType algorithm,
        @NotNull(message = "Grid is required") @Valid GridRequest grid,
        @NotNull(message = "Start position is required") @Valid Position start,
        @NotNull(message = "End position is required") @Valid Position end) { }
