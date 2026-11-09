package checker;

import checker.checks.*;
import checker.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * small 데이터 기준 기대 결과 테스트.
 * 처음에는 전부 실패(빨간불)하는 것이 정상이고, 함수를 채울 때마다 초록불로 바뀝니다.
 */
class SmallDataTest {
    static List<Qualification> quals;
    static List<Outpatient> outs;
    static List<Inpatient> ins;

    @BeforeAll
    static void load() {
        quals = DataLoader.loadQualifications("data/small/qualifications.csv");
        outs = DataLoader.loadOutpatients("data/small/outpatient.csv");
        ins = DataLoader.loadInpatients("data/small/inpatient.csv");
    }

    static LocalDate day(String s) { return LocalDate.parse(s); }

    static Set<String> idsOf(List<Outpatient> list) {
        return list.stream().map(Outpatient::claimId).collect(Collectors.toCollection(TreeSet::new));
    }

    // ---------- Day 1 ----------

    @Test
    @DisplayName("3.3 중복 청구: 먼저 들어온 1건은 원본으로 두고 나머지만 한 번씩 출력")
    void duplicateClaims() {
        List<String> result = DuplicateClaims.find(outs);
        assertEquals(3, result.size(), "같은 ID가 두 번 나오면 안 됩니다");
        assertEquals(Set.of("C0000002", "C0000005", "C0000006"), new TreeSet<>(result));
    }

    @Test
    @DisplayName("3.3 중복 청구: 진료비가 다르거나 기관이 다르면 중복이 아님")
    void duplicateClaimsNotFalsePositive() {
        List<String> result = DuplicateClaims.find(outs);
        assertTrue(result.contains("C0000002"), "먼저 진짜 중복부터 찾아야 합니다");
        assertFalse(result.contains("C0000008"), "같은 날 같은 기관이라도 진료비가 다르면 다른 청구");
        assertFalse(result.contains("C0000010"), "기관이 다르면 다른 청구");
    }

    @Test
    @DisplayName("3.3 중복 청구: 빈 목록이면 빈 결과")
    void duplicateClaimsEmpty() {
        assertEquals(3, DuplicateClaims.find(outs).size(), "먼저 기본 기능부터 완성하세요");
        assertTrue(DuplicateClaims.find(List.of()).isEmpty());
    }

    @Test
    @DisplayName("3.4 자격 없는 사람의 청구")
    void noQualificationClaims() {
        assertEquals(Set.of("C0000011", "C0000012"),
                new TreeSet<>(NoQualificationClaims.find(outs, quals)));
    }

    // ---------- Day 2 ----------

    @Test
    @DisplayName("5.3 특정 기간의 청구: 시작일·종료일을 모두 포함")
    void claimsByPeriod() {
        List<Outpatient> sorted = new ArrayList<>(outs);
        sorted.sort(Comparator.comparing(Outpatient::visitDate));
        assertEquals(Set.of("C0000004", "C0000005", "C0000006"),
                idsOf(ClaimsByPeriod.find(sorted, day("2026-03-12"), day("2026-03-12"))));
        assertEquals(5, ClaimsByPeriod.find(sorted, day("2026-03-12"), day("2026-03-13")).size());
        assertEquals(outs.size(), ClaimsByPeriod.find(sorted, day("2026-01-01"), day("2026-12-31")).size());
    }

    @Test
    @DisplayName("5.3 특정 기간의 청구: 시작일이 종료일보다 늦거나 범위 밖이면 빈 목록")
    void claimsByPeriodEmpty() {
        List<Outpatient> sorted = new ArrayList<>(outs);
        sorted.sort(Comparator.comparing(Outpatient::visitDate));
        assertEquals(3, ClaimsByPeriod.find(sorted, day("2026-03-12"), day("2026-03-12")).size(), "먼저 기본 기능부터 완성하세요");
        assertTrue(ClaimsByPeriod.find(sorted, day("2026-03-10"), day("2026-03-05")).isEmpty());
        assertTrue(ClaimsByPeriod.find(sorted, day("2027-01-01"), day("2027-01-31")).isEmpty());
    }

