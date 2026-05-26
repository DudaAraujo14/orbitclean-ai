package br.com.orbitclean.orbital.repository;

import br.com.orbitclean.orbital.domain.OrbitalObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrbitalObjectRepository extends JpaRepository<OrbitalObject, Long> {

    Optional<OrbitalObject> findByNoradId(Integer noradId);

    boolean existsByNoradId(Integer noradId);
}