package checker;

import checker.checks.*;
import checker.model.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 불러오기 → 점검 실행 → 리포트 출력.
 * 실행할 때 인자로 small(기본) 또는 large를 줄 수 있습니다.
 *   IntelliJ: Run > Edit Configurations > Program arguments 에 large
 *   터미널:   ./gradlew run --args=large
 */
public class Main {
    public static void main(String[] args) {
        String set = args.length > 0 ? args[0] : "small";
        String dir = "data/" + set + "/";

        List<Qualification> quals = DataLoader.loadQualifications(dir + "qualifications.csv");
        List<Outpatient> outs = DataLoader.loadOutpatients(dir + "outpatient.csv");
        List<Inpatient> ins = DataLoader.loadInpatients(dir + "inpatient.csv");
        System.out.printf("데이터 불러옴 (%s): 자격 이력 %,d행 / 진료 청구 %,d건 / 입원 청구 %,d건%n%n",
                set, quals.size(), outs.size(), ins.size());

        long start = System.nanoTime();
        List<String> dup = DuplicateClaims.find(outs);
        List<String> noQual = NoQualificationClaims.find(outs, quals);
        List<String> outPeriod = OutOfQualificationPeriod.find(outs, quals);
        List<OverlapPair> overlap = OverlappingAdmissions.find(ins);
        List<Outpatient> top10 = TopN.find(outs, 10);
        List<Suspect> similar = SimilarMembers.find(quals, 1);
        List<String> outliers = FeeOutliers.find(outs);
        List<LocalDate> spikes = SpikeDays.find(outs, 7, 3.0);
        double ms = (System.nanoTime() - start) / 1e6;

        ReportPrinter.print(outs.size(), quals, dup, noQual, outPeriod, overlap, top10, similar, outliers, spikes);
        System.out.printf("%n점검 로직 실행 시간: %.0f ms (파일 읽기 제외)%n", ms);
    }
}