    @Test
    @DisplayName("6.3 자격 기간 밖 진료: 취득 전·상실 후·재취득 전 공백")
    void outOfQualificationPeriod() {
        assertEquals(Set.of("C0000013", "C0000014", "C0000015"),
                new TreeSet<>(OutOfQualificationPeriod.find(outs, quals)));
    }

    @Test
    @DisplayName("6.3 자격 기간 밖 진료: 취득일 당일·상실일 당일은 정상")
    void outOfQualificationBoundary() {
        List<String> result = OutOfQualificationPeriod.find(outs, quals);
        assertTrue(result.contains("C0000013"), "먼저 진짜 자격 기간 밖 진료부터 찾아야 합니다");
        assertFalse(result.contains("C0000016"), "취득일 당일은 자격 있음");
        assertFalse(result.contains("C0000017"), "상실일 당일은 자격 있음");
        assertFalse(result.contains("C0000018"), "공백 직전 날은 자격 있음");
        assertFalse(result.contains("C0000019"), "재취득 당일은 자격 있음");
    }

    @Test
    @DisplayName("6.4 중복 입원 기간: 퇴원일 = 다음 입원일도 겹침, 하루 떨어지면 겹치지 않음")
    void overlappingAdmissions() {
        List<OverlapPair> result = OverlappingAdmissions.find(ins);
        Set<Set<String>> pairs = result.stream()
                .map(p -> Set.of(p.first(), p.second()))
                .collect(Collectors.toSet());
        assertEquals(2, result.size());
        assertEquals(Set.of(Set.of("H0000001", "H0000002"), Set.of("H0000003", "H0000004")), pairs);
    }

    @Test
    @DisplayName("7.3 월별 집계: 건수와 진료비 합계")
    void monthlySummary() {
        Map<YearMonth, long[]> m = MonthlySummary.find(outs);
        assertEquals(3, m.size());
        assertArrayEquals(new long[]{10, 113_100}, m.get(YearMonth.of(2026, 2)));
        assertArrayEquals(new long[]{45, 2_984_000}, m.get(YearMonth.of(2026, 3)));
        assertArrayEquals(new long[]{1, 11_000}, m.get(YearMonth.of(2026, 4)));
        long totalCount = 0, totalFee = 0;
        for (long[] v : m.values()) { totalCount += v[0]; totalFee += v[1]; }
        assertEquals(outs.size(), totalCount);
        assertEquals(outs.stream().mapToLong(Outpatient::fee).sum(), totalFee);
    }

    // ---------- Day 3 ----------

    @Test
    @DisplayName("9.2 진료비 상위 N건: 진료비 큰 순서")
    void topN() {
        List<Outpatient> top = TopN.find(outs, 3);
        assertEquals(3, top.size());
        assertEquals("C0000020", top.get(0).claimId());
        assertEquals(2_350_000, top.get(0).fee());
        assertTrue(top.get(0).fee() >= top.get(1).fee() && top.get(1).fee() >= top.get(2).fee());
    }

    @Test
    @DisplayName("9.2 진료비 상위 N건: N이 전체보다 크거나 0 이하")
    void topNLimits() {
        assertEquals(outs.size(), TopN.find(outs, 1000).size());
        assertTrue(TopN.find(outs, 0).isEmpty());
    }

    @Test
    @DisplayName("10.2 동일인 의심 쌍: 생년월일이 같고 이름이 한 글자 이하로 다름")
    void similarMembers() {
        List<Suspect> result = SimilarMembers.find(quals, 1);
        Set<Set<String>> pairs = result.stream().map(s -> Set.of(s.a(), s.b())).collect(Collectors.toSet());
        assertEquals(2, result.size());
        assertEquals(Set.of(Set.of("M000001", "M000031"), Set.of("M000023", "M000032")), pairs);
    }

    @Test
    @DisplayName("11.3 튀는 청구: 진료비 이상치")
    void feeOutliers() {
        assertEquals(List.of("C0000020"), FeeOutliers.find(outs));
    }

    @Test
    @DisplayName("11.3 청구 급증일: 직전 7일 평균의 3배를 넘는 날")
    void spikeDays() {
        assertEquals(List.of(day("2026-03-25")), SpikeDays.find(outs, 7, 3.0));
    }
}
