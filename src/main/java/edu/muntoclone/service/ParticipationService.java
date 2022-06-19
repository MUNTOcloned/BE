package edu.muntoclone.service;

import edu.muntoclone.entity.Participation;
import edu.muntoclone.repository.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationService {

    private final ParticipationRepository participationRepository;

    public List<Participation> findAllBySocialId(Long id) {
        return participationRepository.findAllBySocialId(id);
    }
}
