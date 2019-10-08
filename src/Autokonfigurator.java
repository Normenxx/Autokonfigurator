import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Autokonfigurator {

    private static ArrayList<Plattform> vordefiniertePlattformen = new ArrayList<>();
    private static ArrayList<Paket> vordefiniertePakete = new ArrayList<>();

    private static Plattform ausgewaehltePlattform;
    private static ArrayList<Paket> ausgewaehltePakete = new ArrayList<>();

    public static void main(String[] args) {
        //Test
      Autokonfigurator autokonfigurator = new Autokonfigurator();
      autokonfigurator.menu();
    }

    private  void menu(){
        Scanner scanner = new Scanner(System.in);
        String eingabe = "";
        datenLaden();

        if (!(vordefiniertePlattformen.size() == 0 || vordefiniertePakete.size() == 0)) {

            /*
        0 Plattform wählen
        1 Paket wählen
        2 Ergebnis anzeigen
         */
            int status = 0;
            boolean fertig = false;
            while (!fertig) {
                switch (status) {
                    case 0:
                        anzeigenStatus();
                        anzeigenPlattformen();

                        System.out.println("Geben Sie den Namen der Plattform ein oder schreiben Sie 'weiter':");
                        eingabe = scanner.nextLine();
                        Plattform gesuchtePlattform = sucheElement(eingabe, "plattform");
                        if (gesuchtePlattform != null) {
                            ausgewaehltePlattform = gesuchtePlattform;
                        } else if (eingabe.equals("weiter")) {
                            status = 1;
                        } else {
                            System.out.println("Name wurde falsch eingegeben!");
                        }
                        break;
                    case 1:
                        anzeigenStatus();
                        anzeigenPakete();

                        System.out.println("Geben Sie den Namen des Pakets ein oder schreiben Sie 'weiter' oder schreiben Sie 'zurueck':");
                        eingabe = scanner.nextLine();
                        Paket gesuchtePakete = sucheElement(eingabe, "pakete");
                        if (gesuchtePakete != null && ausgewaehltePakete.contains(gesuchtePakete)) {
                            ausgewaehltePakete.remove(gesuchtePakete);
                        } else if (gesuchtePakete != null) {
                            ausgewaehltePakete.add(gesuchtePakete);
                        } else if (eingabe.equals("weiter")) {
                            status = 2;
                        } else if (eingabe.equals("zurueck")) {
                            status = 0;
                        } else {
                            System.out.println("Name wurde falsch eingegeben!");
                        }
                        break;
                    case 2:
                        if (ausgewaehltePlattform == null || ausgewaehltePakete.size() <= 0) {
                            System.out.println("Auswahl ungültig");
                            status = 1;
                        } else {
                            anzeigenBestellung();
                            System.out.println("Schreiben Sie 'fertig' um die Bestellung zu bestätigen oder schreiben Sie 'zurueck':");
                            eingabe = scanner.nextLine();

                            if (eingabe.equals("fertig")) {
                                status = 3;
                            } else if (eingabe.equals("zurueck")) {
                                status = 1;
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Bestellung wurde versendet.");
                        fertig = true;
                        break;
                }
            }
        }
    }

    //Zeigt alle Ausgewählten Pakete oder Plattformen an
    private static void anzeigenStatus() {
        System.out.println("----------------------------------------------");
        System.out.println("Ausgewaehlte Plattform:");
        if (ausgewaehltePlattform == null) {
            System.out.println("-");
        } else {
            System.out.println(ausgewaehltePlattform.toString());
        }
        System.out.println("Ausgewaehlte Pakete:");
        if (ausgewaehltePakete.size() == 0) {
            System.out.println("-");
        } else {
            for (Paket paket : ausgewaehltePakete) {
                System.out.println(paket.toString());
            }
        }
        System.out.println("----------------------------------------------");
    }

    //Zeigt die Bestellung an
    private static void anzeigenBestellung() {
        System.out.println("\n\nBestellung");
        System.out.println("Kosten (in Euro): " + kostenrechner());
        System.out.println("Lieferzeit (in Monate): " + ausgewaehltePlattform.getLieferzeit());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, ausgewaehltePlattform.getLieferzeit());
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        System.out.println("Lieferdatum: " + dateFormat.format(date));

        System.out.println("Plattform:");
        System.out.println(ausgewaehltePlattform.toString());

        System.out.println("Pakete:");
        for (Paket paket : ausgewaehltePakete) {
            System.out.println(paket.toString());
        }

    }

    //Zeige alle Pakete an
    private static void anzeigenPakete() {
        System.out.println("----------------------------------------------");
        System.out.println("Moegliche Pakete:");
        for (Paket paket : vordefiniertePakete) {
            System.out.println(paket.toString());
        }
        System.out.println("----------------------------------------------");
    }

    //Zeige alle Plattform  an
    private static void anzeigenPlattformen() {
        System.out.println("----------------------------------------------");
        System.out.println("Moegliche Plattformen:");
        for (Plattform plattform : vordefiniertePlattformen) {
            System.out.println(plattform.toString());
        }
        System.out.println("----------------------------------------------");
    }

    //Sucht ein Element in den vordefinierten Listen
    private static <t> t sucheElement(String gesucht, String typ) {
        if (typ.toLowerCase().equals("plattform")) {
            for (Plattform plattform : vordefiniertePlattformen) {
                if (plattform.getName().equals(gesucht)) {
                    return (t) plattform;
                }
            }
        } else {
            for (Paket paket : vordefiniertePakete) {
                if (paket.getName().equals(gesucht)) {
                    return (t) paket;
                }
            }
        }
        return null;
    }

    //Ladet Informationen aus den Txt Dateien und erstellt die vordefinierten Arraylisten
    private static void datenLaden() {
        Charset zeichensatz = Charset.forName("ISO-8859-1");

        Path pfad1 =  Path.of("config","plattform.txt");
        try (BufferedReader reader = Files.newBufferedReader(pfad1, zeichensatz)) {
            vordefiniertePlattformen = new ArrayList<>();
            String zeile = reader.readLine();
            while (zeile != null) {
                String daten[] = zeile.split(";");

                Plattform plattform = new Plattform(daten[0], daten[1], Integer.parseInt(daten[2]), Integer.parseInt(daten[3]));
                vordefiniertePlattformen.add(plattform);
                zeile = reader.readLine();
            }
        } catch (NoSuchFileException e) {
            System.err.println("plattform.txt konnte nicht gefunden werden.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path pfad2 = Path.of("config","pakete.txt");
        try (BufferedReader reader = Files.newBufferedReader(pfad2, zeichensatz)) {
            vordefiniertePakete = new ArrayList<>();
            String zeile = reader.readLine();
            while (zeile != null) {
                String daten[] = zeile.split(";");

                Paket paket = new Paket(daten[0], daten[1], Integer.parseInt(daten[2]), Integer.parseInt(daten[3]));
                vordefiniertePakete.add(paket);
                zeile = reader.readLine();
            }
        } catch (NoSuchFileException e) {
            System.err.println("pakete.txt konnte nicht gefunden werden.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Gesamtkosten berechnen
    private static int kostenrechner() {
        int gesamtpreis = 0;
        gesamtpreis += ausgewaehltePlattform.getKosten();

        for (Paket paket : ausgewaehltePakete) {
            gesamtpreis += paket.getKosten();
        }
        return gesamtpreis;
    }

}
