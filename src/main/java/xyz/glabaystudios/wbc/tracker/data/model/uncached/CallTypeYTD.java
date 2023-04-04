package xyz.glabaystudios.wbc.tracker.data.model.uncached;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class CallTypeYTD {

    private Map<String, Integer> yearStats = new HashMap<>();

    public void addDataForMonth(String monthSr) {
        if (yearStats.containsKey(monthSr))
            yearStats.put(monthSr, yearStats.get(monthSr) + 1);
        else
            yearStats.put(monthSr, 1);
    }

    public int getMonthData(String monthSr) {
        return Objects.isNull(yearStats.get(monthSr)) ? 0 : yearStats.get(monthSr);
    }
}
