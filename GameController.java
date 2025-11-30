/*TUBES*/

package tugasbesar;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameController {
    private Player player;
    private MainFrame view;
    private Random rng;

    public GameController(Player player, MainFrame view) {
        this.player = player;
        this.view = view;
        this.rng = new Random();

        initController();
        updateView();
    }

    private void initController() {
        view.btnRide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTripSimulation();
            }
        });

        view.btnBuyHelmet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buyHelmetLogic();
            }
        });

        view.btnWork.addActionListener(e -> {
            startWorkMiniGame();
        });

        view.btnRest.addActionListener(e -> {
            if (player.getStress() > 0 || player.getHealth() < 100) {
                player.reduceStress(20);
                player.heal(15);
                view.appendLog("Tidur nyenyak. Sehat++, Stress--");
            } else {
                view.appendLog("Kondisi prima! Tidak butuh istirahat.");
            }
            updateView();
        });
    }

    private void startWorkMiniGame() {
        String[] jobs = {"Kasir Cafe", "Asisten Dosen", "Data Entry", "Freelance Coding"};
        int jobIndex = rng.nextInt(jobs.length);
        String jobTitle = jobs[jobIndex];

        int a = rng.nextInt(20) + 1;
        int b = rng.nextInt(10) + 1;
        int correctAnswer = 0;
        String question = "";

        if (rng.nextBoolean()) {
            correctAnswer = a + b;
            question = "Hitung: " + a + " + " + b + " = ?";
        } else {
            correctAnswer = a * b;
            question = "Hitung: " + a + " x " + b + " = ?";
        }

        String input = JOptionPane.showInputDialog(view, "Pekerjaan: " + jobTitle + "\n" + question, "Tantangan Kerja", JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.isEmpty()) {
            try {
                int userAnswer = Integer.parseInt(input);

                if (userAnswer == correctAnswer) {
                    int gaji = 50000 + rng.nextInt(20000);
                    player.earnMoney(gaji);
                    player.reduceStress(-5);

                    view.appendLog("[" + jobTitle + "] Sukses, Gaji: + Rp " + gaji);
                } else {
                    view.appendLog("[" + jobTitle + "] Salah Hitung, Bos Marah");
                    player.reduceStress(-10);
                }
            } catch (NumberFormatException ex) {
                view.appendLog("Kerja Gagal. Anda Mengantuk");
            }
        } else {
            view.appendLog("Membatalkan Pekerjaan");
        }
        updateView();
    }

    private void buyHelmetLogic() {
        int selectedIndex = view.cmbShop.getSelectedIndex();
        Helmet item = null;

        switch (selectedIndex) {
            case 0: item = new Helmet("Helm Batok", 10, 50000, 5); break;
            case 1: item = new Helmet("Helm SNI", 50, 200000, 15); break;
            case 2: item = new Helmet("Helm Full Face", 90, 500000, 30); break;
        }

        if (item != null) {
            if (player.getMoney() >= item.getPrice()) {
                player.buyHelmet(item);
                player.equipHelmet(item);
                view.appendLog("Membeli & Memakai: " + item.getName());
            } else {
                JOptionPane.showMessageDialog(view, "Uang tidak cukup!");
            }
        }
        updateView();
    }

    private void startTripSimulation() {
        view.btnRide.setEnabled(false);

        view.gamePanel.resetState();

        int helmetCode = getHelmetTypeCode();
        view.gamePanel.startAnimation(helmetCode);
        view.appendLog("Berangkat ke Kampus ...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(2000);
                return null;
            }

            @Override
            protected void done() {
                calculateTripResult(player.getCurrentHelmet() != null);
                view.btnRide.setEnabled(true);
                updateView();
            }
        };
        worker.execute();
    }

    private void calculateTripResult(boolean wearingHelmet) {
        int hazardChance = rng.nextInt(100);
        boolean accident = hazardChance < 50; 

        if (accident) {
            int hazardType = rng.nextInt(3);
            String eventName = "";
            int visualCode = 0;

            switch (hazardType) {
                case 0: eventName = "Diserempet Angkot!"; visualCode = 1; break;
                case 1: eventName = "Hantam Jalan Berlubang!"; visualCode = 2; break;
                case 2: eventName = "Kucing Oren Nyebrang!"; visualCode = 3; break;
            }

            view.gamePanel.triggerAccidentVisual(visualCode);

            double baseDamage = 30 + rng.nextInt(20); 
            if (!wearingHelmet) baseDamage *= 1.5;

            double oldHealth = player.getHealth();
            player.takeDamage(baseDamage);
            double actualDamage = oldHealth - player.getHealth();

            String finalEventName = eventName;

            Timer pauseTimer = new Timer(800, e -> {
                ((Timer)e.getSource()).stop();

                view.gamePanel.showFallenState();
            
                view.appendLog("BAHAYA: " + finalEventName);
                
                if (wearingHelmet) {
                    view.appendLog("Helm melindungi. Luka: -" + (int)actualDamage);
                } else {
                    view.appendLog("KEPALA BOCOR! Luka: -" + (int)actualDamage);
                }
                
                checkGameOver();
                handleSemesterProgression(); 
                updateView();
            });
            pauseTimer.setRepeats(false);
            pauseTimer.start();
        } else {
            view.gamePanel.stopAnimation("Sampai Tujuan", true);
            view.appendLog("Sampai Kampus dengan Aman.");
            player.reduceStress(5);
            handleSemesterProgression(); 
            updateView();
        }
    }
    private void handleSemesterProgression() {
        boolean naikSemester = player.processTrip(); 
        
        if (player.getSemester() > 8) {
            view.dispose();
            GraduationFrame gradFrame = new GraduationFrame(player.getIpk(), player.getMoney());
            gradFrame.setVisible(true);
            return;
        }
        if (naikSemester) {
            view.appendLog("--------------------------------");
            view.appendLog("SELAMAT! NAIK KE SEMESTER " + player.getSemester());
            view.appendLog("IPK telah diperbarui.");
            view.appendLog("--------------------------------");
        } else {
            int sisa = 2 - player.getAttendance();
            view.appendLog("(Butuh " + sisa + "x berangkat lagi utk naik semester)");
        }
    }

    private int getHelmetTypeCode() {
        if (player.getCurrentHelmet() == null) return 0;
        
        String name = player.getCurrentHelmet().getName();
        if (name.contains("Batok")) return 1;
        if (name.contains("SNI")) return 2;
        if (name.contains("Full Face")) return 3;
        return 0;
    }

    private void checkGameOver() {
        if (player.getHealth() <= 0) {
            JOptionPane.showMessageDialog(view, "GAME OVER\nAnda masuk Rumah Sakit dalam kondisi Kritis", "Kecelakaan Fatal", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void updateView() {
        view.lblMoney.setText("Saldo: Rp " + (int)player.getMoney());
        view.lblSemester.setText("Semester: " + player.getSemester());
        view.lblIpk.setText(String.format("IPK: %.2f", player.getIpk()));

        view.barHealth.setValue((int)player.getHealth());
        view.barStress.setValue((int)player.getStress());

        view.gamePanel.setHelmetType(getHelmetTypeCode());

        if (player.getCurrentHelmet() != null) {
            view.setTitle("SKBM - Menggunakan: " + player.getCurrentHelmet().getName());
        } else {
            view.setTitle("SKBM - TIDAK MENGGUNAKAN HELM");
        }
    }
}