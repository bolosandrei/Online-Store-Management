package presentation;

import dao.ProductDAO;
import model.Client;
import model.Product;
import start.ReflectionExample;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

import static javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS;

public class ProductView extends JFrame {
    private final ProductDAO pdao = new ProductDAO();
    private JTable table;
    JFrame frProduct = new JFrame("Product OP");

    public ProductView() {
        frProduct.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frProduct.setLocation(450, 100);
        frProduct.setSize(650, 600);
        JPanel panel = new JPanel();
        JPanel p1 = new JPanel();
        JLabel label1 = new JLabel("Product OP");
        p1.setBackground(new Color(19, 132, 254));
        label1.setFont(new Font("JetBrains Mono", Font.BOLD, 24));
        p1.add(label1);
        JPanel p2 = new JPanel();// contine Label uri si TextField uri
        JPanel p21 = new JPanel();
        p21.setLayout(new FlowLayout());
        p21.setBackground(new Color(99, 172, 206));
        JPanel p22 = new JPanel();
        p22.setLayout(new FlowLayout());
        p22.setBackground(new Color(99, 172, 206));
        JPanel p23 = new JPanel();
        p23.setLayout(new FlowLayout());
        p23.setBackground(new Color(99, 172, 206));
        JPanel p24 = new JPanel();
        p24.setLayout(new FlowLayout());
        p24.setBackground(new Color(99, 172, 206));
        p2.setBackground(new Color(38, 249, 7, 150));
        p2.setLayout(new GridLayout(4, 1, 4, 4));
        JPanel p3 = new JPanel(); // contine butoanele
        p3.setLayout(new FlowLayout());
        p3.setBackground(new Color(99, 172, 206));
        JPanel p4 = new JPanel(); // contine Tabelul
        p4.setLayout(new FlowLayout());
        p4.setBackground(new Color(99, 172, 206));

        JButton backBtn = new JButton("BACK");
        backBtn.setBackground(new Color(38, 249, 7));
        JButton printAllBtn = new JButton("PRINT");
        printAllBtn.setBackground(new Color(38, 249, 7));
        JButton insertBtn = new JButton("INSERT");
        insertBtn.setBackground(new Color(38, 249, 7));
        JButton deleteBtn = new JButton("DELETE");
        deleteBtn.setBackground(new Color(38, 249, 7));
        JButton updateBtn = new JButton("UPDATE");
        updateBtn.setBackground(new Color(38, 249, 7));
        JLabel l1 = new JLabel("id: ");
        l1.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        JTextField tf1 = new JTextField();
        tf1.setColumns(5);
        JLabel l2 = new JLabel("Name: ");
        l2.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        JTextField tf2 = new JTextField();
        tf2.setColumns(12);
        JLabel l3 = new JLabel("Price: ");
        l3.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        JTextField tf3 = new JTextField();
        tf3.setColumns(20);
        JLabel l4 = new JLabel("Quantity: ");
        l4.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        JTextField tf4 = new JTextField();
        tf4.setColumns(20);
        p21.add(l1);
        p21.add(tf1);
        p22.add(l2);
        p22.add(tf2);
        p23.add(l3);
        p23.add(tf3);
        p24.add(l4);
        p24.add(tf4);
        p2.add(p21);
        p2.add(p22);
        p2.add(p23);
        p2.add(p24);
        p2.setAlignmentX(Component.CENTER_ALIGNMENT);

        p3.add(printAllBtn);
        p3.add(insertBtn);
        p3.add(deleteBtn);
        p3.add(updateBtn);
        p3.add(backBtn);

        printAllBtn.addActionListener(e -> {
            table = new JTable();
            table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Id", "Name", "Price", "Quantity"}));
            table.setBounds(60, 400, 650, 100);
            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            List<Product> tableList = pdao.listAll();

            for (Product p : tableList) {
                int id = p.getId();
                String nume = p.getName();
                int price = p.getPrice();
                int quantity = p.getQuantity();

                tableModel.addRow(new Object[]{id, nume, price, quantity});
            }
            //JScrollPane sp = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            ReflectionExample reflectionExample = new ReflectionExample();
            int nrCols = reflectionExample.retrieveColumnHeaders(tableList).size();
            String[] header = new String[nrCols];

            //retrieve the columns names
            for (int i = 0; i < nrCols; i++) {
                header[i] = reflectionExample.retrieveColumnHeaders(tableList).get(i).toString();
            }
            int nrRows = tableList.size(), i = 0, k = 0;
            String[][] tuple = new String[nrRows][nrCols];
            for (Product t : tableList) { //for each tuple
                k = 0;
                for (Field field : t.getClass().getDeclaredFields()) //retrieve fields
                {
                    field.setAccessible(true);
                    Object value = null;
                    try {
                        value = field.get(t);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                    tuple[i][k] = value.toString();
                    k++;
                }
                i++;
            }

            JTable jTable = new JTable(tuple, header); //create a table
            JScrollPane sp = new JScrollPane(jTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED); //wrap it in a ScrollPane


            p4.removeAll();
            p4.add(sp);
            table.repaint();
            p4.setBackground(new Color(69, 142, 176));
            table.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
            sp.repaint();
            p4.repaint();
            p4.revalidate();
        });
        insertBtn.addActionListener(e -> {
            Product c = new Product(tf2.getText(), Integer.parseInt(tf3.getText()), Integer.parseInt(tf4.getText()));
            ProductDAO.insert(c);
        });
        deleteBtn.addActionListener(e -> {
            ProductDAO.deleteById(Integer.parseInt(tf1.getText()));
        });
        updateBtn.addActionListener(e -> {
            ProductDAO.updateById(Integer.parseInt(tf1.getText()), tf2.getText(), tf3.getText(), tf4.getText());
        });
        backBtn.addActionListener(e -> {
            frProduct.setVisible(false);
            View mainClient = new View();
        });
        panel.setBackground(new Color(69, 142, 176));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(p1);
        panel.add(p2);
        panel.add(p3);
        panel.add(p4);

        frProduct.setContentPane(panel);
        frProduct.setVisible(true);
    }
}
