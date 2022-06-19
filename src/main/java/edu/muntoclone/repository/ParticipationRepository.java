package edu.muntoclone.repository;

import edu.muntoclone.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    List<Participation> findAllBySocialId(Long id);
}
