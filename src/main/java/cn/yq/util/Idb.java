package cn.yq.util;

import cn.yq.data.IdbQuery;
import cn.yq.restClient.RestClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.helpers.DateTimeDateFormat;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;

public class Idb {
    static String gmt_modified;
    /**
     *
     * @param dbenv   环境
     * @param dbname  数据库名
     * @param tbname  数据表
     * @param sql     sql语句
     * @throws IOException
     */
    public static void idb(String dbenv,String dbname, String tbname,String sql)throws IOException {
        CloseableHttpResponse closeableHttpResponse;
        RestClient restClient = new RestClient();
        String Url = "";
        String Cookie = "";
        String splitcol = "-1";
        String splitcolmode = "=";
        String selectmod = "1";
        String iscount = "0";
        String idb2Json ;
        //转换为Json数据
        IdbQuery idbquery = new IdbQuery(dbenv,dbname,tbname,splitcol,splitcolmode,sql,selectmod,iscount);
        idb2Json = JSON.toJSONString(idbquery);
        //请求头
        HashMap<String, String> headermap = new HashMap<String, String>();
        headermap.put("Content-Type", "application/json");
        headermap.put("Cookie", Cookie);
        closeableHttpResponse = restClient.post(Url, idb2Json, headermap);
        //验证状态码是不是200
        System.out.println(idb2Json);
        int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
        System.out.println("status:"+statusCode);
        JSONObject responseJson = restClient.getResponseJson(closeableHttpResponse);
        String s = "aa";
        int i = 0;
        while(s.length() == 2 && i<8){
            String flag ="nereus_00.nereus_message_sent_000"+i;
            s = TestUtil.getValueByJPath(responseJson, flag);

            if (s.length()>2)
            {
                JSONArray test = JSONArray.parseArray(s);
                System.out.println(test);
                int size = test.size();
                for(int j=0;j<size;j++) {
                    String index = "["+j+"]";
                    if (TestUtil.getValueByJPath(responseJson, flag + index+"/message_type").equals("TZ"))
                        //时间 和 短信内容
                      gmt_modified =  TestUtil.getValueByJPath(responseJson,flag + index+"/gmt_created");
                        String  receiver_no = TestUtil.getValueByJPath(responseJson,flag + index+"/receiver_no");
                        String message_type = TestUtil.getValueByJPath(responseJson, flag + index+"/message_type");
                        String content = TestUtil.getValueByJPath(responseJson, flag + index+"/content");
                        String SQL = "insert into messagefortest(message_type,content,receiver_no,gmt_modified) values('"+message_type+"','"+content+"','"+receiver_no+"','"+gmt_modified+"')";
                    System.out.println(SQL);
                        mysqlUtil.insert(SQL);
                        System.out.println(TestUtil.getValueByJPath(responseJson,flag + index+"/gmt_created") + TestUtil.getValueByJPath(responseJson, flag + index+"/content"));
                }
            }
            i++;
        }

    }
    public static void main(String[] args){
        try {
            idb("tst","nereus","nereus_message_sent","receiver_no ='13371913710'");
        }catch (IOException e){
            System.out.print(e.getMessage());
        }
    }
}
