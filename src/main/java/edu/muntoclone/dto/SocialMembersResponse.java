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



        @Builder
        public SocialMember(Long memberId, String memberProfileUrl, String name, String greeting) {
            this.memberId = memberId;
            this.memberProfileUrl = memberProfileUrl;
            this.name = name;
            this.greeting = greeting;
        }

        public static SocialMember of(Member owner) {
            return SocialMember.builder()
                    .memberId(owner.getId())
                    .memberProfileUrl(owner.getProfileImageUrl())
                    .name(owner.getName())
                    .build();
        }
    }
}