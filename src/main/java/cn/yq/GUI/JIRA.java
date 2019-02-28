package cn.yq.GUI;

import cn.yq.restClient.RestClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.yq.GUI.exportExcel.createExecel;
import static cn.yq.H2DataBase.H2Manager.searchValue;

/**
 * 中间件，接收页面传送回来的数据，进行信息处理，爬取网页信息，再将信息传到生成excel表中
 * @author RJSETTER
 * @date 2019/1/30
 */
public class JIRA {
    private static Logger logger = LoggerFactory.getLogger(JIRA.class);
    public static String GetJiraHtml(String cookie, String URL) throws IOException{
        logger.info("当前的COOKIE为:"+cookie);
        HashMap<String, String> headermap = new HashMap<String, String>();
        headermap.put("Content-Type", "application/json");
        headermap.put("Cookie", cookie);
        CloseableHttpResponse closeableHttpResponse;
        closeableHttpResponse = RestClient.get(URL,headermap);
        String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
        return responseString;
    }

    /**
     *
     * @param Cookie   通过界面输入
     * @param jiraNum  通过界面输入的JIRA需求号，需求号间以英文逗号隔开
     * @param selectedTypeList    界面传递进来的选择的分类
     * @throws SQLException
     */
    public static boolean Jira2Excel(String Cookie,String jiraNum,List<String> selectedTypeList) {
        //请求地址
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String date = df.format(new Date())+".xls";// new Date()为获取当前系统时间，也可使用当前时间戳
        String URLBASE = "";
        //Excel第一行标题
        List<String> firstLineTitle = selectedTypeList;
        //存放JIRA需求号
        List<String> JIRA = getJiraNumList(jiraNum);
        //存放JIRA需求号与需求号对应的信息
        Map<String,List<String>> MSG = new HashMap<String, List<String>>();
        for(int index=0;index<JIRA.size();index++){
            String URL = URLBASE + JIRA.get(index);
            try {
                String responseString = GetJiraHtml(Cookie,URL);
                Document doc =  Jsoup.parse(responseString);
                logger.info("JIRA号:"+JIRA.get(index)+",RESPONSE:"+responseString);
                List<String> keyValue = getJIRAInfoKey(selectedTypeList);
                //存放该需求的数据
                List<String> JIRAMsg = new LinkedList<String>();
                for(int i=0;i<keyValue.size();i++){
                    String address = "#"+keyValue.get(i);
                    JIRAMsg.add(doc.select(address).text());
                    logger.info("key:"+address+",value:"+doc.select(address).text());
                }
                MSG.put(JIRA.get(index),JIRAMsg);
            }catch (IOException e){
                logger.info("获取JIRA页面数据失败！");
                return false;
            }
        }
        createExecel(date,firstLineTitle,MSG);
        return true;
    }

    /**
     *  接收前端传回的信息，去查询数据库，找到相应的数据，填入链表返回。
     * @param selectedTypeList
     * @return
     */
    public static List<String> getJIRAInfoKey(List<String> selectedTypeList){
        //从数据库查信息，找到信息存入列中，用于后面数据输出
        List<String> key = new LinkedList<String>();
        for(int i=0;i<selectedTypeList.size();i++){
            String searchSQL= "SELECT VALUE FROM TYPE WHERE KEY ='"+selectedTypeList.get(i)+"'";
//            System.out.println(searchValue(searchSQL));
            key.add(searchValue(searchSQL));
        }
        return key;
    }

    /**
     * 获取传进来的诸如“CMOSND34,SFDS,SDFXCV,SDFS”字符串，返回一个String列表
     * @param jiraNum
     * @return
     */
    public static List<String> getJiraNumList(String jiraNum){
        List<String> JIRA = new LinkedList<String>();
        String []s = jiraNum.split(",");
        for(int i=0;i<s.length;i++){
            JIRA.add(s[i]);
        }
        return JIRA;
    }


    public static void main(String [] args) throws IOException {
        String Cookie = "";
        String URLBASE = "";

        //Excel第一行标题
        List<String> firstLineTitle = new LinkedList<String>();

        //存放JIRA需求号
        List<String> JIRA = new LinkedList<String>();

        //存放JIRA需求号与需求号对应的信息
        Map<String,List<String>> MSG = new HashMap<String, List<String>>();
        for(int index=0;index<JIRA.size();index++){
            String URL = URLBASE + JIRA.get(index);
            String responseString = GetJiraHtml(Cookie,URL);
            //System.out.println(responseString);
            Document doc =  Jsoup.parse(responseString);
            List<String> JIRAMsg = new LinkedList<String>();

            MSG.put(JIRA.get(index),JIRAMsg);
        }
        createExecel("JIRA测试",firstLineTitle,MSG);
    }
}
