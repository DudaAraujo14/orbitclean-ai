package br.com.orbitclean.orbital.service;

import br.com.orbitclean.orbital.domain.OrbitalObject;
import br.com.orbitclean.orbital.dto.MissionSimulationResponse;
import br.com.orbitclean.orbital.repository.OrbitalObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionSimulationService {

    private final OrbitalObjectRepository orbitalObjectRepository;
    private final OrbitalScoringService scoringService;

    public MissionSimulationResponse simulate(Long orbitalObjectId) {
        OrbitalObject object = orbitalObjectRepository.findById(orbitalObjectId)
                .orElseThrow(() -> new IllegalArgumentException("Orbital object not found"));

        int riskScore = object.getRiskScore() != null && object.getRiskScore() > 0
                ? object.getRiskScore()
                : scoringService.calculateRiskScore(object);

        int circularValueScore = object.getCircularValueScore() != null && object.getCircularValueScore() > 0
                ? object.getCircularValueScore()
                : scoringService.calculateCircularValueScore(object);

        object.setRiskScore(riskScore);
        object.setCircularValueScore(circularValueScore);
        orbitalObjectRepository.save(object);

        String recommendedAction = defineRecommendedAction(riskScore, circularValueScore);
        Double estimatedCostUsd = calculateEstimatedCost(object, riskScore);
        Integer generatedCloCredits = calculateCloCredits(object, riskScore);
        String missionComplexity = defineMissionComplexity(object, riskScore);
        String justification = buildJustification(object, recommendedAction, riskScore, circularValueScore, generatedCloCredits);

        return new MissionSimulationResponse(
                object.getId(),
                object.getNoradId(),
                object.getName(),
                riskScore,
                circularValueScore,
                recommendedAction,
                estimatedCostUsd,
                generatedCloCredits,
                missionComplexity,
                justification
        );
    }

    private String defineRecommendedAction(int riskScore, int circularValueScore) {
        if (riskScore >= 81 && circularValueScore >= 61) {
            return "CAPTURE_AND_ORBITAL_RECYCLING";
        }

        if (riskScore >= 81) {
            return "CONTROLLED_DEORBIT";
        }

        if (riskScore >= 61 && circularValueScore >= 61) {
            return "RIDESHARE_REMOVAL_MISSION";
        }

        if (riskScore >= 31) {
            return "CONTINUOUS_MONITORING";
        }

        return "LOW_PRIORITY_MONITORING";
    }

    private Double calculateEstimatedCost(OrbitalObject object, int riskScore) {
        double baseCost = 2_000_000.0;

        double massFactor = object.getMassKg() != null
                ? object.getMassKg() * 2500
                : 500_000.0;

        double altitudeFactor = object.getAltitudeKm() != null
                ? object.getAltitudeKm() * 1200
                : 500_000.0;

        double riskFactor = riskScore * 45_000.0;

        return baseCost + massFactor + altitudeFactor + riskFactor;
    }

    private Integer calculateCloCredits(OrbitalObject object, int riskScore) {
        double massFactor = object.getMassKg() != null
                ? object.getMassKg() / 10.0
                : 10.0;

        double altitudeMultiplier = calculateAltitudeMultiplier(object.getAltitudeKm());
        double riskMultiplier = riskScore / 20.0;

        int credits = (int) Math.round(massFactor * altitudeMultiplier * riskMultiplier);

        return Math.max(credits, 1);
    }

    private double calculateAltitudeMultiplier(Double altitudeKm) {
        if (altitudeKm == null) {
            return 1.0;
        }

        if (altitudeKm >= 1000 && altitudeKm <= 2000) {
            return 1.5;
        }

        if (altitudeKm >= 600 && altitudeKm < 1000) {
            return 1.2;
        }

        if (altitudeKm >= 300 && altitudeKm < 600) {
            return 1.0;
        }

        return 0.8;
    }

    private String defineMissionComplexity(OrbitalObject object, int riskScore) {
        double altitude = object.getAltitudeKm() != null ? object.getAltitudeKm() : 0;
        double mass = object.getMassKg() != null ? object.getMassKg() : 0;

        if (riskScore >= 81 || altitude >= 1000 || mass >= 1000) {
            return "HIGH";
        }

        if (riskScore >= 61 || altitude >= 600 || mass >= 500) {
            return "MEDIUM";
        }

        return "LOW";
    }

    private String buildJustification(
            OrbitalObject object,
            String recommendedAction,
            int riskScore,
            int circularValueScore,
            int generatedCloCredits
    ) {
        return "Object " + object.getName()
                + " received ORS " + riskScore
                + " and CVS " + circularValueScore
                + ". Recommended action: " + recommendedAction
                + ". Estimated CLO generation: " + generatedCloCredits
                + " credits. The decision considers mass, altitude, operational status and circular value potential.";
    }
}