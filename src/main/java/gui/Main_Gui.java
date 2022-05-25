/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import data.DopravniLetadlo;
import data.DopravniProstredek;
import data.StihaciLetoun;
import data.TypProstredku;
import data.Vrtulnik;
import java.util.Iterator;
import java.util.function.Consumer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kolekce.KolekceException;
import spravce.Ovladac;

/**
 *
 * @author HP
 */
public class Main_Gui extends Application {

    Ovladac ovladac = new Ovladac();
    AnchorPane root = new AnchorPane();
    ListView listView = new ListView();
    Consumer<String> alert = e -> {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(e);
        alert.setHeaderText("Chyba");
        alert.showAndWait();
    };
    Consumer<String> info = e -> {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(e);
        alert.setHeaderText("Info");
        alert.showAndWait();
    };

    ObservableList<DopravniProstredek> list = FXCollections.observableArrayList();
    private static final String BINARNI_SOUBOR = "seznam.bin";
    private static final String TEXTOVY_SOUBOR = "seznam.txt";

    @Override
    public void start(Stage stage) throws Exception {

        listView.setMinWidth(900);
        listView.setMinHeight(700);
        listView.setItems(list);
        root.setLeftAnchor(listView, 10.0);
        root.setRightAnchor(listView, 100.0);
        root.getChildren().add(listView);
        setHBox();
        setVBox();
        Scene scene = new Scene(root, 1100, 750);
        stage.setTitle("Půjčovna");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setHBox() {
        HBox hbox = new HBox();
        hbox.setSpacing(15);
        ObservableList<TypProstredku> typy = FXCollections.observableArrayList(TypProstredku.values());
        ComboBox novyBox = new ComboBox();
        ComboBox filtrBox = new ComboBox();
        novyBox.setItems(typy);
        filtrBox.setItems(typy);
        novyBox.getSelectionModel().selectFirst();
        filtrBox.getSelectionModel().selectFirst();
        Spinner<Integer> spinnerGen = new Spinner<Integer>();
        spinnerGen.setMaxWidth(60);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000);
        spinnerGen.setValueFactory(valueFactory);

        Button generuj = new Button("Generuj");
        generuj.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                if (spinnerGen.getValue() <= 0) {
                    alert.accept("Špatně zvolený počet prvků");
                } else {
                    list.clear();
                    ovladac.generuj(spinnerGen.getValue());
                    updateList();
                }
            }
        });
        Button uloz = new Button("Ulož");
        uloz.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                ovladac.ulozTextSoubor(TEXTOVY_SOUBOR);
                info.accept("Uloženo");
            }
        });
        Button nacti = new Button("Načti");
        nacti.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                try {
                    list.clear();
                    ovladac.nactiTextSoubor(TEXTOVY_SOUBOR);
                    updateList();
                } catch (Exception e) {
                    alert.accept("Prázdný soubor");
                }
            }
        });
        Button zrus = new Button("Zruš");
        zrus.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                ovladac.zrus();
                list.clear();
                info.accept("Zrušeno");
            }
        });
        Button zalohuj = new Button("Zalohuj");
        zalohuj.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                ovladac.zalohuj(BINARNI_SOUBOR);
                info.accept("Zálohováno");
            }
        });
        Button obnov = new Button("Obnov");
        obnov.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                try {
                    list.clear();
                    ovladac.obnov(BINARNI_SOUBOR);
                    updateList();
                } catch (Exception e) {
                    alert.accept("Prázdný soubor");
                }
            }
        });
        Button najdi = new Button("Najdi");
        najdi.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                String klic = DialogDopravni.najdiDialog();
                if (klic.equals("Error")) {
                    alert.accept("Špatně zadaný klíč");
                } else if (klic.equals("Nehledat")) {

                } else {
                    String typParametru = klic.substring(0, 1);
                    DopravniProstredek nalezen = (DopravniProstredek) ovladac.najdi(klic.substring(1), typParametru);
                    if (nalezen == null) {
                        alert.accept("Prvek nenalezen");
                    } else {
                        DialogDopravni.nalezenoDialog(nalezen);
                    }

                }
            }
        });
        Button novy = new Button("Nový");
        novy.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                TypProstredku typ
                        = (TypProstredku) filtrBox.getSelectionModel().getSelectedItem();
                String[] novaData = DialogDopravni.vytvorDialog(true, null, typ);
                if (novaData[0] == null) {

                } else if (novaData[1].trim() == "" || novaData[0].trim() == ""
                        || novaData[4].trim() == "" || neniCislo(novaData[2])
                        || neniCislo(novaData[3]) || neniCislo(novaData[5])) {
                    alert.accept("Špatně zadané hodnoty");

                } else {
                    DopravniProstredek novyPrvek = null;
                    int cena = Integer.parseInt(novaData[2]);
                    int hmotnost = Integer.parseInt(novaData[3]);
                    String id = novaData[0];
                    String nazev = novaData[1];
                    String vyrobce = novaData[4];
                    int special = Integer.parseInt(novaData[5]);
                    System.out.println(special);
                    switch (typ) {
                        case DOPRAVNILETADLO:
                            novyPrvek = new DopravniLetadlo(special, cena, hmotnost,
                                    vyrobce, id, nazev);
                            break;
                        case STIHACILETOUN:
                            novyPrvek = new StihaciLetoun(special, cena, hmotnost,
                                    vyrobce, id, nazev);
                            break;
                        case VRTULNIK:
                            novyPrvek = new Vrtulnik(special, cena, hmotnost, vyrobce, id, nazev);
                            break;
                    }
                    ovladac.vlozPosledni(novyPrvek);
                    list.clear();
                    updateList();

                }
            }
        });

        Button filtruj = new Button("Filtruj");
        filtruj.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                TypProstredku typ = (TypProstredku) filtrBox.getSelectionModel().getSelectedItem();
                ObservableList<DopravniProstredek> listFiltr = FXCollections.observableArrayList();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getTyp() == typ) {
                        listFiltr.add(list.get(i));
                    }
                }
                listView.setItems(listFiltr);
            }
        });

        hbox.getChildren().addAll(generuj, spinnerGen, uloz, nacti,
                 new Label("Nový:"), novyBox, novy, new Label("Filtr:"),
                filtrBox, filtruj, najdi, zalohuj, obnov, zrus);
        root.setBottomAnchor(hbox, 10.0);
        root.setLeftAnchor(hbox, 10.0);
        root.getChildren().add(hbox);
    }

    private void setVBox() {
        VBox vbox = new VBox();
        vbox.setSpacing(15);
        Button edituj = new Button("Edituj");
        edituj.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                if (ovladac.aktualniPrvek() == null) {
                    alert.accept("Není nastaven aktuální prvek");
                } else {
                    editujPrvek();
                    list.clear();
                    updateList();
                }
            }
        });

        Button vyjmi = new Button("Vyjmi");
        vyjmi.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                if (ovladac.aktualniPrvek() == null) {
                    alert.accept("Není nastaven aktuální prvek");
                } else {

                    ovladac.vyjmi();
                    list.clear();
                    updateList();
                    info.accept("Vyjmuto");
                }
            }
        });
        Button prvni = new Button("První");
        prvni.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                if (ovladac.dejPocet() == 0) {
                    alert.accept("Prázdný seznam");
                } else {
                    ovladac.prvni();
                    listView.getSelectionModel().selectFirst();
                }

            }
        });
        Button dalsi = new Button("Další");
        dalsi.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                boolean posledniPrvek = false;
                try {
                    posledniPrvek = ovladac.jePosledni();
                } catch (KolekceException e) {
                }

                if (ovladac.aktualniPrvek() == null) {
                    alert.accept("Není nastaven aktuální prvek");
                } else if (posledniPrvek) {
                    alert.accept("Poslední prvek");
                } else {
                    ovladac.dalsi();
                    listView.getSelectionModel().selectNext();
                }

            }
        });
        Button zpet = new Button("Zpět");
        zpet.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                boolean prvniPrvek = false;
                try {
                    prvniPrvek = ovladac.jePrvni();
                } catch (KolekceException e) {
                }
                if (ovladac.aktualniPrvek() == null) {
                    alert.accept("Není nastaven aktuální prvek");
                } else if (prvniPrvek) {
                    alert.accept("První prvek");
                } else {
                    ovladac.zpet();
                    listView.getSelectionModel().selectPrevious();
                }
            }
        });
        Button posledni = new Button("Poslední");
        posledni.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                if (ovladac.dejPocet() == 0) {
                    alert.accept("Prázdný seznam");
                } else {
                    ovladac.posledni();
                    listView.getSelectionModel().selectLast();
                }
            }
        });

        Button zobraz = new Button("Zobraz");
        zobraz.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler() {
            @Override
            public void handle(Event t) {
                listView.setItems(list);

            }
        });

        vbox.getChildren().addAll(new Label("Procházení"), prvni, dalsi, zpet, posledni, new Label("Příkazy"), edituj, vyjmi, zobraz);
        root.setRightAnchor(vbox, 10.0);
        root.getChildren().add(vbox);
    }

    private void updateList() {
        Iterator<DopravniProstredek> iterator = ovladac.iterator();
        list.add(iterator.next());
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
    }

    private void editujPrvek() {
        DopravniProstredek prvek = (DopravniProstredek) ovladac.aktualniPrvek();
        TypProstredku typ = prvek.getTyp();
        String[] novaData = DialogDopravni.vytvorDialog(false, prvek, typ);
        if (novaData[0] == null) {

        } else {
            DopravniProstredek editPrvek = null;
            int cena = Integer.parseInt(novaData[2]);
            int hmotnost = Integer.parseInt(novaData[3]);
            String id = novaData[0];
            String nazev = novaData[1];
            String vyrobce = novaData[4];
            int special = Integer.parseInt(novaData[5]);
            System.out.println(special);
            switch (typ) {
                case DOPRAVNILETADLO:
                    editPrvek = new DopravniLetadlo(special, cena, hmotnost,
                            vyrobce, id, nazev);
                    break;
                case STIHACILETOUN:
                    editPrvek = new StihaciLetoun(special, cena, hmotnost,
                            vyrobce, id, nazev);
                    break;
                case VRTULNIK:
                    editPrvek = new Vrtulnik(special, cena, hmotnost, vyrobce, id, nazev);
                    break;
            }

            ovladac.uprav(editPrvek);
        }

    }

    private boolean neniCislo(String cislo) {
        try {
            int i = Integer.parseInt(cislo);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

}
