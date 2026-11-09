package checker.checks;

import checker.model.Outpatient;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class MonthlySummary {
    // TODO [7.3] 월별 청구 건수와 진료비 합계를 반환 (값은 {건수, 합계})
    public static Map<YearMonth, long[]> find(List<Outpatient> claims) {
        return Map.of();
    }
}
