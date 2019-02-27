package cn.yq.Table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.table.DefaultTableModel;

public class AddRowAction implements ActionListener {


    DefaultTableModel defaultModel;

    public AddRowAction(DefaultTableModel defaultModel) {
        this.defaultModel = defaultModel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object[] p = { "lvyuan", new Integer(91), new Integer(1949),
                new Integer(1910) };
        defaultModel.insertRow(0, p); //用此种方法插入数据，不会造成表格闪动过快，用户的选择也不会丢失

    }

}
