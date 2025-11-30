/*TUBES*/

package tugasbesar;

public class Helmet {
    private String name;
    private int protectionLevel;
    private int price;
    private int stylePoints;

    public Helmet(String name, int protectionLevel, int price, int stylePoints) {
        this.name = name;
        this.protectionLevel = protectionLevel;
        this.price = price;
        this.stylePoints = stylePoints;
    }

    public String getName() {
        return name;
    }
    public int getProtectionLevel() {
        return protectionLevel;
    }
    public int getPrice() {
        return price;
    }
    public int getStylePoints() {
        return stylePoints;
    }

    @Override
    public String toString() {
        return name + "(Lindung: " + protectionLevel + "%, Gaya: " + stylePoints + ")";
    }
}