package checker.model;

import java.time.LocalDate;

/** 자격 이력 한 행. endDate가 null이면 현재 자격이 유지 중입니다. */
public record Qualification(String memberId, String name, LocalDate birthDate,
                            LocalDate startDate, LocalDate endDate) {}
