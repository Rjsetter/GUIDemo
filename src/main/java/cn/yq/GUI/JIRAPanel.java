package cn.yq.GUI;


import cn.yq.H2DataBase.H2Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import static cn.yq.GUI.Datetime.dateDiff;
import static cn.yq.GUI.Datetime.getDatetime;
import static cn.yq.GUI.JIRA.Jira2Excel;
import static cn.yq.H2DataBase.H2Manager.searchCookie;


/**
 * 小程序的JIRA面板JIRAPanel
 * @author RJSETTER
 * @date 2019/1/29
 */
public class JIRAPanel extends JPanel {
    //日志系统
    private static Logger logger = LoggerFactory.getLogger(JIRAPanel.class);
    static String COOKIE ="";
    //选择分类的列表，用于储存人员从页面选择的数据
    public static List<String> selectedTypeList = new LinkedList<String>();
    //按钮
    static JButton
            clearButton = new JButton("清空"),
            startButton = new JButton("开始生成Excel表");
    //显示文本
    static JLabel
            cookieLabel2Jira = new JLabel("Cookie："),
            showLabel = new JLabel("输出窗口："),
            JiraID = new JLabel("JIRA需求号："),
            typeSelect = new JLabel("选择分类：");
    //面板
    static JPanel panelJIRA = new JPanel(){
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon ii = new ImageIcon("D:\\GUIDemo\\src\\main\\java\\zhongan2.jpg");
            g.drawImage(ii.getImage(), 0, 0, getWidth(), getHeight(), ii.getImageObserver());
        }
    };
    //文本框
    static JTextField
            cookieText2Jira = new JTextField(100),
            jiraText = new JTextField(200);//jira需求号输入框
    //输出文本域
    static  JTextArea showArea = new JTextArea("");

    /**
     * 设置JIRA面板
     */
    static public JPanel getJIRAPanel(){
        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panelJIRA.setLayout(null);
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        cookieLabel2Jira.setBounds(10,20,80,25);
        panelJIRA.add(cookieLabel2Jira);
        /*
         * 创建文本域用于用户输入
         */
        String showMSG = "你需要重新输入COOKIE...";
        if(isLiveCookie())
            showMSG = "太好啦!COOKIE还是活跃的，可以不用输入！";
        cookieText2Jira.setBounds(100,20,400,25);
        cookieText2Jira.setFont(new Font("黑体", Font.PLAIN, 16));
        cookieText2Jira.addFocusListener(new DefaultTextUtil.JTextFieldHintListener(cookieText2Jira, showMSG));

        panelJIRA.add(cookieText2Jira);
        /**
         * JIRA需求号输入框
         */
        JiraID.setBounds(10,50,80,25);
        panelJIRA.add(JiraID);
        /**
         * 创建文本域用于用户输入
         */
        jiraText.setBounds(100,50,400,25);
        jiraText.setFont(new Font("黑体", Font.PLAIN, 16));
        jiraText.addFocusListener(new DefaultTextUtil.JTextFieldHintListener(jiraText, "请依次输入JIRA需求号，以因为逗号隔开..."));
        panelJIRA.add(jiraText);
        //多选框
        typeSelect.setBounds(10,80,80,25);
//        typeSelect.setFont(new Font("黑体", Font.PLAIN, 16));
        panelJIRA.add(typeSelect);
        //换行数，决定输出框的Y值
        int flagNum = setCheckBox(panelJIRA);
        int Y = (flagNum/4+1)*30 +80;
        //输出窗口 文本
        showLabel.setBounds(10,Y,80,25);
        panelJIRA.add(showLabel);
        showArea.setFont(new Font("黑体",Font.BOLD,13));
        showArea.setLineWrap(true);
//        showArea.setBackground(Color.ORANGE);
        showArea.setBorder(BorderFactory.createLineBorder(Color.black));
        showArea.setBounds(100,Y,400,100);
        JScrollPane jsp=new JScrollPane(showArea);
        jsp.setBounds(100,Y,650,160);
        panelJIRA.add(jsp);
        //生成按钮
        startButton.setBounds(300,Y+170,140,25);
        panelJIRA.add(startButton);
        //按钮监听
        setButtonLister(startButton);
        return panelJIRA;
    }

    /**
     * 添加按钮监听
     * @param button
     */
    public static void setButtonLister(JButton button){
        //监听开始按钮
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == startButton){
                    if(cookieText2Jira.getText().equals("你需要重新输入COOKIE...")){
                        JOptionPane.showMessageDialog(null, "COOKIE过期啦，请重新输入！", "错误", JOptionPane.ERROR_MESSAGE);
                    }else if(jiraText.getText().equals("请依次输入JIRA需求号，以因为逗号隔开...")){
                        JOptionPane.showMessageDialog(null, "请输入JIRA需求号哦!", "错误", JOptionPane.ERROR_MESSAGE);
                    }else if(selectedTypeList.size() == 0){
                        JOptionPane.showMessageDialog(null, "请选择要生成的分类哦！!", "错误", JOptionPane.ERROR_MESSAGE);
                    }else{
                        if(!isLiveCookie()||!cookieText2Jira.getText().equals("太好啦!COOKIE还是活跃的，可以不用输入！")){
                            COOKIE = cookieText2Jira.getText();
                        }
//                        System.out.println("-------测试一下----------------"+COOKIE);
                        if(Jira2Excel(COOKIE,jiraText.getText(),selectedTypeList)){
                            JOptionPane.showMessageDialog(null, "生成Excel成功！!", "错误", JOptionPane.INFORMATION_MESSAGE);
                            showArea.grabFocus();
                        }else{
                            JOptionPane.showMessageDialog(null, "生成Excel失败，请检查是否出错！!", "错误", JOptionPane.ERROR_MESSAGE);
                            showArea.grabFocus();
                        }
                    }
                }
            }
        });

    }

    /**
     * 向面板中添加多选项
     * @param panel
     * @return
     */
    public static int setCheckBox(JPanel panel){
        String sql = "SELECT * FROM TYPE";
        List<String> rs=  H2Manager.search(sql);
        //列表用于储存多选控件
        List<JCheckBox> CheckBoxs = new LinkedList<JCheckBox>();
            for(int j=1;j<rs.size();j++)
            {
                 CheckBoxs.add(new JCheckBox(rs.get(j)));
//                System.out.println(rs.getInt("id") + "," + rs.getString("key")+ "," + rs.getString("value"));
            }
        int X=10,Y=50,W=120,H=25,turnFlag = 0;
            //添加多选控件
        for(int i=0;i<CheckBoxs.size();i++){
            X += 120;
            if(turnFlag%4 ==0){
                //每一行有四个选项的时候换行
                Y += 30;
                X = 100;
            }
            turnFlag += 1;
            JCheckBox BOOM = CheckBoxs.get(i);
            BOOM.setFont(new Font("黑体", Font.PLAIN, 14));
            BOOM.setBounds(X,Y,W,H);
            //添加监控,监控添加人员
            BOOM.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 获取事件源（即复选框本身）
                    JCheckBox checkBox = (JCheckBox) e.getSource();
                    if(checkBox.isSelected()) {
                        //选中时
                        if (!selectedTypeList.contains(checkBox.getText())) {
//                            System.out.println("选中并添加: " + checkBox.getText());
                            selectedTypeList.add(checkBox.getText());
                            showArea.append("选中并添加: " + checkBox.getText()+'\n');
                            showArea.append("目前选中："+selectedTypeList.toString()+'\n');
                        }
                    }else if(!checkBox.isSelected()){
                        //未选中时
                            selectedTypeList.remove(checkBox.getText());
                            showArea.append("删除所选: " +checkBox.getText()+'\n');
                            showArea.append("目前选中："+selectedTypeList.toString()+'\n');
                        }
                }
            });
            panel.add(BOOM);
        }
        return turnFlag;
    }

    public static boolean isLiveCookie(){
        String sql = "SELECT COOKIE,DATE FROM COOKIES WHERE TYPE='JIRA'";
        long flag = 388800;  //设置cookie存活时间为4.5天，及108个小时
        List<String> INFO = searchCookie(sql);
        String newDate = getDatetime();
        String oldDate = INFO.get(1);
        //全局变量，cookie
        COOKIE = INFO.get(0);
        try{
        if(dateDiff(oldDate,newDate,"yyyy-MM-dd HH:mm:ss")<flag) {
            logger.info("这个Cookie是活跃的！");
            return true;
        }}catch (Exception e){
            logger.info("日期对比失败！");
        }
        return false;
    }
}
