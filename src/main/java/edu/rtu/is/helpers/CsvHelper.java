package edu.rtu.is.helpers;

import edu.rtu.is.Rule;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class CsvHelper {


    public static Rule toRule(String path, Map<String, Object> parameters) {

        List<CSVRecord> records = null;
        List<String> conditions = new ArrayList<>();
        HashMap<String, Float> data = new HashMap<>();
        Boolean header = true;

        // Default value
        Float valueFeature = 0.1f;
        Float valueNew = 0.01f;
        String resultColumnName = null;

        // Value from parameters
        try {
            if (parameters.containsKey("value_feature")) {
                if (!(parameters.get("value_feature") instanceof Float)) {
                    throw new IllegalArgumentException("...");
                }
                valueFeature = (Float) parameters.get("value_feature");
            }

            if (parameters.containsKey("value_new")) {
                if (!(parameters.get("value_new") instanceof Float)) {
                    throw new IllegalArgumentException("...");
                }
                valueNew = (Float) parameters.get("value_new");
            }

            if (parameters.containsKey("result_column_name")) {
                if (!(parameters.get("result_column_name") instanceof String)) {
                    throw new IllegalArgumentException("...");
                }
                resultColumnName = (String) parameters.get("result_column_name");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Wrong type of parameter");
        }

        try {
            Reader in = new FileReader(path);
            records = CSVFormat.EXCEL.parse(in).getRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (CSVRecord record : records) {
            for (int i = 0; i < record.size(); i++) {
                if(header) {
                    conditions.add(record.get(i));
                    data.put(record.get(i), valueFeature);
                    if(resultColumnName == null && i == record.size()-1) {
                        resultColumnName = record.get(i);
                    }

                } else {
                    int currentValue = Integer.parseInt(record.get(i));
                    Float previousValue =  data.get(conditions.get(i));
                    if(1 == currentValue) {
                        data.put(conditions.get(i), previousValue + (1-previousValue)*valueNew);
                    } else {
                        data.put(conditions.get(i), previousValue);
                    }
                }
            }
            if (header) {
                header = false;
            }
        }

        return new Rule(conditions, data, resultColumnName);

    }
}
