package xyz.glabaystudios.wbc.tracker.services;

import org.springframework.stereotype.Service;
import xyz.glabaystudios.wbc.tracker.data.model.Callback;
import xyz.glabaystudios.wbc.tracker.data.model.uncached.CallbackFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AdminService {

    public List<Callback> getFilteredCallbacks(CallbackFilter filter, List<Callback> callbacks) {
        List<Callback> filteredCallbacks = new ArrayList<>();
        for (Callback cb : callbacks) {
            // Check if the SpecificDate field is populated
            if (Objects.nonNull(filter.getSpecificDate()) && !filter.getSpecificDate().isBlank()) {
                if (Objects.equals(filter.getSpecificDate(), cb.getDateOfCallback())) filteredCallbacks.add(cb);
            }
            // Check if the Specific Agent field is populated
            else if (Objects.nonNull(filter.getFilterByAgent()) && !filter.getFilterByAgent().isBlank()) {
                // if the specific agent has a callback, add it to the list
                if (Objects.equals(filter.getFilterByAgent(), cb.getAgent())) filteredCallbacks.add(cb);
                // if the specific agent has a callback and we're looking for complete or incomplete
                if (Objects.nonNull(filter.getFilterByStatus()) && !filter.getFilterByStatus().isBlank()) {
                    if (Objects.equals(filter.getFilterByStatus().toUpperCase(),"COMPLETE")) {
                        // Looking for completed cases
                        if (Objects.equals(cb.getCompleted(), Boolean.TRUE)) filteredCallbacks.add(cb);
                    }
                    if (Objects.equals(filter.getFilterByStatus().toUpperCase(),"INCOMPLETE")) {
                        // Looking for incomplete cases
                        if (Objects.equals(cb.getCompleted(), Boolean.FALSE)) filteredCallbacks.add(cb);
                    }
                }
            }
            // Check if the start date is not null or empty
            else if (Objects.nonNull(filter.getFilterStartDay()) && !filter.getFilterStartDay().isBlank()) {
                if (isCallbacksBetweenDates(filter.getFilterStartDay(), filter.getFilterEndDay(), cb.getDateOfCallback()))
                    filteredCallbacks.add(cb);
            }
            // Check if the Completed Status field is populated
            else if (Objects.nonNull(filter.getFilterByStatus()) && !filter.getFilterByStatus().isBlank()) {
                if (Objects.equals(filter.getFilterByStatus().toUpperCase(),"COMPLETE")) {
                    // Looking for completed cases
                    if (Objects.equals(cb.getCompleted(), Boolean.TRUE)) filteredCallbacks.add(cb);
                }
                if (Objects.equals(filter.getFilterByStatus().toUpperCase(),"INCOMPLETE")) {
                    // Looking for incomplete cases
                    if (Objects.equals(cb.getCompleted(), Boolean.FALSE)) filteredCallbacks.add(cb);
                }
            }
        }
        return filteredCallbacks;
    }


    private boolean isCallbacksBetweenDates(String startingDate, String endingDate, String callbackDate) {
        int startingDateYear = Integer.parseInt(startingDate.split("-")[0]);
        int startingDateMonth = Integer.parseInt(startingDate.split("-")[1]);
        int startingDateDay = Integer.parseInt(startingDate.split("-")[2]);

        int endingDateYear = Integer.parseInt(endingDate.split("-")[0]);
        int endingDateMonth = Integer.parseInt(endingDate.split("-")[1]);
        int endingDateDay = Integer.parseInt(endingDate.split("-")[2]);

        int callbackYear = Integer.parseInt(callbackDate.split("-")[0]);
        int callbackMonth = Integer.parseInt(callbackDate.split("-")[1]);
        int callbackDay = Integer.parseInt(callbackDate.split("-")[2]);

        // Are we within the year
        if (callbackYear >= startingDateYear && callbackYear <= endingDateYear)
            // Are we within the Month range
            if (callbackMonth >= startingDateMonth && callbackMonth <= endingDateMonth)
                // are we within the Day Range
                return (callbackDay >= startingDateDay && callbackDay <= endingDateDay);
            else return false;
        else return false;
    }

}
