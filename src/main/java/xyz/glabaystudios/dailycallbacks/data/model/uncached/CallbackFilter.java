package xyz.glabaystudios.dailycallbacks.data.model.uncached;

import lombok.Data;

@Data
public class CallbackFilter {
    public String specificDate = "";
    public String filterStartDay = "";
    public String filterEndDay = "";
    public String filterByAgent = "";
    public String filterByStatus = "";
}
