package br.com.orbitclean.orbital.dto;

import jakarta.validation.constraints.NotNull;

public record MissionSimulationRequest(

        @NotNull(message = "Orbital object ID is required")
        Long orbitalObjectId

) {
}