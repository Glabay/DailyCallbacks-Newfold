package xyz.glabaystudios.dailycallbacks.services;

import org.springframework.stereotype.Service;
import xyz.glabaystudios.dailycallbacks.data.model.Callback;
import xyz.glabaystudios.dailycallbacks.data.model.uncached.CallbackFilter;

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
                if (Objects.equals(filter.getFilterByAgent(), cb.getAgent())) filteredCallbacks.add(cb);
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
}
