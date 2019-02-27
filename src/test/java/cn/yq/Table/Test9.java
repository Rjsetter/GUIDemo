package cn.yq.Table;

import cn.yq.H2DataBase.H2Manager;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * 展示如何使用DefaultTableModel类创建表格
 *
 * @author burns
 *
 */
public class Test9 {

    JTable table = null;
    DefaultTableModel defaultModel = null;

    public Test9(Map<String,List<String>> Data) {
        JFrame f = new JFrame();
        List<String> HEADER = new LinkedList<String>();
        HEADER = Data.get("0");
        Vector columnNames = createColumnNames(HEADER);
        Vector data = createTableModelData();

        // 创建一个默认的表格模型
        defaultModel = new DefaultTableModel(data, columnNames);
        table = new JTable(defaultModel);
        table.setPreferredScrollableViewportSize(new Dimension(400, 80));

        JScrollPane tablePanel = new JScrollPane(table);

        JPanel buttonPanel = new JPanel();
        JButton addRow = new JButton("增加行");
        buttonPanel.add(addRow);
        addRow.addActionListener(new AddRowAction(defaultModel));

        JButton deleteRow = new JButton("删除行");
        buttonPanel.add(deleteRow);
        deleteRow.addActionListener(new DeleteRowAction(defaultModel));

        generateTabelDataThread();


        Container contentPane = f.getContentPane();
        contentPane.add(buttonPanel, BorderLayout.NORTH);
        contentPane.add(tablePanel, BorderLayout.CENTER);
        f.setTitle("AddRemoveCells");
        f.pack();
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void generateTabelDataThread() {
        GenerateDataThread t1 = new GenerateDataThread();
        Thread t = new Thread(t1);
        t.start();

    }

    private Vector createColumnNames(List<String> HEADER) {
        //添加数据表标题
        Vector columnNames = new Vector();
        for(int i=0;i<HEADER.size();i++)
        {
            columnNames.add(HEADER.get(i));
        }
        return columnNames;
    }

    private Vector createTableModelData() {
        Vector rowData1 = new Vector();
        rowData1.add("王鹏");
        rowData1.add(new Integer(91));
        rowData1.add(new Integer(1949));
        rowData1.add(new Integer(1910));

        Vector data = new Vector();
//        data.add(rowData1);
        return data;
    }

//    public static void main(String[] args) {
//        new Test9();
//    }


    class GenerateDataThread implements Runnable {
        @Override
        public void run() {
            try {
                ResultSet rs = H2Manager.table();
                int i = 0;
                while (rs.next()) {
                    i++;
                    Vector rowData = new Vector();
                    rowData.add(rs.getString(1));
                    rowData.add(rs.getString(2));
                    rowData.add(rs.getString(3));
                    rowData.add(rs.getString(4));
                    if (i % 2000 == 0) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    defaultModel.insertRow(defaultModel.getRowCount(), rowData);
                }
            } catch (SQLException e) {
                System.out.println();
            }

        }
    }
}