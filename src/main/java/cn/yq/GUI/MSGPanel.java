package cn.yq.GUI;

import cn.yq.data.IdbQuery;
import cn.yq.restClient.RestClient;
import cn.yq.util.TestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eltima.components.ui.DatePicker;
import com.sun.javafx.scene.EnteredExitedHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


import static cn.yq.util.optDate.Date1BigThanDate2;


public class MSGPanel extends JFrame implements ActionListener {
    //日志系统
    private static Logger logger = LoggerFactory.getLogger(MSGPanel.class);

    String environment;  //环境
    String cookie;       //cookie
    String phone;        //手机号码
    static String msgFlag = " test";    //避免重复输出
    static String message;    //信息内容
    static String messageType ;//信息类型
    //存储验证码信息
    static HashMap<String,String> YZMMessage = new HashMap<String,String>();
    //存储通知信息
    static HashMap<String,String> TZMessage = new HashMap<String, String>();
    //时间格式
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //按钮
    JButton
            searchButton = new JButton("查询"),
            clearButton = new JButton("清空");
    //显示文本
    JLabel
            cookieLabel = new JLabel("Cookie："),
            envLabel = new JLabel("验证环境："),
            phoneLabel = new JLabel("手机号码："),
            showLabel = new JLabel("输出窗口："),
            title = new JLabel("<html>模<br>拟<br>进<br>场</html>"),
            startTimeLabel = new JLabel("开始时间："),
            endTimeLabel = new JLabel("结束时间："),
            JiraID = new JLabel("JIRA需求号：");      
    //容器
    JPanel panel = new JPanel(),      
           panelJIRA = new JPanel();//JIRA查询面板
    //面板
    JFrame frame = new JFrame("查询验证码 1.0");
    //菜单
    JMenuBar
            menuBar = new JMenuBar();
    JMenu menuOption = new JMenu("选择功能");
    //菜单项
    JMenuItem itemMsg = new JMenuItem("短信查询通道"),
              itemJira = new JMenuItem("JIRA信息抓取");
    //文本框
    JTextField
            cookieText = new JTextField(100),
            phoneText = new JTextField(50),
            jiraText = new JTextField(200);//jira需求号输入框

    static  JTextArea area = new JTextArea("");
    String []env= {"选择环境","tst","pre","prd"};
    String []msgType = {"消息类型","验证码","通知"};
    //下拉框
    JComboBox box = new JComboBox(env),
              msType = new JComboBox(msgType);
    static DatePicker
            startDatePicker = new DatePicker(),
            endDatePicker = new DatePicker();




    public MSGPanel(){
        frame.setSize(800, 500);
        frame.setLocation(300,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //菜单栏设置
        frame.setJMenuBar(menuBar);
        menuBar.add(menuOption);
        menuOption.add(itemMsg);
        menuOption.add(itemJira);
        itemJira.addActionListener(this);
        itemMsg.addActionListener(this);
        // 添加面板
        frame.add(panel);

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        cookieLabel.setBounds(10,20,80,25);
        panel.add(cookieLabel);
        /*
         * 创建文本域用于用户输入
         */
        cookieText.setBounds(100,20,170,25);
        panel.add(cookieText);
        /**
         * 模拟进场
         */
        title.setBounds(300,20,40,80);
//        title.setFont(new Font("楷体",Font.BOLD,12));
//        panel.add(title);
        //环境
        envLabel.setBounds(10,50,80,25);
        panel.add(envLabel);
        box.setBounds(100,50,80,25);
        box.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == 1)
                {}
//                    System.out.println(event.getItem());
            }
        });
        panel.add(box);
        //短信类型
        msType.setBounds(190,50,80,25);
        msType.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if(event.getStateChange()==1){}
