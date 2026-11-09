package checker.model;

/** 같은 사람으로 의심되는 가입자 쌍과 이름의 편집 거리 */
public record Suspect(String a, String b, int distance) {}
