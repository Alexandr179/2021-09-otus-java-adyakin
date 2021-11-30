package ru.otus;

import java.util.*;

public class АТМ implements Banking {

    public static Map<Note, Integer> safe;
    private static Map<Note, Integer> amountMapping;

    public АТМ() {
        safe = new HashMap<>();
        amountMapping = new TreeMap<>() {{//todo: ONLY this notes..  все поля имеют значение (без проверки на NULL)
            put(Note.TEN, 10);
            put(Note.FIFTY, 50);
            put(Note.HUNDRED, 100);
            put(Note.TWO_HUNDRED, 200);
            put(Note.FIVE_HUNDRED, 500);
            put(Note.THOUSAND, 1000);
            put(Note.TWO_THOUSAND, 2000);
            put(Note.FIVE_THOUSAND, 5000);
        }};
    }

    @Override
    public void addToSafe(Map<Note, Integer> amountPutted) {
        amountPutted.forEach((key, value) -> safe.put(key, +value));
    }

    @Override
    public Map<Note, Integer> lookAtMapSafe() {
        return safe;
    }

    @Override
    public Integer lookAtIntSafe() {
        return accumulate(safe);
    }


    /**
     * допускается съем суммы кратной номиналам
     */
    @Override
    public void takeFromSafe(Integer sum) throws IllegalAccessException {
        Map<Note, Integer> safeTemp = safe;
        for (Map.Entry<Note, Integer> entryMapping : entriesSortedByValues(amountMapping)) {// ..using sorted-method by values amountMapping
            Integer valueNote = entryMapping.getValue();
            while (valueNote <= sum) {
                Integer value = safe.get(entryMapping.getKey());
                if(value >= 1) {
                    safe.put(entryMapping.getKey(), value - 1);
                    sum -= valueNote;
                } else break;
            }
        }
        if(sum > 0) {
            // todo: rollback ..не реализовано
            throw new IllegalAccessException("Error. Issue of an amount");
        }
    }


    private Integer accumulate(Map<Note, Integer> map){
        return map.entrySet().stream()
                .reduce(0, (safeEntry, safeEntryNext) ->
                                safeEntry + amountMapping.get(safeEntryNext.getKey()) * safeEntryNext.getValue(),
                        Integer::sum);
    }

    private <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {// https://ru.stackoverflow.com/questions/614718/%D0%9A%D0%B0%D0%BA-%D0%BE%D1%82%D1%81%D0%BE%D1%80%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D1%82%D1%8C-treemap-%D0%BF%D0%BE-%D0%B7%D0%BD%D0%B0%D1%87%D0%B5%D0%BD%D0%B8%D1%8E-%D0%B2-%D0%BF%D0%BE%D1%80%D1%8F%D0%B4%D0%BA%D0%B5-%D1%83%D0%B1%D1%8B%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F
        List<Map.Entry<K, V>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        return sortedEntries;
    }
}
