package checker.model;

import java.time.LocalDate;

/** 입원 청구 한 건. 입원일과 퇴원일을 모두 포함하는 기간입니다. */
public record Inpatient(String claimId, String memberId, String institution,
                        LocalDate admitDate, LocalDate dischargeDate, long fee) {}
