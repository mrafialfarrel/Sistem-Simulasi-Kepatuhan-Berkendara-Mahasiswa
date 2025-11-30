/*TUBES*/

package tugasbesar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;

public class GraduationFrame extends JFrame {

    public GraduationFrame(double finalIpk, double finalMoney) {
        setTitle("Happy Graduation!");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(230, 240, 255)); 
        mainPanel.setLayout(new GridLayout(4, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("HAPPY GRADUATION!", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 36));
        lblTitle.setForeground(new Color(50, 50, 150)); 

        JLabel lblSubtitle = new JLabel("Selamat! Anda lulus Semester 8.", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        statsPanel.setOpaque(false);
        
        JLabel lblIpk = new JLabel("IPK Akhir: " + String.format("%.2f", finalIpk), SwingConstants.CENTER);
        lblIpk.setFont(new Font("Arial", Font.BOLD, 24));
        lblIpk.setForeground(new Color(0, 100, 0));

        JLabel lblMoney = new JLabel("Sisa Saldo: Rp " + (int)finalMoney, SwingConstants.CENTER);
        lblMoney.setFont(new Font("Monospaced", Font.PLAIN, 18));

        statsPanel.add(lblIpk);
        statsPanel.add(lblMoney);

        JButton btnExit = new JButton("Terima Ijazah & Keluar");
        btnExit.setFont(new Font("Arial", Font.BOLD, 14));
        btnExit.setBackground(new Color(46, 204, 113)); 
        btnExit.setForeground(Color.WHITE);
        
        btnExit.addActionListener(e -> System.exit(0));

        mainPanel.add(lblTitle);
        mainPanel.add(lblSubtitle);
        mainPanel.add(statsPanel);
        mainPanel.add(btnExit);

        add(mainPanel, BorderLayout.CENTER);
    }
}