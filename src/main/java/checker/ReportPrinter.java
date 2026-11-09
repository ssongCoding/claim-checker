package checker;

import checker.model.OverlapPair;
import checker.model.Outpatient;
import checker.model.Qualification;
import checker.model.Suspect;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 점검 결과를 한 화면 리포트로 출력합니다. (이 파일은 수정하지 않아도 됩니다) */
public class ReportPrinter {

    public static void print(int claimCount, List<Qualification> quals,
                             List<String> dup, List<String> noQual, List<String> outPeriod,
                             List<OverlapPair> overlap, List<Outpatient> top10,
                             List<Suspect> similar, List<String> outliers, List<LocalDate> spikes) {
        Set<String> members = new HashSet<>();
        for (Qualification q : quals) members.add(q.memberId());

        System.out.printf("[점검 결과] 청구 %,d건 / 가입자 %,d명%n", claimCount, members.size());
        System.out.println("- 중복 청구: " + dup.size() + "건");
        System.out.println("- 자격 없는 사람의 청구: " + noQual.size() + "건");
        System.out.println("- 자격 기간 밖 진료: " + outPeriod.size() + "건");
        System.out.println("- 중복 입원 기간: " + overlap.size() + "건");
        if (top10.isEmpty()) {
            System.out.println("- 진료비 상위 10건: 0건");
        } else {
            System.out.printf("- 진료비 상위 10건: %,d원 ~ %,d원%n",
                    top10.get(0).fee(), top10.get(top10.size() - 1).fee());
        }
        System.out.println("- 동일인 의심 쌍: " + similar.size() + "쌍");
        System.out.println("- 이상 청구: " + outliers.size() + "건");
        System.out.println("- 청구 급증일: " + spikes.size() + "일" + (spikes.isEmpty() ? "" : " " + spikes));
    }
}
