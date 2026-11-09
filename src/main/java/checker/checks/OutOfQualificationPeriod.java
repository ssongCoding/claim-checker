package checker.checks;

import checker.model.Outpatient;
import checker.model.Qualification;
import java.util.List;

public class OutOfQualificationPeriod {
    // TODO [6.3] 자격 기간 밖에서 받은 진료의 청구ID 목록을 반환
    public static List<String> find(List<Outpatient> claims, List<Qualification> quals) {
        return List.of();
    }
}
