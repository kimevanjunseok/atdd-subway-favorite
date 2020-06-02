package wooteco.subway.service.member;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, jwtTokenProvider, stationRepository);
    }

    @Test
    void createMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        memberService.createMember(member);

        verify(memberRepository).save(any());
    }

    @Test
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(anyString());
    }

    @Test
    void updateMember() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        memberService.updateMember(1L, new UpdateMemberRequest("New_" + TEST_USER_NAME, "New_" + TEST_USER_PASSWORD));

        verify(memberRepository).save(any());
    }

    @Test
    void deleteMember() {
        memberService.deleteMember(1L);
        verify(memberRepository).deleteById(any());
    }

    @Test
    void findFavoriteByMemberId() {
        List<Station> stations = Lists.newArrayList(new Station(1L, "강남역"), new Station(2L, "역삼역"));
        when(stationRepository.findAllById(anySet())).thenReturn(stations);

        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(1L, 2L);

        memberService.addFavorite(member, favoriteCreateRequest);

        Set<FavoriteResponse> favoriteResponses = memberService.findFavorites(member);
        MemberFavoriteResponse memberFavoriteResponse = MemberFavoriteResponse.of(1L, favoriteResponses);

        assertThat(memberFavoriteResponse.getFavorites().size()).isEqualTo(1);
    }

    @Test
    void deleteFavoriteById() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        member.addFavorite(new Favorite(1L, 1L, 2L));

        memberService.deleteFavoriteById(member, 1L);

        Set<FavoriteResponse> favoriteResponses = memberService.findFavorites(member);
        MemberFavoriteResponse memberFavoriteResponse = MemberFavoriteResponse.of(1L, favoriteResponses);

        assertThat(memberFavoriteResponse.getFavorites().size()).isEqualTo(0);
    }
}
