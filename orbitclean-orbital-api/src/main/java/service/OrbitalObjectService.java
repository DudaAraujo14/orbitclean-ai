package br.com.orbitclean.orbital.service;

import br.com.orbitclean.orbital.domain.OrbitalObject;
import br.com.orbitclean.orbital.dto.OrbitalObjectRequest;
import br.com.orbitclean.orbital.dto.OrbitalObjectResponse;
import br.com.orbitclean.orbital.dto.OrbitalScoreResponse;
import br.com.orbitclean.orbital.repository.OrbitalObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrbitalObjectService {

    private final OrbitalObjectRepository repository;
    private final OrbitalScoringService scoringService;

    public OrbitalObjectResponse create(OrbitalObjectRequest request) {
        if (repository.existsByNoradId(request.noradId())) {
            throw new IllegalArgumentException("Orbital object with this NORAD ID already exists");
        }

        OrbitalObject object = OrbitalObject.builder()
                .noradId(request.noradId())
                .name(request.name())
                .objectType(request.objectType())
                .operationalStatus(request.operationalStatus())
                .massKg(request.massKg())
                .altitudeKm(request.altitudeKm())
                .inclinationDeg(request.inclinationDeg())
                .riskScore(0)
                .circularValueScore(0)
                .build();

        return OrbitalObjectResponse.fromEntity(repository.save(object));
    }

    public List<OrbitalObjectResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(OrbitalObjectResponse::fromEntity)
                .toList();
    }

    public OrbitalObjectResponse findById(Long id) {
        OrbitalObject object = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orbital object not found"));

        return OrbitalObjectResponse.fromEntity(object);
    }

    public OrbitalObjectResponse update(Long id, OrbitalObjectRequest request) {
        OrbitalObject object = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orbital object not found"));

        object.setNoradId(request.noradId());
        object.setName(request.name());
        object.setObjectType(request.objectType());
        object.setOperationalStatus(request.operationalStatus());
        object.setMassKg(request.massKg());
        object.setAltitudeKm(request.altitudeKm());
        object.setInclinationDeg(request.inclinationDeg());

        return OrbitalObjectResponse.fromEntity(repository.save(object));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Orbital object not found");
        }

        repository.deleteById(id);
    }

    public OrbitalScoreResponse calculateScores(Long id) {
        OrbitalObject object = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orbital object not found"));

        int riskScore = scoringService.calculateRiskScore(object);
        int circularValueScore = scoringService.calculateCircularValueScore(object);

        object.setRiskScore(riskScore);
        object.setCircularValueScore(circularValueScore);

        OrbitalObject savedObject = repository.save(object);

        return new OrbitalScoreResponse(
                savedObject.getId(),
                savedObject.getNoradId(),
                savedObject.getName(),
                savedObject.getRiskScore(),
                scoringService.classifyRisk(savedObject.getRiskScore()),
                savedObject.getCircularValueScore(),
                scoringService.classifyCircularValue(savedObject.getCircularValueScore())
        );
    }

    public List<OrbitalObjectResponse> findPriorityRanking() {
        return repository.findAll()
                .stream()
                .sorted((a, b) -> {
                    int scoreA = safeScore(a.getRiskScore()) + safeScore(a.getCircularValueScore());
                    int scoreB = safeScore(b.getRiskScore()) + safeScore(b.getCircularValueScore());

                    return Integer.compare(scoreB, scoreA);
                })
                .map(OrbitalObjectResponse::fromEntity)
                .toList();
    }

    private int safeScore(Integer score) {
        return score == null ? 0 : score;
    }
}