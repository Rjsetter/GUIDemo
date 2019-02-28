package cn.yq.GUI;

import cn.yq.H2DataBase.H2Manager;
import cn.yq.restClient.RestClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

/**
 * 后台管理
 * @author Rjsetter
 * @date 2019/2/15
 */
public class BEPanel {
    //日志
    private static Logger logger = LoggerFactory.getLogger(BEPanel.class);
    //声明面板
    static JPanel BEPanel = new JPanel();
    //显示文本
    static JLabel
        jiraLabel = new JLabel("JIRA的Cookie："),
        idbLabel = new JLabel("IDB的Cookie:");
    //文本输入框
    static JTextField
        jiraText = new JTextField(),
        idbText = new JTextField();
    //按钮
    static Button
        jiraButton =new Button("UPDATE"),
        idbButton = new Button("UPDATE");

    /***
     * 创建后台的面板并返回
     * @return
     */
    public static JPanel getBEPanel(){
        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        BEPanel.setLayout(null);

        /**
         * jira的cookie更新
         */
        jiraLabel.setBounds(10,20,100,25);
        BEPanel.add(jiraLabel);
        //创建文本域用于用户输入
        jiraText.setBounds(120,20,200,25);
        String showMSG = "在这输入你需要更新的Cookie...";
        jiraText.setFont(new Font("黑体", Font.PLAIN, 13));
        jiraText.addFocusListener(new DefaultTextUtil.JTextFieldHintListener(jiraText, showMSG));
        BEPanel.add(jiraText);
        jiraButton.setBounds(330,20,80,25);
        BEPanel.add(jiraButton);

        /**
         *  idb的cookie更新
         */
        idbLabel.setBounds(10,50,100,25);
        BEPanel.add(idbLabel);
        //创建文本域用于用户输入
            idbText.setBounds(120,50,200,25);
        idbText.setFont(new Font("黑体", Font.PLAIN, 13));
        idbText.addFocusListener(new DefaultTextUtil.JTextFieldHintListener(idbText, showMSG));
        BEPanel.add(idbText);
        idbButton.setBounds(330,50,80,25);
        BEPanel.add(idbButton);
        setLisetner();
        return BEPanel;
    }
    /**
     * 设置监听
     */
    public static void setLisetner(){
        //idb和jira的URL地址，用于鉴定COOKIE是否有效
        final String idbUrl = "";
        final String jiraUrl = "";
        //监听jiraIdb更新事件
        jiraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                if(jiraText.getText().equals("在这输入你需要更新的Cookie...")){
                    JOptionPane.showMessageDialog(null, "请输入JIRA的Cookie，再执行更新操作！", "提示", JOptionPane.WARNING_MESSAGE);
                }else if(!isLiveCookie(jiraText.getText(),jiraUrl)){
                    //判读输入的Cookie是否有效
                    System.out.println(jiraText.getText()+jiraUrl);
                    JOptionPane.showMessageDialog(null, "请输入有效的COOKIE！", "错误", JOptionPane.WARNING_MESSAGE);
                } else{
                    logger.info("jiraCookie执行更新操作！");
                    //更新数据库操作
                    //type为之前遗留规定，更改会带来联动问题，未更改
                    if(H2Manager.update(jiraText.getText(),"JIRA")){
                        JOptionPane.showMessageDialog(null, "COOKIE已更新为最新！", "提示",JOptionPane.PLAIN_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "更新Cookie失败，请重试！", "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }}catch (IOException S){logger.info("查询Cookie是否存活失败！");}
            }
        });
        //监听idbCookie更新事件
        idbButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{if(idbText.getText().equals("在这输入你需要更新的Cookie...")){
                    JOptionPane.showMessageDialog(null, "请输入IDB的Cookie，再执行更新操作！", "错误", JOptionPane.WARNING_MESSAGE);
                }else if(!isLiveCookie(idbText.getText(),idbUrl)){
                    //判读输入的Cookie是否有效
//                    System.out.println(isLiveCookie(idbText.getText()+idbText.getText(),idbUrl));
                    JOptionPane.showMessageDialog(null, "请输入有效的COOKIE！", "错误", JOptionPane.WARNING_MESSAGE);
                }else{
                    logger.info("idbCookie执行更新操作！");
                    //更新数据库操作
                    //type为之前遗留规定，更改会带来联动问题，未更改
                    if(H2Manager.update(idbText.getText(),"MSG")){
                        JOptionPane.showMessageDialog(null, "COOKIE已更新为最新！", "提示",JOptionPane.PLAIN_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "更新Cookie失败，请重试！", "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }}catch (IOException s){logger.info("查询Cookie是否存活失败！");}
            }
        });
    }

    /**
     * 判读数据是不是为最新的
     * @param Cookie
     * @param URL
     * @return
     * @throws IOException
     */
    private static Boolean isLiveCookie(String Cookie,String URL)throws IOException {
        HashMap<String, String> headermap = new HashMap<String, String>();
        headermap.put("Content-Type", "application/json");
        headermap.put("Cookie", Cookie);
        CloseableHttpResponse closeableHttpResponse;
        closeableHttpResponse = RestClient.get(URL,headermap);
        String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
        System.out.println(responseString);
        if(responseString.contains("统一登录")||responseString.contains("噢！遇到了一个错误。")){
            return false;
        }
        return true;
    }
//    public static void main(String []args){
//        if(isLiveCookie(""))
//    }
}
