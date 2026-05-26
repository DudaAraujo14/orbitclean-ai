package br.com.orbitclean.orbital.dto;

public record OrbitalScoreResponse(
        Long id,
        Integer noradId,
        String name,
        Integer riskScore,
        String riskLevel,
        Integer circularValueScore,
        String circularValueLevel
) {
}