/*TUBES*/

package tugasbesar;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private double health;
    private double money;
    private double stress;
    private double ipk;
    private int attendance;
    private int semester;
    private Helmet currentHelmet;
    private List<Helmet> inventory;

    public Player() {
        this.health = 100.0;
        this.money = 500000.0;
        this.stress = 0;
        this.ipk = 4.0;
        this.attendance = 0;
        this.semester = 1;
        this.inventory = new ArrayList<>();
        this.currentHelmet = null;
    }

    public void buyHelmet(Helmet helmet) {
        if (money >= helmet.getPrice()) {
            money -= helmet.getPrice();
            inventory.add(helmet);
        }
    }

    public void equipHelmet(Helmet helmet) {
        this.currentHelmet = helmet;
    }

    public void takeDamage(double amount) {
        if (currentHelmet != null) {
            double reduction = (amount * currentHelmet.getProtectionLevel()) / 100.0;
            amount -= reduction;
            stress += 5;
        } else {
            amount *= 1.5;
        }
        this.health -= amount;
        if (this.health < 0) this.health = 0;
    }

    public void heal(double amount) {
        this.health += amount;
        if (this.health > 100) this.health = 100;
    }

    public void reduceStress(double amount) {
        this.stress -= amount;
        if (this.stress < 0) this.stress = 0;
    }

    public void earnMoney(double amount) {
        this.money += amount;
    }

    public boolean processTrip() {
        attendance++;
        if (attendance >= 2) {
            semester++;
            attendance = 0;
            updateIPK();
            return true;
        }
        return false;
    }

    private void updateIPK() {
        if (stress > 70) {
            ipk -= 0.3; 
        } else if (stress > 30) {
            ipk -= 0.1;
        } else {
            ipk += 0.2;
        }

        if (ipk > 4.0) ipk = 4.0;
        if (ipk < 0.0) ipk = 0.0;
    }

    public double getHealth() {
        return health;
    }
    public double getMoney() {
        return money;
    }
    public double getStress() {
        return stress;
    }
    public double getIpk() {
        return ipk;
    }
    public int getAttendance() {
        return attendance;
    }
    public int getSemester() {
        return semester;
    }
    public Helmet getCurrentHelmet() {
        return currentHelmet;
    }
    public List<Helmet> getInventory() {
        return inventory;
    }
}