package bforbank.tennis.repositories;

import bforbank.tennis.domains.entities.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository  extends JpaRepository<MatchEntity, Long> {
}
