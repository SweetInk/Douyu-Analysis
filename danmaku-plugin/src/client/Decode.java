package client;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * this code reserve from flash client.
 * @author suchu
 * @since 2018/5/18 17:45
 */
public class Decode {
    Map<Object, String> sItems;
    int count;
    String rawString;

    public Decode() {
        this.sItems = new LinkedHashMap();
    }

    public int getCount() {
        return this.count;
    }

    public String rawString() {
        return this.rawString;
    }

    public void parse(String src) {
        String loc7;
        String loc8;
        if (!"/".equals(String.valueOf(src.charAt(src.length() - 1)))) {
            src += "/";
        }
        this.rawString = src;
        StringBuilder loc2 = new StringBuilder(512);
        String loc3 = "";
        int loc4 = src.length();
        int loc5 = 0;
        int loc6 = 0;
        while (loc6 < loc4) {
            loc7 = src.charAt(loc6) + "";
            if ("/".equals(loc7)) {
                Object key = loc3.length() > 0 ? loc3 : loc5;
                this.sItems.put(key, loc2.toString());
                loc2 = new StringBuilder(512);
                loc3 = "";
                loc5++;
            } else if ("@".equals(loc7)) {
                loc6++;
                loc8 = src.charAt(loc6) + "";
                if ("=".equals(loc8)) {
                    loc3 = loc2.toString();
                    loc2 = new StringBuilder(512);
                } else if ("A".equals(loc8)) {
                    loc2.append("@");
                } else if ("S".equals(loc8)) {
                    loc2.append("/");
                }

            } else {
                loc2.append(loc7);
            }
            loc6++;
        }
        this.count = loc5;
    }

    public String getItem(String key) {
        Object result = this.sItems.get(key);
        return result != null ? result.toString() : "";
    }

    public int getItemAsInt(String key) {
        Object result = this.sItems.get(key);
        return result != null ? Integer.parseInt(result.toString()) : 0;
    }

    public String getItemByIndex(int index) {
        Object result = this.sItems.get(index);
        return result != null ? result.toString() : "";
    }

    public boolean hasKey(String key) {
        return this.sItems.containsKey(key);
    }

}
