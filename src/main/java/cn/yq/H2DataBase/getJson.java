package cn.yq.H2DataBase;

import cn.yq.H2DataBase.H2Manager;
import cn.yq.restClient.RestClient;
import cn.yq.util.TestUtil;
import cn.yq.util.mysqlUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class getJson {
    public static Map<String,String>TABLE_VALUE() throws IOException {
        Map<String,String> TABLE_COMPANY = new LinkedHashMap<String, String>();
        String Cookie = "";
        String URLBASE = "";
        String ENV = "prd";
        String URL = URLBASE+ENV;
        HashMap<String, String> headermap = new HashMap<String, String>();
        headermap.put("Content-Type", "application/json");
        headermap.put("Cookie", Cookie);
        CloseableHttpResponse closeableHttpResponse;
        closeableHttpResponse = RestClient.get(URL,headermap);
        String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
//        System.out.println(responseString);
        JSONArray test = JSONArray.parseArray(responseString);
        for(int i=0;i<test.size();i++){
            JSONObject s =  test.getJSONObject(i);
            String DB_NAME =TestUtil.getValueByJPath(s,"db_name");
            String company = TestUtil.getValueByJPath(s,"company");
            String sql = "insert into env2dbname(env,db_name,company)VALUES('prd','"+DB_NAME+"','"+company+"')";
            mysqlUtil.insert(sql);
            TABLE_COMPANY.put(TestUtil.getValueByJPath(s,"db_name"),TestUtil.getValueByJPath(s,"company"));
        }
        return TABLE_COMPANY;
    }

    public static void getTABLE(Map<String,String> TABLE_COMPANY) throws IOException{
        for (Map.Entry<String, String> entry : TABLE_COMPANY.entrySet()) {
            System.out.println("正在收集数据库："+entry.getKey());
            String Cookie = "";
            String URLBASE = "";
            String COMPANY = entry.getValue();
            String TABLE = entry.getKey();
            String URL = URLBASE+TABLE;
            HashMap<String, String> headermap = new HashMap<String, String>();
            headermap.put("Content-Type", "application/json");
            headermap.put("Cookie", Cookie);
            CloseableHttpResponse closeableHttpResponse;
            closeableHttpResponse = RestClient.get(URL,headermap);
            String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
//        System.out.println(responseString);
            try{
            JSONArray test = JSONArray.parseArray(responseString);
            for(int i=0;i<test.size();i++){

                JSONObject s =  test.getJSONObject(i);
                String tb_name = TestUtil.getValueByJPath(s,"tb_name");
//                String SQL = "INSERT INTO ENV_PRD(env,db_name,company,table_name)VALUES('prd','"+TABLE+"','"+COMPANY+"','"+tb_name+"')";
//                MysqlInsert.insert(SQL);
                H2Manager.H2insert(TABLE,COMPANY,tb_name,"");
            }
            }catch (Exception e){
                System.out.println("SOMETHING WRONG!");
            }
            }
        }

    public static void main(String []args) throws IOException{
        Map<String,String> TABLE_COMPANY = TABLE_VALUE();
//        getTABLE(TABLE_COMPANY);
    }

}
