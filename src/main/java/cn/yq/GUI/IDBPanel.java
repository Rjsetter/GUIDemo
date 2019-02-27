package cn.yq.GUI;
import cn.yq.H2DataBase.H2Manager;
import cn.yq.data.IdbQuery;
import cn.yq.restClient.RestClient;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import static cn.yq.Table.CreateTable.newTable;

/**
 * idb查询面板
 * @author RJSETTER
 * @date 2019/2/1
 */
public class IDBPanel extends JPanel {
    //日志
    private static Logger logger = LoggerFactory.getLogger(JIRAPanel.class);
    //联动字段
    static String env2db = "tst"; //默认为生产环境,用于传入数据库，查找对应的表名，显示到相应下拉框中
     //创建IDB面板
    static JPanel IDBJpanel = new JPanel(){
         public void paintComponent(Graphics g) {
             super.paintComponent(g);
             ImageIcon ii = new ImageIcon("D:\\GUIDemo\\src\\main\\java\\zhongan2.jpg");
             g.drawImage(ii.getImage(), 0, 0, getWidth(), getHeight(), ii.getImageObserver());
         }
     };
    //下拉框
    static JComboBox AutoComboBoxDBName = new JComboBox(),
                    AutoComboBoxTBName = new JComboBox(),
                    AutoComboBoxEnvName = new JComboBox();
    //显示文本
    static JLabel
            IDBCookieJlabel = new JLabel("Cookie："),
            EnvJLabel = new JLabel("选择环境："),
            DBnameJLabel = new JLabel("数据库名："),
            TBnameJLabel = new JLabel("数据表名："),
            SQLJLabel = new JLabel("SQL语句：");
    //文本框
    static JTextField
        IDBCookieText = new JTextField();
    //文本域
    static JTextArea
        IDBSqlText = new JTextArea();
    //按钮
    static JButton
        SearchButton = new JButton("查询按钮");

    public static JPanel getIDBPanel(){
        /**
         * 布局设置为null
         */
        IDBJpanel.setLayout(null);
        /**
         * COOKIE模块
         */
        IDBCookieJlabel.setBounds(10,20,80,25);
        IDBJpanel.add(IDBCookieJlabel);
        /*
         * 创建文本域用于用户输入
         */
        String showMSG = "你需要重新输入COOKIE...";
//        if(isLiveCookie())
//            showMSG = "太好啦!COOKIE还是活跃的，可以不用输入！";
        IDBCookieText.setBounds(100,20,400,25);
        IDBCookieText.setFont(new Font("黑体", Font.PLAIN, 16));
        IDBCookieText.addFocusListener(new DefaultTextUtil.JTextFieldHintListener(IDBCookieText, showMSG));
        IDBJpanel.add(IDBCookieText);
        /**
         * 下拉框模块--》 数据环境
         */
        EnvJLabel.setBounds(10,50,70,25);
        IDBJpanel.add(EnvJLabel);
        AutoComboBoxEnvName.setEditable(true);
        AutoComboBoxEnvName.setBounds(90,50,100,25);
        AutoComboBoxEnvName.addItem("请选择！");
        AutoComboBoxEnvName.addItem("测试环境(TST)");
        AutoComboBoxEnvName.addItem("预发环境(PRE)");
        AutoComboBoxEnvName.addItem("生产环境(PRD)");
        IDBJpanel.add(AutoComboBoxEnvName);
        /**
         * 下拉框模块--》 数据库名字模块
         */
        DBnameJLabel.setBounds(220,50,70,25);
        IDBJpanel.add(DBnameJLabel);
        AutoComboBoxDBName.setEditable(true);
        AutoComboBoxDBName.setBounds(300,50,120,25);
        IDBJpanel.add(AutoComboBoxDBName);
        /**
         * 下拉框模块--》 数据表名字模块
         */
        TBnameJLabel.setBounds(430,50,100,25);
        IDBJpanel.add(TBnameJLabel);
        AutoComboBoxTBName.setEditable(true);
        AutoComboBoxTBName.setBounds(510,50,120,25);
        AutoComboBoxEnvName.setEditable(false);
        IDBJpanel.add(AutoComboBoxTBName);
        /**
         * SQL语句输入窗口排版
         */
        SQLJLabel.setBounds(10,80,70,25);
        IDBJpanel.add(SQLJLabel);
        IDBSqlText.setBounds(90,80,600,100);
        IDBSqlText.setFont(new Font("楷体",Font.BOLD,16));
        IDBSqlText.setLineWrap(true);
        IDBSqlText.setBackground(Color.ORANGE);
        IDBJpanel.add(IDBSqlText);
        SearchButton.setBounds(90,190,100,25);
        IDBJpanel.add(SearchButton);
        setListener();
        return IDBJpanel;
    }