//                    System.out.println(event.getItem());
            }
        });
        panel.add(msType);
        // 电话
        phoneLabel.setBounds(10,80,80,25);
        panel.add(phoneLabel);
        /*
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         */
        phoneText.setBounds(100,80,170,25);
        panel.add(phoneText);
        //开始时间
        startTimeLabel.setBounds(10,110,80,25);
        panel.add(startTimeLabel);
        startDatePicker.setBounds(100,110,170,25);
        panel.add(startDatePicker);
        //结束时间
        endTimeLabel.setBounds(10,140,80,25);
        panel.add(endTimeLabel);
        endDatePicker.setBounds(100,140,170,25);
        panel.add(endDatePicker);
        // 创建搜索按钮
        searchButton.setBounds(100, 170, 80, 25);
        panel.add(searchButton);
        //创建清空输出窗口按钮
        clearButton.setBounds(190, 170, 80, 25);
        panel.add(clearButton);
        //显示区域
        showLabel.setBounds(10,200,160,25);
        panel.add(showLabel);
        area.setFont(new Font("楷体",Font.BOLD,16));
        area.setLineWrap(true);
        area.setBackground(Color.ORANGE);
        area.setBounds(100,200,165,80);
        panel.add(searchButton);
        JScrollPane jsp=new JScrollPane(area);
        jsp.setBounds(100,200,650,210);
        panel.add(jsp);

        //给按钮添加添加监听
        searchButton.addActionListener(this);
        clearButton.addActionListener(this);
