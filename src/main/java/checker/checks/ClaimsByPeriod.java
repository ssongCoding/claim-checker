package checker.checks;

import checker.model.Outpatient;
import java.time.LocalDate;
import java.util.List;

public class ClaimsByPeriod {
    // TODO [5.3] 시작일~종료일(포함)에 진료받은 청구 목록을 반환. 입력은 진료일 순으로 정렬된 목록
    public static List<Outpatient> find(List<Outpatient> sorted, LocalDate from, LocalDate to) {
        return List.of();
    }
}
