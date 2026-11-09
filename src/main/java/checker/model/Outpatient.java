package checker.model;

import java.time.LocalDate;

/** 진료 청구 한 건 */
public record Outpatient(String claimId, String memberId, String institution,
                         LocalDate visitDate, long fee) {}
