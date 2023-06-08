package presentation;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame{
    JFrame frMain = new  JFrame("Magazin");//fereastra - termopan
    //JFrame frClient = new  JFrame("Client OP");
    //JFrame frProduct = new  JFrame("Product OP");
    //JFrame frOrder = new  JFrame("Order OP");

    // frMain:

    JPanel p1 = new JPanel();
    JLabel label1 = new JLabel("Management Magazin");//panelul p1 e format doar dintr-un sir de caractere
    JPanel p2 = new JPanel();//pannel 2 contine 3 butoane

    JButton clientBtn = new JButton("Client OP");
    JButton produsBtn = new JButton("Product OP");
    JButton orderBtn = new JButton("Order OP");

    JPanel panel = new JPanel();//o sa vina adaugate p1 si p2 in el, si el se afiseaza in fereastra

    public View(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // frMain:
        this.setLocation(600,250);
        this.add(p1);
        this.add(p2);
        this.setSize(350, 200);
        p1.setBackground(new Color(99,172,206));
        label1.setFont(new Font("JetBrains Mono", Font.BOLD, 24));
        p1.add(label1);

        p2.setBackground(new Color(99,172,206));
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));

        clientBtn.setFont(new Font("JetBrains Mono", Font.BOLD, 17));
        clientBtn.setBackground(new Color(38, 249, 7));
        clientBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        produsBtn.setFont(new Font("JetBrains Mono", Font.BOLD, 17));
        produsBtn.setBackground(new Color(38, 249, 7));
        produsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        orderBtn.setFont(new Font("JetBrains Mono", Font.BOLD, 17));
        orderBtn.setBackground(new Color(38, 249, 7));
        orderBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        clientBtn.addActionListener(e -> {
            this.setVisible(false);
            ClientView frClient = new ClientView();
        });
        produsBtn.addActionListener(e -> {
            this.setVisible(false);
            ProductView frProduct = new ProductView();
        });
        orderBtn.addActionListener(e -> {
            this.setVisible(false);
            OrderView frOrder = new OrderView();
        });

        p2.add(clientBtn);
        p2.add(produsBtn);
        p2.add(orderBtn);

        panel.setBackground(new Color(69,142,176));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(p1);
        panel.add(p2);
        this.setContentPane(panel);
        this.setVisible(true);
    }
    public static void main(String[] args){
        View view1 = new View();
    }
}
