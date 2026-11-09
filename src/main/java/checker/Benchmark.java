package checker;

import checker.model.Outpatient;
import checker.model.Qualification;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 2.2 [실험] 이중 반복문 vs 해시: 진료 청구 100,000건과 자격 이력 20,000명을 대조해
 * "자격 이력에 없는 가입자의 청구"가 몇 건인지 세는 데 걸리는 시간을 잰다.
 * (이 파일은 수정하지 않고 실행만 합니다)
 *   IntelliJ: Benchmark 클래스 옆 ▶ 실행
 *   터미널:   ./gradlew benchmark
 */
public class Benchmark {
    public static void main(String[] args) {
        List<Qualification> quals = DataLoader.loadQualifications("data/large/qualifications.csv");
        List<Outpatient> outs = DataLoader.loadOutpatients("data/large/outpatient.csv");
        System.out.printf("청구 %,d건 vs 자격 이력 %,d행%n%n", outs.size(), quals.size());

        for (int round = 1; round <= 2; round++) {
            long t = System.nanoTime();
            int nested = countWithNestedLoop(outs, quals);
            double nestedMs = (System.nanoTime() - t) / 1e6;

            t = System.nanoTime();
            int hashed = countWithHash(outs, quals);
            double hashMs = (System.nanoTime() - t) / 1e6;

            System.out.printf("[%d회차] 이중 반복문: %,.0f ms (%d건) / 해시: %.1f ms (%d건) / 배율 약 %,.0f배%n",
                    round, nestedMs, nested, hashMs, hashed, nestedMs / Math.max(hashMs, 0.001));
        }
        System.out.println("\n※ 1회차는 JVM이 아직 달궈지지 않아 더 느릴 수 있습니다. 두 방식의 결과 건수는 같아야 합니다.");
    }

    /** 청구마다 자격 이력을 처음부터 끝까지 훑는다: 청구 수 × 자격 수 */
    static int countWithNestedLoop(List<Outpatient> outs, List<Qualification> quals) {
        int missing = 0;
        for (Outpatient c : outs) {
            boolean found = false;
            for (Qualification q : quals) {
                if (q.memberId().equals(c.memberId())) { found = true; break; }
            }
            if (!found) missing++;
        }
        return missing;
    }

    /** 자격 이력을 HashSet에 담아 두고 청구마다 한 번에 확인한다 */
    static int countWithHash(List<Outpatient> outs, List<Qualification> quals) {
        Set<String> members = new HashSet<>();
        for (Qualification q : quals) members.add(q.memberId());
        int missing = 0;
        for (Outpatient c : outs) if (!members.contains(c.memberId())) missing++;
        return missing;
    }
}