//        menuMsg;

        //使页面完全显示
        panel.invalidate();
        frame.setVisible(true);
    }
    public void actionPerformed(ActionEvent e)throws NullPointerException {
        if (e.getSource() == searchButton) {
            if (cookieText.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Cookie不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                cookieText.grabFocus();
            } else if (phoneText.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "手机号不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                cookieText.grabFocus();
            } else if (phoneText.getText().length() != 11) {
                JOptionPane.showMessageDialog(null, "请输入正确的手机号！", "错误", JOptionPane.ERROR_MESSAGE);
                cookieText.grabFocus();
            } else if (box.getSelectedItem().equals("选择环境")) {
                JOptionPane.showMessageDialog(null, "请选择环境！", "错误", JOptionPane.ERROR_MESSAGE);
                box.grabFocus();
            } else if (msType.getSelectedItem().equals("消息类型")) {
                JOptionPane.showMessageDialog(null, "请短信类型！", "错误", JOptionPane.ERROR_MESSAGE);
                msType.grabFocus();
            }  else if (startDatePicker.getText().length() ==0) {
                JOptionPane.showMessageDialog(null, "请选择开始时间！", "错误", JOptionPane.ERROR_MESSAGE);
                msType.grabFocus();
             }else if (endDatePicker.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请选择结束时间！", "错误", JOptionPane.ERROR_MESSAGE);
                msType.grabFocus();
            }
            else {
                cookie = cookieText.getText();
                phone = phoneText.getText();
                environment = box.getSelectedItem().toString();
                if (msType.getSelectedItem().toString().equals("通知"))
                    messageType = "TZ";
                else
                    messageType = "YZM";
                try {
                    logger.info("环境："+environment+"  短信类型："+messageType+"  手机号码:"+phone+"  Cookie："+cookie);
                    logger.info("开始时间："+startDatePicker.getText());
                    logger.info("结束时间："+endDatePicker.getText());
                    idb(environment, phone, cookie);
                    showMessage(YZMMessage,TZMessage,messageType);
                } catch (IOException c) {
                    message = "查询出错！";
                    area.append(message);
                    area.repaint();
                    logger.error(message);
                }
            }
        }else if(e.getSource() == clearButton){
            //清楚输出框的文本信息
            if(area.getText().length() != 0)
                area.setText("");
            else {
                JOptionPane.showMessageDialog(null, "输出窗口已为空！", "错误", JOptionPane.ERROR_MESSAGE);
                area.grabFocus();
            }
        }else if(e.getSource() == itemMsg){
            System.out.println("短信查询通道！");
            panel.setVisible(true);
        }else if(e.getSource() == itemJira){
            System.out.println("jira通道");
            panel.setVisible(false);

        }

    }

    /**
     *用这个接口，访问数据库，查询数据，并存入两个哈希表中
     * @param dbenv   环境
     * @throws IOException
     */
    public static void idb(String dbenv,String phone,String Cookie)throws IOException {
        CloseableHttpResponse closeableHttpResponse;
        RestClient restClient = new RestClient();
        //访问的接口地址
        String Url = "http://idb.zhonganonline.com/getqueryrst";
        String dbname = "nereus";
        String tbname = "nereus_message_sent";
        String sql = "receiver_no ='"+phone +"'";
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
        logger.info("传送进IDB接口的报文："+idb2Json);
        closeableHttpResponse = restClient.post(Url, idb2Json, headermap);
        JSONObject responseJson = restClient.getResponseJson(closeableHttpResponse);
        String panDuan = "[]";
        int tbNum = 0;   //遍历八个表
        //判断三种情况，返回为[],返回为null,和表不会越界
        while(panDuan.length()==2 ||panDuan.length() == 0 && tbNum<8) {
            logger.info("-----------进入json表--------");
            String flag = "nereus_00.nereus_message_sent_000" + tbNum;
            panDuan = TestUtil.getValueByJPath(responseJson, flag);
            if (panDuan.length() > 2) {
                logger.info("---------进入循环--------");
                JSONArray test = JSONArray.parseArray(panDuan);
                //判断条数
                int size = test.size();
                logger.info("数据条数："+size);
                for (int j = 0; j < size; j++) {
                    String index = "[" + j + "]";
                    //短信类型  TZ通知，YZM验证码
                       try {
                               //时间 和 短信内容
                               String gmt_created = TestUtil.getValueByJPath(responseJson, flag + index + "/gmt_created");
                               String content = TestUtil.getValueByJPath(responseJson, flag + index + "/content");
                               String message_type = TestUtil.getValueByJPath(responseJson, flag + index + "/message_type");
                               message = TestUtil.getValueByJPath(responseJson, flag + index + "/gmt_created") + TestUtil.getValueByJPath(responseJson, flag + index + "/content");
                               if (message_type.equals("YZM")){
                                   YZMMessage.put(gmt_created,content);
                               }else if(message_type.equals("TZ")){
                                   TZMessage.put(gmt_created,content);
                               }else{
                                   logger.info("出现遗漏信息："+gmt_created+" "+content);
                               }
                       }catch (NullPointerException e){
                           area.append("没有查询到信息，请检查环境与手机号是否有误！\n");
                       }
                }
            }
            //遍历json返回的8个表
            tbNum++;
        }
    }


    /**
     * 传入储存了验证码和通知内容的两个哈希表以及短信类型，在输出窗口打印出来
     * @param YZMMessage
     * @param TZMessage
     * @param MsgType
     */
    public static void showMessage(HashMap<String,String> YZMMessage,HashMap<String,String> TZMessage, String MsgType){
        String date1 = startDatePicker.getText();  //开始时间
        String date2 = endDatePicker.getText();    //结束时间
        if(MsgType.equals("TZ")){
            Set set = TZMessage.keySet();
            Object []arr = set.toArray();
            Arrays.sort(arr);
            boolean haveNoTZValue = true;   //初始设置为该时间段没有信息，当获取的一条信息后，就证明有信息，盖标志为false
            for(Object key:arr){
                String gmt_created =key.toString();
                String content = TZMessage.get(key);
                if(Date1BigThanDate2(gmt_created,date1) && Date1BigThanDate2(date2,gmt_created)){
                    haveNoTZValue = false;
                    area.append(gmt_created+ ":"+content+ "\n");
                    area.append(" \n");
                    area.repaint(50);
                    area.grabFocus();
                }
            }
            if(haveNoTZValue){
                area.append("---请检查查询信息是否正确，该时间段数据库中没有信息---\n");
                area.repaint(50);
                area.grabFocus();
            }
        }else if(MsgType.equals("YZM")){
            Set set = YZMMessage.keySet();
            Object []arr = set.toArray();
            Arrays.sort(arr);
            boolean haveNoYZMValue = true;   //初始设置为该时间段没有信息，当获取的一条信息后，就证明有信息，盖标志为false
            for(Object key:arr){
                String gmt_created =key.toString();
                String content = YZMMessage.get(key);
                if(Date1BigThanDate2(gmt_created,date1) && Date1BigThanDate2(date2,gmt_created)){
                    haveNoYZMValue = false;
                    area.append(gmt_created+ ":"+content+ "\n");
                    area.append(" \n");
                    area.repaint(50);
                    area.grabFocus();
                }
            }
            if(haveNoYZMValue){
                area.append("---请检查查询信息是否正确，该时间段数据库中没有信息---\n");
                area.repaint(50);
                area.grabFocus();
            }
        } else{
            //未知短信类型
            area.append("请选择正确的短信类型！\n");
            area.repaint(50);
            area.grabFocus();
        }
    }


    public static void main(String[] args) {
       try{  new MSGPanel();}catch (Exception e){
           System.out.println("warning!");
       }
    }
}

