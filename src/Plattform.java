public class Plattform {
    private String name, farbe;
    private int kosten, lieferzeit;

    public Plattform(String name, String farbe, int kosten, int lieferzeit) {
        this.name = name;
        this.farbe = farbe;
        this.kosten = kosten;
        this.lieferzeit = lieferzeit;
    }

    public String getName() {
        return name;
    }

    public int getKosten() {
        return kosten;
    }

    public int getLieferzeit() {
        return lieferzeit;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n\t- Standardfarbe: " + farbe + "\n\t- Kosten (in Euro): " + kosten + "\n\t- Lieferzeit: " + lieferzeit + " Monate";
    }
}
