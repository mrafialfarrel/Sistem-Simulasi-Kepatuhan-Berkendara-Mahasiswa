/*TUBES*/

package tugasbesar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {
    private boolean isAnimating = false;
    private boolean isFallen = false; 

    private int roadLineOffsetY = 0;
    private Timer timer;
    private String currentStatus = "Siap Berangkat";
    private Color statusColor = Color.WHITE;

    private int helmetType = 0;
    private int accidentType = 0;
    private boolean isShaking = false;
    private Random rand = new Random();

    private int riderCurrentY;
    private int riderTargetY;
    private final int RIDER_START_Y_OFFSET = 150; 

    public GamePanel() {
        setBackground(new Color(60, 60, 60));
        timer = new Timer(16, e -> updateAnimation());
        resetState();
    }

    public void resetState() {
        this.isAnimating = false;
        this.isFallen = false;
        this.isShaking = false;
        this.accidentType = 0;
        this.currentStatus = "Siap Berangkat";
        this.statusColor = Color.WHITE;
        this.riderCurrentY = getHeight() - RIDER_START_Y_OFFSET;
        repaint();
    }

    public void startAnimation(int helmetType) {
        resetState();
        this.helmetType = helmetType;
        this.isAnimating = true;
        this.currentStatus = "Sedang Berkendara ...";
        this.statusColor = Color.YELLOW;
        this.riderTargetY = getHeight() / 2 + 50;
        riderCurrentY = getHeight() + 100; 
        timer.start();
    }

    public void setHelmetType(int type) {
        this.helmetType = type;
        repaint();
    }

    public void triggerAccidentVisual(int type) {
        this.accidentType = type;
        this.isShaking = true;
        this.statusColor = Color.RED;
        this.currentStatus = "AWAS!! TABRAKAN!";
        this.riderCurrentY = this.riderTargetY;
        repaint();
    }

    public void showFallenState() {
        this.isAnimating = false;
        this.isShaking = false;
        this.isFallen = true;
        this.currentStatus = "KECELAKAAN! Terkapar di Jalan.";
        this.statusColor = Color.RED;
        timer.stop();
        repaint();
    }

    public void stopAnimation(String result, boolean isGood) {
        this.isAnimating = false;
        this.isShaking = false;
        if (isGood) this.isFallen = false;
        this.currentStatus = result;
        this.statusColor = isGood ? Color.GREEN : Color.RED;
        this.riderCurrentY = this.riderTargetY;
        timer.stop();
        repaint();
    }

    private void updateAnimation() {
        if (isAnimating) {
            roadLineOffsetY = (roadLineOffsetY + 15) % 60;

            if (riderCurrentY > riderTargetY) {
                riderCurrentY -= 4; 
                if (riderCurrentY < riderTargetY) riderCurrentY = riderTargetY;
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;

        drawTopDownRoad(g2d, width, height);

        if (isShaking && !isFallen) {
            int shakeX = rand.nextInt(14) - 7;
            int shakeY = rand.nextInt(14) - 7;
            g2d.translate(shakeX, shakeY);
        }

        if (riderCurrentY == 0) riderCurrentY = height - RIDER_START_Y_OFFSET;

        int hazardY = riderCurrentY - 180;

        if (accidentType > 0) {
            drawTopDownHazard(g2d, centerX, hazardY);
        }

        if (isFallen) {
            drawTopDownFallenScene(g2d, centerX, riderCurrentY);
        } else {
            drawTopDownMotor(g2d, centerX, riderCurrentY);
            drawTopDownRider(g2d, centerX, riderCurrentY, helmetType);
        }

        if (isShaking && !isFallen) {
            g2d.setTransform(g2d.getDeviceConfiguration().getDefaultTransform());
        }
        drawStatusText(g2d);
    }


    private void drawTopDownRoad(Graphics2D g2d, int width, int height) {
        g2d.setColor(new Color(34, 139, 34)); 
        g2d.fillRect(0, 0, width, height);

        int roadWidth = 300;
        int roadLeft = (width - roadWidth) / 2;
        g2d.setColor(new Color(60, 60, 60)); 
        g2d.fillRect(roadLeft, 0, roadWidth, height);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(roadLeft + 10, 0, roadLeft + 10, height);
        g2d.drawLine(roadLeft + roadWidth - 10, 0, roadLeft + roadWidth - 10, height);

        g2d.setColor(Color.WHITE);
        int dashHeight = 40;
        int gap = 20;
        for (int y = -dashHeight; y < height; y += (dashHeight + gap)) {
            g2d.fillRect(width / 2 - 2, y + roadLineOffsetY, 4, dashHeight);
        }
    }

    private void drawTopDownMotor(Graphics2D g2d, int x, int y) {
        int motoWidth = 30;
        int motoLength = 70;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(x - 8, y + motoLength / 2 - 5, 16, 15);

        g2d.fillRect(x - 6, y - motoLength / 2 - 10, 12, 15);

        g2d.setColor(new Color(180, 0, 0)); 
        RoundRectangle2D body = new RoundRectangle2D.Float(x - motoWidth / 2, y - motoLength / 2, motoWidth, motoLength, 15, 15);
        g2d.fill(body);

        g2d.setColor(new Color(220, 0, 0));
        RoundRectangle2D tank = new RoundRectangle2D.Float(x - motoWidth / 2 + 2, y - motoLength / 2 + 5, motoWidth - 4, 25, 10, 10);
        g2d.fill(tank);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(x - 22, y - motoLength / 2 + 5, x + 22, y - motoLength / 2 + 5);
    }

    private void drawTopDownRider(Graphics2D g2d, int x, int y, int helmetType) {
        g2d.setColor(Color.BLUE); 
        g2d.fill(new Ellipse2D.Float(x - 18, y - 15, 36, 20));

        g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(x - 15, y - 10, x - 20, y - 25); 
        g2d.drawLine(x + 15, y - 10, x + 20, y - 25); 

        int helmetSize = 26;
        int helmetX = x - helmetSize / 2;
        int helmetY = y - 10;

        if (helmetType == 0) { 
            g2d.setColor(new Color(30, 30, 30));
            g2d.fill(new Ellipse2D.Float(helmetX + 2, helmetY + 2, helmetSize - 4, helmetSize - 4));
            g2d.setColor(new Color(255, 200, 150));
             g2d.fill(new Ellipse2D.Float(helmetX + 8, helmetY + 18, 10, 5));
        } else {
            Color helmetColor;
            if (helmetType == 1) helmetColor = Color.GRAY; 
            else if (helmetType == 2) helmetColor = Color.WHITE; 
            else helmetColor = Color.RED; 

            g2d.setColor(helmetColor);
            g2d.fill(new Ellipse2D.Float(helmetX, helmetY, helmetSize, helmetSize));

            if (helmetType > 1) {
                g2d.setColor(new Color(50, 50, 50, 150));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawArc(helmetX + 3, helmetY + 2, helmetSize - 6, helmetSize - 6, 45, 90);
            }
        }
    }

    private void drawTopDownFallenScene(Graphics2D g2d, int x, int y) {
        Graphics2D gMotor = (Graphics2D) g2d.create();
        gMotor.rotate(Math.toRadians(60), x, y); 
        drawTopDownMotor(gMotor, x, y);
        gMotor.dispose();

        int bodyX = x + 50; 
        int bodyY = y + 10;

        Graphics2D gBody = (Graphics2D) g2d.create();
        gBody.translate(bodyX, bodyY);
        gBody.rotate(Math.toRadians(-45)); 

        
        gBody.setColor(new Color(0, 0, 150));
        gBody.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        gBody.drawLine(-5, 20, -10, 50);
        gBody.drawLine(5, 20, 15, 40);
        gBody.drawLine(15, 40, 25, 45);

        gBody.setColor(Color.BLUE);
        gBody.drawLine(-10, -20, -25, -35);
        gBody.drawLine(10, -20, 20, -10);

        gBody.fill(new RoundRectangle2D.Float(-15, -25, 30, 50, 10, 10));

        gBody.setColor(new Color(255, 200, 150)); 
        gBody.fillOval(-10, -38, 20, 20); 
        gBody.setColor(new Color(30, 30, 30)); 
        gBody.fillOval(-10, -40, 20, 15);

        gBody.dispose(); 

        if (helmetType > 0) {
            int helmX = bodyX + 25; 
            int helmY = bodyY - 30;
            
            Graphics2D gHelm = (Graphics2D) g2d.create();
            gHelm.translate(helmX, helmY);
            gHelm.rotate(Math.toRadians(30)); 
            
            drawLooseHelmet(gHelm, helmetType); 
            gHelm.dispose();
        }

        drawImpactEffect(g2d, x, y);
    }

    private void drawLooseHelmet(Graphics2D g2d, int type) {
        int size = 26;
        int offset = -size/2; 

        Color c;
        if (type == 1) c = Color.GRAY;
        else if (type == 2) c = Color.WHITE;
        else c = Color.RED;

        g2d.setColor(c);
        g2d.fill(new Ellipse2D.Float(offset, offset, size, size));

        g2d.setColor(new Color(40,40,40));
        g2d.fillOval(offset+5, offset+5, size-10, size-10);

        if (type > 1) {
            g2d.setColor(new Color(100, 200, 255, 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawArc(offset, offset, size, size, 0, 180);
        }
    }

    private void drawTopDownHazard(Graphics2D g2d, int x, int y) {
        if (accidentType == 1) { 
            g2d.setColor(new Color(34, 139, 34)); 
            g2d.fill(new RoundRectangle2D.Float(x - 40, y - 60, 80, 120, 5, 5));
            g2d.setColor(new Color(100, 200, 100));
             g2d.fillRect(x - 35, y - 55, 70, 15);

        } else if (accidentType == 2) { 
            g2d.setColor(new Color(30, 30, 30));
            g2d.fill(new Ellipse2D.Float(x - 30, y - 15, 60, 30));
            g2d.fill(new Ellipse2D.Float(x - 15, y - 25, 40, 40));

        } else if (accidentType == 3) {
            g2d.setColor(new Color(230, 126, 34));
            g2d.fill(new Ellipse2D.Float(x - 10, y, 20, 35));
            g2d.fill(new Ellipse2D.Float(x - 8, y - 12, 16, 16));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawArc(x - 5, y + 30, 15, 20, 180, 180);
        }
        
        if(isShaking && !isFallen) {
             drawImpactEffect(g2d, x, y + 30);
        }
    }

    private void drawImpactEffect(Graphics2D g, int x, int y) {
        g.setColor(Color.YELLOW);
        for(int i=0; i<8; i++) {
            Graphics2D gStar = (Graphics2D)g.create();
            gStar.rotate(Math.toRadians(i*45), x, y);
            gStar.fillPolygon(new int[]{x, x-10, x, x+10}, new int[]{y-20, y-5, y+20, y-5}, 4);
            gStar.dispose();
        }
    }

    private void drawStatusText(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2d.setColor(Color.BLACK);
        g2d.drawString(currentStatus, 22, 42);
        g2d.setColor(statusColor);
        g2d.drawString(currentStatus, 20, 40);
    }
}