    /**
     * 传入下级下拉框
     * @param ComboBox
     */
    public static void setComboBox(JComboBox ComboBox,String env2db,String flag,String envflag){
        ResultSet rs = null;
        if(flag.equals("init")){
            rs = H2Manager.getDBName(env2db);
            ComboBox.addItem("-------------------");
            try{
                while(rs.next()){
                    ComboBox.addItem(rs.getString(1));
//                    System.out.println(flag+":"+rs.getString(1));
                }
            }catch (SQLException e){
                logger.info("查询数据库名出错。");
            }
        } else if(flag.equals("update")){
            rs = H2Manager.searchLike(env2db,envflag);
           ComboBox.removeAllItems();
           ComboBox.addItem(env2db);
            try{
                while(rs.next()){
                    ComboBox.addItem(rs.getString(1));
//                    System.out.println(flag+":"+rs.getString(1));
                }
            }catch (SQLException e){
                logger.info("查询数据库名出错。");
            }
        }
    }

    /**
     * 传入数据库表的下拉框
     * @param ComboBox
     */
    public static void setTBComboBox(JComboBox ComboBox,String env2db,String dbname,String flag,String tbname){
        ResultSet rs = null;
        if(flag.equals("init")){
            rs = H2Manager.getTBName(env2db,dbname);
            ComboBox.removeAllItems();
            try{
                while(rs.next()){
                    ComboBox.addItem(rs.getString(1));
                }
            }catch (SQLException e){
                logger.info("查询数据库名出错。");
            }
        } else if(flag.equals("update")){
            rs = H2Manager.searchTB(env2db,dbname,tbname);
            ComboBox.removeAllItems();
            ComboBox.addItem(tbname);
            try{
                while(rs.next()){
                    ComboBox.addItem(rs.getString(1));
//                    System.out.println(flag+":"+rs.getString(1));
                }
            }catch (SQLException e){
                logger.info("查询数据库名出错。");
            }
        }
    }

    /**
     * 设置IDB面板的监听
     */
    private static void setListener(){
        /**
         * 监听模块  监听选择的环境
         */
        AutoComboBoxEnvName.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getItem().toString().equals("预发环境(PRE)")){
                    env2db = "pre";
                    setComboBox(AutoComboBoxDBName,"pre","init","");
                }else if(e.getItem().toString().equals("测试环境(TST)")){
                    env2db = "tst";
                    setComboBox(AutoComboBoxDBName,"tst","init","");
                }
                else {
                    setComboBox(AutoComboBoxDBName,"prd","init","");
                    env2db = "prd";
                }
            }
        });
        AutoComboBoxDBName.setMaximumRowCount(10);
        /**
         * 监听模块
         * 监听数据库--》选择相应的表名
         */
        AutoComboBoxDBName.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println("目前选中："+e.getItem().toString()+"库");
                setTBComboBox(AutoComboBoxTBName,env2db,e.getItem().toString(),"init","");
            }
        });
        /**
         * 监听模块  数据库模块监听数据库
         */
        AutoComboBoxDBName.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
