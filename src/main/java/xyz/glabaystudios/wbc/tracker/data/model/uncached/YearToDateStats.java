package xyz.glabaystudios.wbc.tracker.data.model.uncached;

import java.util.*;

public class YearToDateStats {
    private Map<String, CallTypeYTD> cachedCallTypeList;

    public Map<String, CallTypeYTD> getCallTypeList() {
        if (Objects.isNull(cachedCallTypeList)) cachedCallTypeList = new HashMap<>();
        return cachedCallTypeList;
    }

    public void updateCallType(String callbackType, String month) {
        if (getCallTypeList().containsKey(callbackType))
            getCallTypeList().get(callbackType).addDataForMonth(month);
        else {
            CallTypeYTD dataObject = new CallTypeYTD();
            dataObject.addDataForMonth(month);
            getCallTypeList().put(callbackType, dataObject);
        }
    }
}
