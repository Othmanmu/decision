package edu.rtu.is;

import java.io.BufferedReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class IntelligentSystem {

    CSVParser records;
    private List<Rule> ruleList = new ArrayList();
    private HashMap<String, Float> values = new HashMap();
    Rule  matchedRule;
    List<Rule> pMatchedRules = new ArrayList();

    String filePath;
    String rFilePath;

    public IntelligentSystem(String filePath, String rFilePath) {
        this.filePath = filePath;
        this.rFilePath = rFilePath;
        pMatchedRules.clear();

        initialize();
    }

    public void initialize() {
        List<String> headers = getHeaders();

        // calulate values
        headers.stream().forEach(h -> {
            Float value = calculateValue(h);
            values.put(h, value);
        });
        System.out.println(values);
        // create rules;
        try {
            BufferedReader br = new BufferedReader(new FileReader(rFilePath));
            String st;
            while ((st = br.readLine()) != null) {
                String conditionsString = st.split("-")[0];
                List<String> conditions = Arrays.asList(conditionsString.split(","));
                String result = st.split("-")[1];
                ruleList.add(new Rule(conditions, values, result));
            }

            System.out.println("Values : " + values);
            System.out.println("Rules : " + ruleList);

        } catch (Exception e) {
            Logger.getLogger(IntelligentSystem.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void main(String[] args) throws IOException {
        IntelligentSystem is = new IntelligentSystem("/Users/othmanm2/Documents/data.csv", "/Users/othmanm2/Documents/rules.txt");
        is.initialize();
        System.out.println("Result : \n" + is.getResult(Arrays.asList("F1", "F2")));

    }

    public void setRuleList(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }

    /**
     * Calculate decision according to provided conditions, according to below
     * sequence: 1- check if there are rue that match conditions, it will return
     * value of decision. 2- check if some rules contains provided conditions,
     * it calculate the results from those rules. 3- if not match, it will
     * return unable to calculate.
     *
     * @param condition
     * @return
     */
    public String getResult(List<String> condition) {

        // check if we have exactly matching rule then find first.
        Rule rule = ruleList.stream().filter(r -> r.exactlyTheSame(condition)).findFirst().orElse(null);

        if (Objects.nonNull(rule)) {
            matchedRule = rule;
            return rule.getResult();
        }

        // check partially matched rules.
        List<Rule> partiallyMatch = ruleList.stream().filter(r -> r.contains(condition)).collect(Collectors.toList());
        pMatchedRules = partiallyMatch;

        // Getting the maximum values of partially matched.
        Optional<Rule> first = partiallyMatch.stream().sorted((r, r1) -> Float.compare(r.getValue(), r1.getValue())).findFirst();

        if (first.isPresent()) {
            return first.get().getResult();
        }

        return "Unable to predict the result";
    }

    public CSVParser load() throws IOException {
        File csvData = new File(filePath);
        return CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.DEFAULT.withFirstRecordAsHeader());
    }

    public CSVParser getRecords() {
        try {
            return load();
        } catch (IOException ex) {
            Logger.getLogger(IntelligentSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Returns CVS file readers, to be used to load rules conditions.
     *
     * @return
     */
    public List<String> getHeaders() {
        try {
            return load().getHeaderNames();
        } catch (IOException ex) {
            Logger.getLogger(IntelligentSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.EMPTY_LIST;
    }

    public List<Rule> getRules() {
        return ruleList;
    }

    public void reset(){
        pMatchedRules.clear();
        matchedRule = null;
    }

    /**
     * Return calculated value for provided parameter.
     *
     * @param parameter
     * @return
     */
    public float calculateValue(String parameter) {

        float defaultValue = 0.5f;
        float defaultFactor = 0.1f;
        float currentValue = defaultValue;

        float nextValue = 0f;
        try {
            records = load();
        } catch (IOException ex) {
            Logger.getLogger(IntelligentSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (CSVRecord record : records) {

            // skip all rows that is not marked with X
            if (!"X".equals(record.get(parameter))) {
                continue;
            }

            nextValue = currentValue + (1 - currentValue) * defaultFactor;
            currentValue = nextValue;

        }
        return nextValue;
    }

    public Rule getMatchedRule(){
        return matchedRule;
    }

    public List<Rule> getPartiallyMatchedRules(){
        return pMatchedRules;
    }
}
