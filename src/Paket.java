public class Paket {
    private String name, beschreibung;
    private int kosten, servicedauer;

    public Paket(String name, String beschreibung, int kosten, int servicedauer) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.kosten = kosten;
        this.servicedauer = servicedauer;
    }

    public String getName() {
        return name;
    }


    public int getKosten() {
        return kosten;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n\t- " + beschreibung + "\n\t- Kosten (in Euro): " + kosten +  "\n\t- Servicedauer: " + servicedauer + " Jahre";
    }
}
