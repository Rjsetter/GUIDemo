package cn.yq.Table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.table.DefaultTableModel;

public class DeleteRowAction implements ActionListener {

    private DefaultTableModel defaultModel;

    public DeleteRowAction(DefaultTableModel defaultModel) {
        this.defaultModel = defaultModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int rowcount = defaultModel.getRowCount() - 1;
        defaultModel.removeRow(rowcount);

    }

}