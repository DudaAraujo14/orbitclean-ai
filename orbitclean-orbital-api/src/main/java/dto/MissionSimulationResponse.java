package br.com.orbitclean.orbital.dto;

public record MissionSimulationResponse(
        Long orbitalObjectId,
        Integer noradId,
        String objectName,
        Integer riskScore,
        Integer circularValueScore,
        String recommendedAction,
        Double estimatedCostUsd,
        Integer generatedCloCredits,
        String missionComplexity,
        String justification
) {
}