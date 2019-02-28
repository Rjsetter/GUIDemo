package cn.yq.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static cn.yq.GUI.BEPanel.getBEPanel;
import static cn.yq.GUI.JIRAPanel.getJIRAPanel;


/**
 * 主程序
 * @author RJSETTER
 * @date 2019/1/29
 */
public class GUIDemo extends JFrame implements ActionListener {

    JFrame frame = new JFrame("测试小工具 1.0");
    // 创建选项卡面板
    static JTabbedPane tabbedPane = new JTabbedPane();

    public GUIDemo(){
        frame.setSize(800, 500);
        frame.setLocation(300,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tabbedPane.add("短信查询通道", getMSGPanel());
        tabbedPane.add("JIRA2EXCEL", getJIRAPanel());
        tabbedPane.add("IDB数据库", IDBPanel.getIDBPanel());
        tabbedPane.add("后台管理", getBEPanel());

        frame.setContentPane(tabbedPane);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)throws NullPointerException { }

    public static void main(String[] args) {
       try{  new GUIDemo();}catch (Exception e){
           System.out.println("warning!");
       }
    }
}



