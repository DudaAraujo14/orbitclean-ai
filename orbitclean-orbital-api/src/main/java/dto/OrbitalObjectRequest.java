package br.com.orbitclean.orbital.dto;

import jakarta.validation.constraints.*;

public record OrbitalObjectRequest(

        @NotNull(message = "NORAD ID is required")
        Integer noradId,

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Object type is required")
        String objectType,

        @NotBlank(message = "Operational status is required")
        String operationalStatus,

        @PositiveOrZero(message = "Mass must be zero or positive")
        Double massKg,

        @PositiveOrZero(message = "Altitude must be zero or positive")
        Double altitudeKm,

        @PositiveOrZero(message = "Inclination must be zero or positive")
        Double inclinationDeg

) {
}