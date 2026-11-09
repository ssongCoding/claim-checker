package checker.model;

/** 기간이 겹치는 입원 청구 두 건의 청구ID */
public record OverlapPair(String first, String second) {}
