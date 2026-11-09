package checker.checks;

import checker.model.Outpatient;
import checker.model.Qualification;
import java.util.List;

public class NoQualificationClaims {
    // TODO [3.4] 자격 이력에 없는 가입자의 청구ID 목록을 반환
    public static List<String> find(List<Outpatient> claims, List<Qualification> quals) {
        return List.of();
    }
}
