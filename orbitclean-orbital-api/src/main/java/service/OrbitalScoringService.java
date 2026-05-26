package br.com.orbitclean.orbital.service;

import br.com.orbitclean.orbital.domain.OrbitalObject;
import org.springframework.stereotype.Service;

@Service
public class OrbitalScoringService {

    public int calculateRiskScore(OrbitalObject object) {
        int altitudeScore = calculateAltitudeRisk(object.getAltitudeKm());
        int massScore = calculateMassRisk(object.getMassKg());
        int typeScore = calculateTypeRisk(object.getObjectType());
        int statusScore = calculateStatusRisk(object.getOperationalStatus());
        int inclinationScore = calculateInclinationRisk(object.getInclinationDeg());

        int total = altitudeScore + massScore + typeScore + statusScore + inclinationScore;

        return Math.min(total, 100);
    }

    public int calculateCircularValueScore(OrbitalObject object) {
        int typeScore = calculateTypeCircularValue(object.getObjectType());
        int massScore = calculateMassCircularValue(object.getMassKg());
        int statusScore = calculateStatusCircularValue(object.getOperationalStatus());
        int altitudeScore = calculateAltitudeCircularValue(object.getAltitudeKm());

        int total = typeScore + massScore + statusScore + altitudeScore;

        return Math.min(total, 100);
    }

    public String classifyRisk(int score) {
        if (score <= 30) return "LOW";
        if (score <= 60) return "MEDIUM";
        if (score <= 80) return "HIGH";
        return "CRITICAL";
    }

    public String classifyCircularValue(int score) {
        if (score <= 20) return "NEGLIGIBLE";
        if (score <= 40) return "LOW";
        if (score <= 60) return "MEDIUM";
        if (score <= 80) return "HIGH";
        return "STRATEGIC";
    }

    private int calculateAltitudeRisk(Double altitudeKm) {
        if (altitudeKm == null) return 5;

        if (altitudeKm >= 1000 && altitudeKm <= 2000) return 25;
        if (altitudeKm >= 600 && altitudeKm < 1000) return 30;
        if (altitudeKm >= 300 && altitudeKm < 600) return 15;

        return 10;
    }

    private int calculateMassRisk(Double massKg) {
        if (massKg == null) return 5;

        if (massKg >= 1000) return 20;
        if (massKg >= 500) return 15;
        if (massKg >= 100) return 10;

        return 5;
    }

    private int calculateTypeRisk(String objectType) {
        if (objectType == null) return 5;

        return switch (objectType.toUpperCase()) {
            case "ROCKET_BODY" -> 20;
            case "INACTIVE_SATELLITE" -> 18;
            case "FRAGMENT" -> 12;
            case "MISSION_RELATED_OBJECT" -> 10;
            case "ACTIVE_SATELLITE" -> 5;
            default -> 8;
        };
    }

    private int calculateStatusRisk(String status) {
        if (status == null) return 5;

        return switch (status.toUpperCase()) {
            case "ABANDONED" -> 20;
            case "INOPERATIVE" -> 18;
            case "UNCONTROLLED" -> 20;
            case "DECOMMISSIONED" -> 15;
            case "ACTIVE" -> 5;
            default -> 8;
        };
    }

    private int calculateInclinationRisk(Double inclinationDeg) {
        if (inclinationDeg == null) return 5;

        if (inclinationDeg >= 90) return 10;
        if (inclinationDeg >= 50) return 8;

        return 5;
    }

    private int calculateTypeCircularValue(String objectType) {
        if (objectType == null) return 5;

        return switch (objectType.toUpperCase()) {
            case "ROCKET_BODY" -> 35;
            case "INACTIVE_SATELLITE" -> 30;
            case "MISSION_RELATED_OBJECT" -> 20;
            case "ACTIVE_SATELLITE" -> 15;
            case "FRAGMENT" -> 5;
            default -> 10;
        };
    }

    private int calculateMassCircularValue(Double massKg) {
        if (massKg == null) return 5;

        if (massKg >= 1000) return 30;
        if (massKg >= 500) return 22;
        if (massKg >= 100) return 12;

        return 5;
    }

    private int calculateStatusCircularValue(String status) {
        if (status == null) return 5;

        return switch (status.toUpperCase()) {
            case "ABANDONED", "INOPERATIVE", "DECOMMISSIONED" -> 20;
            case "UNCONTROLLED" -> 10;
            case "ACTIVE" -> 5;
            default -> 8;
        };
    }

    private int calculateAltitudeCircularValue(Double altitudeKm) {
        if (altitudeKm == null) return 5;

        if (altitudeKm >= 600 && altitudeKm <= 2000) return 15;
        if (altitudeKm >= 300 && altitudeKm < 600) return 10;

        return 5;
    }
}