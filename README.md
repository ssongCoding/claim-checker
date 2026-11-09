# claim-checker — 청구 데이터 점검기 (실습 뼈대)

"미니 프로젝트 기반 알고리즘 실전 학습" 3일 과정의 실습 프로젝트입니다. 데이터는 모두 가상입니다.
함수는 처음에 비어 있고, 날마다 하나씩 채워 점검기를 완성합니다.

## 강의 교재

[docs/교재_최종본.pdf](docs/교재_최종본.pdf) (168쪽)

## 준비

- **JDK 17** (더 높은 버전이어도 `release 17` 기준으로 컴파일됩니다)
- **IntelliJ IDEA Community**

## 프로젝트 열기 (IntelliJ)

1. 이 저장소를 zip으로 내려받아 압축을 풉니다. (Code → Download ZIP)
2. IntelliJ에서 **Open** → 압축 푼 `claim-checker` 폴더 선택 → "Trust Project"
3. 오른쪽 아래에 Gradle 불러오기가 끝날 때까지 기다립니다. (처음에는 1~2분, 인터넷 필요)
4. 프로젝트 JDK가 17인지 확인합니다. (File → Project Structure → Project SDK)

## 실행

| 하고 싶은 일 | 방법 |
|---|---|
| 점검기 실행 (small 데이터) | `src/main/java/checker/Main.java`의 ▶ 실행 |
| large 데이터로 실행 | Run → Edit Configurations → Program arguments에 `large` |
| 테스트 실행 | `src/test/java/checker/SmallDataTest.java`의 ▶ 실행 |
| 속도 비교 실험 (2장) | `src/main/java/checker/Benchmark.java`의 ▶ 실행 |

터미널에서는 `./gradlew run`, `./gradlew run --args=large`, `./gradlew test`, `./gradlew benchmark` 입니다.
(Windows는 `gradlew.bat`)

처음 `Main`을 실행하면 모든 점검 항목이 0건으로 나오고, 테스트는 **전부 실패(빨간불)가 정상**입니다.
함수를 채울 때마다 테스트가 초록불로 바뀝니다.

## 구조

```
data/
  small/   qualifications.csv  outpatient.csv  inpatient.csv   (수십 건, 눈으로 확인·테스트용)
  large/   qualifications.csv  outpatient.csv  inpatient.csv   (청구 10만 건, 속도 비교·최종 점검용)
src/main/java/checker/
  Main.java            불러오기 → 점검 실행 → 리포트 출력
  DataLoader.java      CSV 읽기 (수정 불필요)
  ReportPrinter.java   결과 출력 (수정 불필요)
  Benchmark.java       이중 반복문 vs 해시 속도 비교 (수정 불필요)
  model/               Qualification, Outpatient, Inpatient, Period, OverlapPair, Suspect
  checks/              ← 여기에 점검 함수를 채웁니다
src/test/java/checker/SmallDataTest.java   small 데이터 기준 기대 결과
```

## 채울 함수

| 클래스 | 함수 | 장 | Day |
|---|---|---|---|
| `DuplicateClaims` | 중복 청구 찾기 | 3.3 | 1 |
| `NoQualificationClaims` | 자격 없는 사람의 청구 찾기 | 3.4 | 1 |
| `ClaimsByPeriod` | 특정 기간의 청구 찾기 | 5.3 | 2 |
| `OutOfQualificationPeriod` | 자격 기간 밖 진료 찾기 | 6.3 | 2 |
| `OverlappingAdmissions` | 중복 입원 기간 찾기 | 6.4 | 2 |
| `MonthlySummary` | 월별·기간별 청구 집계 | 7.3 | 2 |
| `TopN` | 진료비 상위 N건 추출 | 9.2 | 3 |
| `SimilarMembers` | 동일인·중복 데이터 판별 | 10.2 | 3 |
| `FeeOutliers`, `SpikeDays` | 튀는 청구 찾기 | 11.3 | 3 |

## 규칙

- 날짜는 `yyyy-MM-dd`. 기간은 **시작일과 종료일을 모두 포함**합니다.
- 상실일(`endDate`)이 비어 있으면 현재 자격이 유지 중입니다.
- AI는 채팅창에서 질문·답변만 사용합니다. (사내 LLM과 같은 조건)
