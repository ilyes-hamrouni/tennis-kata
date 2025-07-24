package bforbank.tennis.repositories;

import bforbank.tennis.domains.entities.SetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SetRepository extends JpaRepository<SetEntity, Long> {
    List<SetEntity> findByMatchId(Long matchId);

}
