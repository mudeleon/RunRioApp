package tip.edu.ph.runrio.util;

import java.util.Map;

/**
 * Created by paulolorenzobasilio on 27/04/2017.
 */

public class MapNullChecker {
    /**
     * Check if all values are null
     *
     * @param aMap - args hash map
     * @param <K>  - key
     * @param <V>  - value
     * @return
     */
    public static <K, V> boolean isMapEmpty(Map<K, V> aMap) {
        for (V v : aMap.values()) {
            if (v == "") {
                return true;
            }
        }
        return false;
    }
}
