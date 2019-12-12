package edu.rtu.is;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntelligentSystem {

    private List<Rule> ruleList;
    private HashMap<String, Float> value = new HashMap<>();

    public String getResult(List<String> condition){

        // check if we have exactly matching rule

        Rule rule = ruleList.stream().filter(r -> r.exactlyTheSame(condition)).findFirst().orElse(null);
        if(Objects.nonNull(rule))
            return rule.getResult();

        List<Rule> partiallyMatch = ruleList.stream().filter(r -> r.contains(condition)).collect(Collectors.toList());


        Optional<Rule> first = partiallyMatch.stream().sorted((r, r1) -> Float.compare(r.getValue(), r1.getValue())).findFirst();

        if(first.isPresent())
            return first.get().getResult();


        return "Unable to predict the result";
    }

}
