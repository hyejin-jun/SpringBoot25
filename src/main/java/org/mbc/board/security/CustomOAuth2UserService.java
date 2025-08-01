package org.mbc.board.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.domain.Member;
import org.mbc.board.domain.MemberRole;
import org.mbc.board.repository.MemberRepository;
import org.mbc.board.security.dto.MemberSecurityDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final InMemoryClientRegistrationRepository clientRegistrationRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("유저리퀘스트으으으.................");
        log.info(userRequest);


        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        log.info("NAME " + clientName);


       /* paramMap.forEach((k, v) -> {
            log.info("=======================================");
            log.info(k + ":" + v);  // 객체 출력
        });*/

        String email = null;

        switch (clientName) {
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;

            case "Naver":

            case "Google":
                break;
        }

        log.info("=================================");
        log.info("EMAIL: " + email);
        log.info("===================================");
        return oAuth2User;
        //return super.loadUser(userRequest);
    }

    private MemberSecurityDTO generateDTO(String email, Map<String, Object> params){

        Optional<Member> result = memberRepository.findByEmail(email);

        //데이터베이스에 해당 이메일을 사용자가 없다면
        if(result.isEmpty()){
            //회원 추가 -- mid는 이메일 주소/ 패스워드는 1111
            Member member = Member.builder()
                    .mid(email)
                    .mpw(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            //MemberSecurityDTO 구성 및 반환
            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(email, "1111",email,false, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProps(params);

            return memberSecurityDTO;
        }else {
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(
                            member.getMid(),
                            member.getMpw(),
                            member.getEmail(),
                            member.isDel(),
                            member.isSocial(),
                            member.getRoleSet()
                                    .stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name()))
                                    .collect(Collectors.toList())
                    );

            return memberSecurityDTO;
    }
}



    private String getKakaoEmail(Map<String, Object> paramMAp) {
        log.info("메서드 실행ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ");
        log.info("카카오 로그인 완 ㅋ");

        Object value = paramMAp.get("kakao_account");

        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String) accountMap.get("email");

        log.info("찾아온 메일 주소오오오: "  + email);

        return email;
    }



    }