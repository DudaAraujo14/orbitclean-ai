package br.com.orbitclean.orbital.controller;

import br.com.orbitclean.orbital.dto.MissionSimulationRequest;
import br.com.orbitclean.orbital.dto.MissionSimulationResponse;
import br.com.orbitclean.orbital.service.MissionSimulationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mission-simulations")
@RequiredArgsConstructor
public class MissionSimulationController {

    private final MissionSimulationService service;

    @PostMapping
    public MissionSimulationResponse simulate(@RequestBody @Valid MissionSimulationRequest request) {
        return service.simulate(request.orbitalObjectId());
    }

    @PostMapping("/orbital-object/{id}")
    public MissionSimulationResponse simulateByObjectId(@PathVariable Long id) {
        return service.simulate(id);
    }
}