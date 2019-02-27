import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TEST {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://u-uat.zhongan.com/?business_id=10002537697010&utm_source=ZhiyingAPP&auto_test=true&auto_payment=false");
        URLConnection conn = url.openConnection();
        Map<String, List<String>> headers = conn.getHeaderFields();
        Set<String> keys = headers.keySet();
        for (String key : keys) {
            if ("Location".equals(key)) {
                String val = conn.getHeaderField(key);
                System.out.println(key + "    " + val);
                String[] s = val.split("\\=|&");
                System.out.println(s[1]);
            }
        }

    }
}