package edu.rtu.is;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Rule {
    List<String> conditions;
    String result;

    HashMap<String, Float> conditionValues;

    public Rule(List<String> conditions, HashMap<String, Float> conditionValues, String result) {
        this.conditions = conditions;
        this.conditionValues = conditionValues;
        this.result = result;
    }

    public static void main(String[] args) {
        List<String> conditions = Arrays.asList("A", "B", "C");
        HashMap<String, Float> data = new HashMap<>();
        data.put("A", 3.45544f);
        data.put("B", 10.45544f);
        data.put("C", 9.33434f);
        data.put("Y", 100f);

        Rule rule = new Rule(conditions, data, "Y");
        System.out.println(rule.getValue());

        System.out.println(rule.contains(Arrays.asList("B", "A")));
    }

    private List<String> getConditions() {
        return conditions;
    }

    public String getResult() {
        return result;
    }

    public float getValue() {
        float min = Float.MAX_VALUE;
        for (String condition : conditions) {
            if (min > conditionValues.get(condition))
                min = conditionValues.get(condition);
        }
        return min * conditionValues.get(result);
    }

    public boolean exactlyTheSame(List<String> cond) {
        Collections.sort(cond);
        Collections.sort(conditions);
        return conditions.equals(cond);
    }

    public boolean contains(List<String> cond) {
        return conditions.containsAll(cond);
    }

}