//            System.out.println(AutoComboBoxDBName.getEditor().getItem().toString());
                String updateText =AutoComboBoxDBName.getEditor().getItem().toString();
                //移除所有选项
                setComboBox(AutoComboBoxDBName,updateText,"update",env2db);
            }
        });
        /**
         * 监听模块  数据表模块监听数据库
         */
        AutoComboBoxTBName.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }
            @Override
            public void keyPressed(KeyEvent e) { }
            @Override
            public void keyReleased(KeyEvent e) {
                String updateText =AutoComboBoxTBName.getEditor().getItem().toString();
                System.out.println("打印"+updateText);
                String dbname = AutoComboBoxDBName.getEditor().getItem().toString();
                setTBComboBox(AutoComboBoxTBName,env2db,dbname,"update",updateText);
            }
        });
        /**
         * 设置按钮监听
         */
        SearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(AutoComboBoxEnvName.getSelectedIndex() == 0){
                    JOptionPane.showMessageDialog(null, "请选择环境！", "错误", JOptionPane.ERROR_MESSAGE);
                }else if(AutoComboBoxDBName.getEditor().getItem().toString().equals("")||AutoComboBoxDBName.getEditor().getItem().toString().equals("-------------------")){
                    JOptionPane.showMessageDialog(null, "请选择数据库！", "错误", JOptionPane.ERROR_MESSAGE);
                }else if(AutoComboBoxTBName.getEditor().getItem().equals("")){
                    JOptionPane.showMessageDialog(null, "请选择数据表！", "错误", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    String dbname = AutoComboBoxDBName.getEditor().getItem().toString();
                    String tbname = AutoComboBoxTBName.getEditor().getItem().toString();
                    String sql = IDBSqlText.getText();
                    System.out.println(env2db+dbname+tbname+sql);
                    try{idb(env2db,dbname,tbname,sql);}catch (IOException s){logger.info("数据库查询出错。"+s);}
                }
            }
        });
    }

    /**
     * 数据查询操作
     * @param dbenv
     * @param dbname
     * @param tbname
     * @param sql
     * @throws IOException
     */
    private static void idb(String dbenv,String dbname, String tbname,String sql)throws IOException {
        CloseableHttpResponse closeableHttpResponse;
        RestClient restClient = new RestClient();
        String Url = "http://idb.zhonganonline.com/getqueryrst";
        String Cookie = "IDB_BETA_UID=D%0AAwMTE5MDk%3DM-%3D1550200392-%3D89c32cef2917059cdd441f333c157d79";
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
        /*
       美化输出
         */
        String responseJson = restClient.getResponseJson(closeableHttpResponse).toString();
        ObjectMapper mapper = new ObjectMapper();
        Object obj = mapper.readValue(responseJson,Object.class);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
        parseJson(responseJson);

    }

    /**
     * 接受json，进行解析,idb返回的json为一个json对象，其中有键值对，key为数据库，value为一个json数组
     * @param jsonString
     */
    private static void parseJson(String jsonString) throws IOException{
        //存放数据信息（头部和信息）
        Map<String, Map<String,String>> Data = new HashMap<String, Map<String, String>>();
        //存放头部信息
        List<String> header = new ArrayList<String>();
//        header.add(0,"realDbTbName");
        ObjectMapper mapper = new ObjectMapper();
        // Jackson提供一个树节点被称为"JsonNode",ObjectMapper提供方法来读json作为树的JsonNode根节点
        JsonNode node = mapper.readTree(jsonString);
        // 看看根节点的类型
        System.out.println("node JsonNodeType:" + node.getNodeType());
        // 是不是一个容器
        System.out.println("node is container Node ? " + node.isContainerNode());
        // 得到所有node节点的子节点名称
        System.out.println("---------得到所有node节点的子节点名称-------------------------");
        Iterator< String > fieldNames = node.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            System.out.println(fieldName + " ");
            //遍历数组中的内容
            JsonNode content = node.get(fieldName);
            //flag记录表头的标志
            Boolean flag = true;
            int i = 1;
            for(JsonNode Elements:content){
                Map<String,String> data = new HashMap<String, String>();
                Iterator<String> elementFileNames = Elements.fieldNames();
//                System.out.println("----------------------------------");
                while (elementFileNames.hasNext()){
                    String test = (String) elementFileNames.next();
                    data.put(test,Elements.get(test).asText());
                    if(flag){
                        header.add(test);
                    }
                }
                Data.put(Integer.toString(i),data);
                i++;
                flag=false;
            }
        }
        //测试用
//        System.out.println(Data.size());
//        for(int i = 0;i<Data.size();i++){
//            System.out.println(Data.get(Integer.toString(i)));
//        }
        if(Data.size()>1){
            newTable(Data,header);
        }else{
            JOptionPane.showMessageDialog(null, "查询错误，请检查是否出错！", "错误", JOptionPane.ERROR_MESSAGE);
        }

    }

}
