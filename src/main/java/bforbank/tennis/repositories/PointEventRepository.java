package bforbank.tennis.repositories;

import bforbank.tennis.domains.entities.PointEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointEventRepository extends JpaRepository<PointEventEntity, Long> {
    List<PointEventEntity> findAllByGame_IdOrderBySequenceNumberAsc(Long gameId);

}
