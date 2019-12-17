package edu.rtu.is;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

public class IntelligentSystem {

    List<CSVRecord> records;
    private List<Rule> ruleList;

    public IntelligentSystem(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }

    public static void main(String[] args) {
        List<String> headers = new IntelligentSystem(null).getHeaders();
        System.out.println("header\n" + headers);
    }

    /**
     * Calculate decision according to provided conditions, according to below sequence:
     * 1- check if there are rue that match conditions, it will return value of decision.
     * 2- check if some rules contains provided conditions, it calculate the results from those rules.
     * 3- if not match, it will return unable to calculate.
     * @param condition
     * @return
     */
    public String getResult(List<String> condition) {

        // check if we have exactly matching rule

        Rule rule = ruleList.stream().filter(r -> r.exactlyTheSame(condition)).findFirst().orElse(null);
        if (Objects.nonNull(rule))
            return rule.getResult();

        List<Rule> partiallyMatch = ruleList.stream().filter(r -> r.contains(condition)).collect(Collectors.toList());


        Optional<Rule> first = partiallyMatch.stream().sorted((r, r1) -> Float.compare(r.getValue(), r1.getValue())).findFirst();

        if (first.isPresent())
            return first.get().getResult();

        return "Unable to predict the result";
    }

    public void readFile() {

        try {
            Reader in = new FileReader("/Users/othmanm2/Documents/RTU/project/Test.csv");
            records = CSVFormat.EXCEL.parse(in).getRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (CSVRecord record : records) {
            System.out.println(record.toString());

        }
    }

    public List<String> getHeaders() {
        if (records == null)
            readFile();
        ArrayList<String> names = new ArrayList<>();
        records.get(0).iterator().forEachRemaining(names::add);
        return names;

    }

    /**
     * Return calculated value for provided parameter.
     * @param parameter
     * @return
     */
    private float calculateValue(String parameter) {
        float result = 0.0f;
        return result;
    }


}
