package client;

import java.util.regex.Pattern;

/**
 * this code reserve from flash client.
 * @author suchu
 * @since 2018/5/18 17:31
 */
public class Encode {
    public String str = "";
    private static Pattern pattern = Pattern.compile("[/@]");

    public void addItem(String key, String value) {
        String temp = "";
        temp = key != null ? this.scan_str(key) + "@=" : "";
        this.str = this.str + (temp + this.scan_str(value) + "/");
        return;
    }

    public void addItem_int(String key, int value) {
        String temp = "";
        temp = key != null ? this.scan_str(key) + "@=" : "";
        this.str = this.str + (temp + this.scan_str(String.valueOf(value)) + "/");
    }

    public String Get_SttString() {
        return this.str;
    }

    private String scan_str(String str) {
        String temp = null;
       //Matcher matcher =  pattern.matcher(str);
      // matcher.
        if (!pattern.matcher(str).lookingAt()) {
            return str;
        }
        StringBuilder temp2 = new StringBuilder();
        int index = 0;
        while (index < str.length()) {
            temp = String.valueOf(str.charAt(index));
            if ("/".equals(temp)) {
                temp2.append("@S");
            } else if ("@".equals(temp)) {
                temp2.append("@A");
            } else {
                temp2.append(temp);
            }
            index++;
        }
        return temp2.toString();
    }

    public static void main(String[] args) {
        Encode encode = new Encode();
        encode.addItem("/type", "logout");
        encode.addItem_int("@int_test", 1234);
        System.out.println(encode.Get_SttString());

        Decode decode = new Decode();
        String test = "type@=loginreq/username@=auto_DVyakJ0WI7/password@=/roomid@=276685/ltkid@=38108410/biz@=1/stk@=04b04a2bbf839a90/dfl@=sn@AA=105@ASss@AA=1/";
        decode.parse(encode.Get_SttString());
        System.out.println(decode.sItems);
        System.out.println(decode.count);
    }

}
