package controller.httprequests;

import java.util.HashMap;
import java.util.Map;

public class APIRouteConstants {



    public static final Map<Integer, String[]> STATFI_GAS_ATTRIBUTES = createStaticMap();

    private static Map<Integer, String[]> createStaticMap(){

        Map<Integer, String[]> result = new HashMap<>();

        result.put(1, new String[]{"VAR_META.CO206", "VAR_META.SO2_0", "VAR_META.NOX_1"});
        result.put(2, new String[]{"HYY_META.CO284", "HYY_META.SO284", "HYY_META.NOx84"});
        result.put(3, new String[]{"KUM_EDDY.av_c"});
        result.put(6, new String[]{"TOR_EDDY.av_c"});
        result.put(7, new String[]{"SII1_EDDY.AV_c"});
        result.put(8, new String[]{"SII2_EDDY.AV_c"});
        result.put(9, new String[]{"KVJ_EDDY.AV_c_LI72"});
        result.put(12, new String[]{"VII_EDDY.AV_c"});
        result.put(13, new String[]{"HAL_EDDY.AV_c"});

        return result;
    }
}
