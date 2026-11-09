package checker.model;

import java.time.LocalDate;

/** 기간. 시작일과 종료일을 모두 포함합니다. */
public record Period(LocalDate start, LocalDate end) {}
