package cn.yq.Table;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class CreateTable {
    static JTable table = null;
    static DefaultTableModel defaultModel = null;

    public static void newTable(Map<String, Map<String, String>> Data, List<String> HEADER) {
        JFrame f = new JFrame();
        Vector columnNames = createColumnNames(HEADER);
        Vector data = createTableModelData(Data, HEADER);

        // 创建一个默认的表格模型
        defaultModel = new DefaultTableModel(data, columnNames);
        table = new JTable(defaultModel);
        //实现表格的排序功能
        final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(
                defaultModel);
        table.setRowSorter(sorter);
        table.setRowSorter(sorter);
        //数据查询功能
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("查询");
        panel.add(label, BorderLayout.WEST);
        final JTextField filterText = new JTextField("");
        panel.add(filterText, BorderLayout.CENTER);
        f.add(panel, BorderLayout.NORTH);
        JButton button = new JButton("查询");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = filterText.getText();
                if (text.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(text));
                }
            }
        });
        f.add(button, BorderLayout.SOUTH);
        //设置内容居中显示
        setTableColumnCenter(table);
        //选中行
        table.setRowSelectionAllowed(true);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        //设置内容自动换行
        table.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer());
        table.setPreferredScrollableViewportSize(new Dimension(600, 80));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane tablePanel = new JScrollPane(); //不需要把table作为参数
        tablePanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tablePanel.setViewportView(table);


        Container contentPane = f.getContentPane();
        contentPane.add(tablePanel, BorderLayout.CENTER);
        f.setTitle("数据表格");
        f.pack();
        f.setVisible(true);
    }


    private static Vector createColumnNames(List<String> HEADER) {
        //添加数据表标题
        Vector columnNames = new Vector();
        for (int i = 0; i < HEADER.size(); i++) {
            columnNames.add(HEADER.get(i));
        }
        return columnNames;
    }

    private static Vector createTableModelData(Map<String, Map<String, String>> Data, List<String> HEADER) {
        Vector data = new Vector();
        for (int i = 1; i < Data.size(); i++) {
            Map<String, String> info = Data.get(Integer.toString(i));
            Vector rowData = new Vector();
            for (int j = 0; j < HEADER.size(); j++) {
                rowData.add(info.get(HEADER.get(j)));
            }
            data.add(rowData);
        }
        return data;
    }


    /**
     * 表格内容自动换行
     */
    static class TableCellTextAreaRenderer extends JTextArea implements TableCellRenderer {
        public TableCellTextAreaRenderer() {

            setLineWrap(true);
            setWrapStyleWord(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            /**
             * 设置字体,并且字体居中
             */
            this.setFont(new Font("黑体", Font.PLAIN, 15));
            // 计算当下行的最佳高度
            int maxPreferredHeight = 20;
            for (int i = 0; i < table.getColumnCount(); i++) {
                setText("" + table.getValueAt(row, i));
                setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
                maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);
            }
            if (table.getRowHeight(row) != maxPreferredHeight) // 少了这行则处理器瞎忙
                table.setRowHeight(row, maxPreferredHeight);
            setText(value == null ? "" : value.toString());
            /**
             * 设置选中行颜色
             */
            Color foreground, background;
            if (isSelected) {
                foreground = Color.DARK_GRAY;
                background = Color.lightGray;
            } else {
                foreground = Color.black;
                background = Color.white;
            }
            this.setForeground(foreground);
            this.setBackground(background);
            return this;
        }
    }

    /**
     * 表格数据居中
     *
     * @param table
     */
    public static void setTableColumnCenter(JTable table) {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);
    }

    /**
     * 内容自适应调整列宽
     * @param myTable
     */
    public static void FitTableColumns(JTable myTable) {
        JTableHeader header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();

        Enumeration columns = myTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) myTable.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(myTable, column.getIdentifier()
                            , false, false, -1, col).getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++) {
                int preferedWidth = (int) myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable,
                        myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column); // 此行很重要
            column.setWidth(width + myTable.getIntercellSpacing().width);
        }
    }
}