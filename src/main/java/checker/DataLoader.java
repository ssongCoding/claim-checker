package checker;

import checker.model.Inpatient;
import checker.model.Outpatient;
import checker.model.Qualification;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/** CSV 파일을 읽어 리스트로 바꿉니다. (이 파일은 수정하지 않아도 됩니다) */
public class DataLoader {

    public static List<Qualification> loadQualifications(String path) {
        return load(path, c -> new Qualification(c[0], c[1], LocalDate.parse(c[2]),
                LocalDate.parse(c[3]), c[4].isEmpty() ? null : LocalDate.parse(c[4])));
    }

    public static List<Outpatient> loadOutpatients(String path) {
        return load(path, c -> new Outpatient(c[0], c[1], c[2], LocalDate.parse(c[3]), Long.parseLong(c[4])));
    }

    public static List<Inpatient> loadInpatients(String path) {
        return load(path, c -> new Inpatient(c[0], c[1], c[2], LocalDate.parse(c[3]),
                LocalDate.parse(c[4]), Long.parseLong(c[5])));
    }

    private static <T> List<T> load(String path, Function<String[], T> parse) {
        try {
            List<String> lines = Files.readAllLines(Path.of(path));
            List<T> rows = new ArrayList<>(lines.size());
            for (int i = 1; i < lines.size(); i++) {          // 0번째 줄은 머리글
                if (lines.get(i).isBlank()) continue;
                rows.add(parse.apply(lines.get(i).split(",", -1)));
            }
            return rows;
        } catch (IOException e) {
            throw new UncheckedIOException("파일을 읽을 수 없습니다: " + path, e);
        }
    }
}
