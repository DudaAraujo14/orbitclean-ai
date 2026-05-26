package br.com.orbitclean.orbital.controller;

import br.com.orbitclean.orbital.dto.OrbitalObjectRequest;
import br.com.orbitclean.orbital.dto.OrbitalObjectResponse;
import br.com.orbitclean.orbital.service.OrbitalObjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orbital-objects")
@RequiredArgsConstructor
public class OrbitalObjectController {

    private final OrbitalObjectService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrbitalObjectResponse create(@RequestBody @Valid OrbitalObjectRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<OrbitalObjectResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public OrbitalObjectResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public OrbitalObjectResponse update(
            @PathVariable Long id,
            @RequestBody @Valid OrbitalObjectRequest request
    ) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}