package cn.yq;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Json工具类
 * 钰丰 2018/2/5
 */
public class JsonUtil {

    public static List<String> list=new LinkedList<String>();

    /**
     * 核心部分
     * @param jsonObject
     */
    public static void getAllKey(JSONObject jsonObject){
        Iterator<String> keys=jsonObject.keys();
        while(keys.hasNext()){
            String key=keys.next();
            if(isJsonObject(jsonObject.get(key).toString())){
                if(!key.equals("properties") && !isArrayOrObject(jsonObject.get(key).toString())) {
                    System.out.println(key);
                }
                JSONObject innerObject=JSONObject.fromObject(jsonObject.get(key));
                getAllKey(innerObject);
            }
        }
    }

    /**
     * 从未知的JsonArray中获取LinkedList
     * @return
     */
    public static LinkedList<LinkedList<String>> getLinkedListFromJsonArray(String jsonArrayStr){

        LinkedList<LinkedList<String>> linkedList=null;
        if(jsonArrayStr!=null && jsonArrayStr.length()>0){
            JSONArray jsonArray=JSONArray.fromObject(jsonArrayStr);
            linkedList=new LinkedList<LinkedList<String>>();
            for(int i=0;i<jsonArray.size();i++){
                JSONArray array=JSONArray.fromObject(jsonArray.get(i));
                LinkedList<String> internalList=new LinkedList<String>();
                for(int j=0;j<array.size();j++){
                    internalList.add(array.get(j).toString());
                }
                linkedList.add(internalList);
            }
        }
        return linkedList;
    }

    /**
     * 判断某个Json字符串是否为一个标准的Json字符串
     * @param jsonString
     * @return
     */
    public static Boolean isJsonObject(String jsonString){
        try{
            JSONObject.fromObject(jsonString);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 判断某个Json字符串是否为一个Json数组
     * @param jsonObject
     * @return
     */
    public static Boolean isArrayOrObject(String jsonObject){
        String type=JSONObject.fromObject(jsonObject).get("type").toString();
        if(type.equals("object") || type.equals("array")){
            return true;
        }else{
            return false;
        }
    }
    public static void main(String[] args){
        JSONObject jsonObject=JSONObject.fromObject("{\"type\": \"object\", \"properties\": {}}");
        JsonUtil.getAllKey(jsonObject);
    }

}