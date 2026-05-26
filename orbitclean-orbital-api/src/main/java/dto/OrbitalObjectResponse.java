package br.com.orbitclean.orbital.dto;

import br.com.orbitclean.orbital.domain.OrbitalObject;

import java.time.LocalDateTime;

public record OrbitalObjectResponse(
        Long id,
        Integer noradId,
        String name,
        String objectType,
        String operationalStatus,
        Double massKg,
        Double altitudeKm,
        Double inclinationDeg,
        Integer riskScore,
        Integer circularValueScore,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static OrbitalObjectResponse fromEntity(OrbitalObject object) {
        return new OrbitalObjectResponse(
                object.getId(),
                object.getNoradId(),
                object.getName(),
                object.getObjectType(),
                object.getOperationalStatus(),
                object.getMassKg(),
                object.getAltitudeKm(),
                object.getInclinationDeg(),
                object.getRiskScore(),
                object.getCircularValueScore(),
                object.getCreatedAt(),
                object.getUpdatedAt()
        );
    }
}