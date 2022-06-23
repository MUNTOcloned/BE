package edu.muntoclone.service;

import edu.muntoclone.entity.Participation;
import edu.muntoclone.repository.ParticipationRepository;
import edu.muntoclone.repository.SocialRepository;
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
    //private final SocialService socialService; // 양방향 의존관계 오류 발생..
    private final SocialRepository socialRepository;

    public List<Participation> findAllBySocialId(Long id) {
        return participationRepository.findAllBySocialId(id);
    }

    @Transactional
    public void approve(Long sid, Long mid, PrincipalDetails principalDetails) {
        checkSocialOwner(sid, principalDetails);

        final Participation participation = this
                .findAllBySocialId(sid)
                .stream()
                .filter(p -> p.getMember().getId().equals(mid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Member who did not participate."));


        participation.setApprovedStatus(1);
    }

    @Transactional
    public void refuse(Long sid, Long mid, PrincipalDetails principalDetails) {
        checkSocialOwner(sid, principalDetails);
        final Participation participation = participationRepository
                .findAllBySocialId(sid)
                .stream().filter(p -> p.getMember().getId().equals(mid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("participation is not found"));

        participationRepository.delete(participation);
    }

    private void checkSocialOwner(Long sid, PrincipalDetails principalDetails) {
        final Long memberId = principalDetails.getMember().getId();
        final Long ownerId = socialRepository.findById(sid)
                .orElseThrow(() -> new IllegalArgumentException("Social does not exist."))
                .getOwner()
                .getId();

        if (!memberId.equals(ownerId))
            throw new IllegalArgumentException("You are not the owner of social.");
    }

}
