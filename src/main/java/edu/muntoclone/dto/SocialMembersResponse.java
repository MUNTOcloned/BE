package edu.muntoclone.dto;

import edu.muntoclone.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialMembersResponse {

    private SocialMember owner;
    private List<SocialMember> members;

    @Builder
    public SocialMembersResponse(SocialMember owner, List<SocialMember> members) {
        this.owner = owner;
        this.members = members;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SocialMember {
        private Long memberId;
        private String memberProfileUrl;
        private String name;
        private String greeting;

        private String answer;




        @Builder
        public SocialMember(Long memberId, String memberProfileUrl, String name, String greeting, String answer) {
            this.memberId = memberId;
            this.memberProfileUrl = memberProfileUrl;
            this.name = name;
            this.answer = answer;
            this.greeting = greeting;
        }

        public static SocialMember of(Member owner, String answer) {
            return SocialMember.builder()
                    .memberId(owner.getId())
                    .memberProfileUrl(owner.getProfileImageUrl())
                    .name(owner.getName())
                    .answer(answer)
                    .build();
        }
    }
}