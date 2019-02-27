package cn.yq.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * 工具类文本框默认提示文字
 * @author RJSETTER
 * @date 2019/1/29
 * 使用方式
 * JTextField jTextField = new JTextField();
 * jTextField.addFocusListener(new JTextFieldHintListener(jTextField, "提示内容"));
 */


public class DefaultTextUtil {
    static public class JTextFieldHintListener implements FocusListener {
        private String hintText;
        private JTextField textField;
        public JTextFieldHintListener(JTextField jTextField,String hintText) {
            this.textField = jTextField;
            this.hintText = hintText;
            jTextField.setText(hintText);  //默认直接显示
            jTextField.setForeground(Color.GRAY);
        }

        @Override
        public void focusGained(FocusEvent e) {
            //获取焦点时，清空提示内容
            String temp = textField.getText();
            if(temp.equals(hintText)) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }

        }

        @Override
        public void focusLost(FocusEvent e) {
            //失去焦点时，没有输入内容，显示提示内容
            String temp = textField.getText();
            if(temp.equals("")) {
                textField.setForeground(Color.GRAY);
                textField.setText(hintText);
            }
        }



    }
}
