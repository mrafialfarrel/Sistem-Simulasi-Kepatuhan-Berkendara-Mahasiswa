/*TUBES*/

package tugasbesar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class MainFrame extends JFrame {
    public JButton btnRide;
    public JButton btnWork;
    public JButton btnRest;
    public JButton btnBuyHelmet;
    public JProgressBar barHealth;
    public JProgressBar barStress;
    public JLabel lblMoney;
    public JLabel lblSemester;
    public JLabel lblIpk;
    public JTextArea txtLog;
    public GamePanel gamePanel;
    public JComboBox<String> cmbShop;

    public MainFrame() {
        setTitle("Sistem Simulasi Kepatuhan Berkendara Mahasiswa (SKBM)");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initUI();
    }

    public void initUI() {
        JPanel leftPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Status Mahasiswa"));
        leftPanel.setPreferredSize(new Dimension(220, 0));

        lblSemester = new JLabel("Semester: 1");
        lblIpk = new JLabel("IPK: 4.0");
        lblMoney = new JLabel("Saldo: Rp 500.000");

        barHealth = new JProgressBar(0, 100);
        barHealth.setValue(100);
        barHealth.setStringPainted(true);
        barHealth.setForeground(Color.RED);
        barHealth.setBorder(BorderFactory.createTitledBorder("Kesehatan Fisik"));

        barStress = new JProgressBar(0, 100);
        barStress.setValue(0);
        barStress.setStringPainted(true);
        barStress.setForeground(Color.BLUE);
        barStress.setBorder(BorderFactory.createTitledBorder("Tingkat Stress"));

        leftPanel.add(lblSemester);
        leftPanel.add(lblIpk);
        leftPanel.add(lblMoney);
        leftPanel.add(barHealth);
        leftPanel.add(barStress);

        add(leftPanel, BorderLayout.WEST);

        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setPreferredSize(new Dimension(280, 0));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel shopPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        shopPanel.setBorder(BorderFactory.createTitledBorder("Toko Helm"));
        shopPanel.setPreferredSize(new Dimension(0, 120));

        cmbShop = new JComboBox<>();
        cmbShop.addItem("Helm Batok (Rp 50rb)");
        cmbShop.addItem("Helm Standar SNI (Rp 200rb)");
        cmbShop.addItem("Helm Full Face (Rp 500rb)");

        btnBuyHelmet = new JButton("Beli Helm");

        shopPanel.add(new JLabel("Pilih Helm: "));
        shopPanel.add(cmbShop);
        shopPanel.add(btnBuyHelmet);

        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);
        txtLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        
        JScrollPane scroll = new JScrollPane(txtLog);
        scroll.setBorder(BorderFactory.createTitledBorder("Riwayat Kejadian"));

        rightPanel.add(scroll, BorderLayout.CENTER);
        rightPanel.add(shopPanel, BorderLayout.NORTH);

        add(rightPanel, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.setPreferredSize(new Dimension(0, 60)); 

        btnRide = new JButton("Berangkat Kuliah");
        btnRide.setFont(new Font("Arial", Font.BOLD, 16));
        btnRide.setBackground(new Color(46, 204, 113));

        btnWork = new JButton("Kerja Part-Time (+Uang)");
        btnRest = new JButton("Istirahat (-Stress)");

        bottomPanel.add(btnWork);
        bottomPanel.add(btnRide);
        bottomPanel.add(btnRest);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void appendLog(String text) {
        txtLog.append("> " + text + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }
}