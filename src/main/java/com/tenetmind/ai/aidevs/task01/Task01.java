package com.tenetmind.ai.aidevs.task01;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenetmind.ai.aidevs.task01.extractor.TagExtractor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dflib.DataFrame;
import org.dflib.csv.Csv;
import org.dflib.json.Json;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.dflib.Exp.$str;

@RequiredArgsConstructor
public class Task01 {

  @Getter
  private final String name = "people";
  private final TagExtractor tagExtractor;
  private final ObjectMapper mapper = new ObjectMapper();

  public List<Object> getAnswer() {
    DataFrame src = loadPeople();

    DataFrame res = src
        .colsAppend("city").merge(
            $str("birthPlace")
        )
        .rows(r -> r.get("gender", String.class).equalsIgnoreCase("M")).select()
        .rows(r -> r.get("city", String.class).equalsIgnoreCase("Grudziądz")).select()
        .colsAppend("born").merge(
            $str("birthDate").mapVal(date -> date.substring(0, 4))
        )
        .colsAppend("age").merge(
            $str("born").mapVal(Task01::toInt).mapVal(year -> 2026 - year)
        )
        .rows(r -> r.get("age", Integer.class) >= 20).select()
        .rows(r -> r.get("age", Integer.class) <= 40).select()
        .colsAppend("tags").merge(
            $str("job").mapVal(tagExtractor::extract)
        )
        .rows(r -> {
          List tags = r.get("tags", List.class);
          return nonNull(tags) && tags.contains("transport");
        }).select()
        .cols("name", "surname", "gender", "born", "city", "tags").select();

    var jsonArrayString = Json.saver().saveToString(res);

    try {
     return mapper.readValue(jsonArrayString, new TypeReference<>() {});
    } catch (Exception e) {
      throw new RuntimeException("Error parsing JSON array", e);
    }
  }

  private static DataFrame loadPeople() {
    try {
      URL resource = Task01.class.getClassLoader().getResource("people.csv");

      if (isNull(resource)) {
        throw new IllegalStateException("Classpath resource not found");
      }

      String path = Path.of(resource.toURI()).toString();
      return Csv.load(path);
    } catch (Exception e) {
      throw new RuntimeException("Error reading CSV from classpath", e);
    }
  }

  private static int toInt(Object value) {
    if (value == null) {
      return Integer.MIN_VALUE;
    }

    try {
      return Integer.parseInt(value.toString().trim());
    } catch (NumberFormatException e) {
      return Integer.MIN_VALUE;
    }
  }
}
