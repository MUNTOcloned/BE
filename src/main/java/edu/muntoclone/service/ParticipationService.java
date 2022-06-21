package edu.muntoclone.service;

import edu.muntoclone.entity.Participation;
import edu.muntoclone.repository.ParticipationRepository;
import edu.muntoclone.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final SocialService socialService;

    public List<Participation> findAllBySocialId(Long id) {
        return participationRepository.findAllBySocialId(id);
    }

    @Transactional
    public void approve(Long sid, Long mid, PrincipalDetails principalDetails) {
        final Long memberId = principalDetails.getMember().getId();
        final Long ownerId = socialService.findById(sid).getOwner().getId();
        if (!memberId.equals(ownerId))
            throw new IllegalArgumentException("You are not the owner of social.");

        final Participation participation = this
                .findAllBySocialId(sid)
                .stream()
                .filter(p -> p.getMember().getId().equals(mid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Member who did not participate."));


        participation.setApprovedStatus(1);
    }
}
