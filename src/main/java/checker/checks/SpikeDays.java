package checker.checks;

import checker.model.Outpatient;
import java.time.LocalDate;
import java.util.List;

public class SpikeDays {
    // TODO [11.3] 그날 청구 건수가 직전 window일 평균의 factor배를 넘는 날짜 목록을 반환
    public static List<LocalDate> find(List<Outpatient> claims, int window, double factor) {
        return List.of();
    }
}
