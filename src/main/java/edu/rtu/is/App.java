package edu.rtu.is;

import com.google.common.collect.ImmutableMap;
import edu.rtu.is.helpers.CsvHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class App {

    public static void main(String[] args) {

        /* Rule rule = CsvHelper.toRule("/Users/celestinballevre/Desktop/decision/src/main/java/edu/rtu/is/data/data.csv",
                ImmutableMap.<String, Object>of()); */

        // Example for parameters
        Rule rule = CsvHelper.toRule("/Users/celestinballevre/Desktop/decision/src/main/java/edu/rtu/is/data/data.csv",
                ImmutableMap.<String, Object>of(
                        "value_feature", 0.1f,
                        "value_new", 0.1f,
                        "result_column_name", "lazy"
                ));

        System.out.println(rule.getConditionValues());
        System.out.println(rule.getResult());
        System.out.println(rule.contains(Arrays.asList("lazy", "weather")));
    }
}
