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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 *
 * @author HP
 */
public class DialogDopravni {
    public static String[] vytvorDialog(boolean novy, DopravniProstredek prvek,
            TypProstredku typ){
        Dialog<String[]> dialog = new Dialog();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        Label id = new Label(" ");
        TextField tfId = new TextField();
        TextField tfNazev = new TextField();
        TextField tfHmotnost = new TextField();
        TextField tfCena = new TextField();
        TextField tfVyrobce = new TextField();
        TextField tfSpecial = new TextField();
        Label special = new Label("");
        grid.add(new Label("ID"),0,0);
        if(novy!=true){
            grid.add(id,1,0);
        }else{
            grid.add(tfId,1,0);
        }
        grid.add(new Label("Název"),0,1);
        grid.add(tfNazev,1,1);
        grid.add(new Label("Cena"),0,2);
        grid.add(tfCena,1,2);
        grid.add(new Label("Hmotnost"),0,3);
        grid.add(tfHmotnost,1,3);
        grid.add(new Label("Výrobce"),0,4);
        grid.add(tfVyrobce,1,4);
        grid.add(special,0,5);
        grid.add(tfSpecial,1,5);
        switch(typ){
            case DOPRAVNILETADLO:
                special.setText("Počet Sedadel");
                break;
            case STIHACILETOUN:
                special.setText("Počet Zbraní");
                break;
            case VRTULNIK:
                special.setText("Počet Vrtulí");
                break;
        }
        if(novy==false){
            nastavTextField(typ,id, prvek, tfNazev, tfCena, tfHmotnost, tfVyrobce,tfSpecial);
        }
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,
                ButtonType.CANCEL);
        dialog.setResultConverter(new Callback<ButtonType, String[]>(){
            @Override
            public String[] call(ButtonType dialogButton){
                String[] novaData = null;
                if(dialogButton == ButtonType.OK){    
                    if(novy){
                     novaData = new String[]{
                    tfId.getText(),tfNazev.getText(),tfCena.getText(),
                        tfHmotnost.getText(), tfVyrobce.getText(),
                        tfSpecial.getText()};
                    }else{
                     novaData = new String[]{
                    id.getText(),tfNazev.getText(),tfCena.getText(),
                        tfHmotnost.getText(), tfVyrobce.getText(),
                        tfSpecial.getText()};   
                        }
                    return novaData;
                }else{
                    String[] escapePole =new String[6]; 
                    return escapePole;
                    
                }
            }
        });
        dialog.getDialogPane().setContent(grid);
        String[] vysledky = dialog.showAndWait().get();
        return vysledky;
        
    }

    private static void nastavTextField(TypProstredku typ,Label id, DopravniProstredek prvek, 
            TextField tfNazev, TextField tfCena, 
            TextField tfHmotnost, TextField tfVyrobce, TextField tfSpecial) {
        id.setText(prvek.getId());
        tfNazev.setText(prvek.getNazev());
        tfCena.setText(Integer.toString(prvek.getCena()));
        tfHmotnost.setText(Integer.toString(prvek.getHmotnost()));
        tfVyrobce.setText(prvek.getVyrobce());
        switch(typ){
            case DOPRAVNILETADLO:
                int pocetSedadel = ((DopravniLetadlo)prvek).getPočetSedadel(); 
                tfSpecial.setText(Integer.toString(pocetSedadel));
                break;
            case STIHACILETOUN:
                int pocetZbrani = ((StihaciLetoun)prvek).getPocetZbrani(); 
                tfSpecial.setText(Integer.toString(pocetZbrani));
                break;
            case VRTULNIK:
                int pocetVrtuli = ((Vrtulnik)prvek).getPočetVrtuli(); 
                tfSpecial.setText(Integer.toString(pocetVrtuli));
                break;
        }
    }
    
    public static String najdiDialog(){
        Dialog<String> dialog = new Dialog();
        GridPane grid = new GridPane();
        TextField tfId = new TextField();
        TextField tfNazev = new TextField();
        grid.add(new Label("ID"), 0, 0);
        grid.add(tfId,1,0);
        grid.add(new Label("Název"),0,1);
        grid.add(tfNazev,1,1);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,
                ButtonType.CANCEL);
        dialog.setResultConverter(new Callback<ButtonType, String>(){
            @Override
            public String call(ButtonType dialogButton){
                if(dialogButton == ButtonType.OK){ 
                String klic = null;
                if(tfId.getText().trim()!="" && tfNazev.getText().trim()!=""){
                    klic="Error";
                }
                else if(tfId.getText().trim()=="" && tfNazev.getText().trim()==""){
                    klic="Error";
                }
                else if(tfId.getText()!=null){
                    klic="i"+tfId.getText();
                }else{
                    klic = "n"+tfNazev.getText();
                }
                return klic;
                }
                else {
                    return "Nehledat";
                }
            }
        });
        return dialog.showAndWait().get();
    }
    public static void nalezenoDialog(DopravniProstredek prvek){
        Dialog<String[]> dialog = new Dialog();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        Label id = new Label(" ");
        Label nazevLabel = new Label();
        Label hmotnostLabel = new Label();
        Label cenaLabel = new Label();
        Label vyrobceLabel = new Label();
        Label specialLabel = new Label();
        Label special = new Label("");
        grid.add(new Label("ID"),0,0);
        grid.add(id,1,0);
        grid.add(new Label("Název"),0,1);
        grid.add(nazevLabel,1,1);
        grid.add(new Label("Cena"),0,2);
        grid.add(cenaLabel,1,2);
        grid.add(new Label("Hmotnost"),0,3);
        grid.add(hmotnostLabel,1,3);
        grid.add(new Label("Výrobce"),0,4);
        grid.add(vyrobceLabel,1,4);
        grid.add(special,0,5);
        grid.add(specialLabel,1,5);
        switch(prvek.getTyp()){
            case DOPRAVNILETADLO:
                special.setText("Počet Sedadel");
                DopravniLetadlo prvekLet = (DopravniLetadlo) prvek;
                specialLabel.setText(Integer.toString(prvekLet.getPočetSedadel()));
                break;
            case STIHACILETOUN:
                special.setText("Počet Zbraní");
                StihaciLetoun prvekStih= (StihaciLetoun) prvek;
                specialLabel.setText(Integer.toString(prvekStih.getPocetZbrani()));
                break;
            case VRTULNIK:
                special.setText("Počet Vrtulí");
                Vrtulnik prvekVrt= (Vrtulnik) prvek;
                specialLabel.setText(Integer.toString(prvekVrt.getPočetVrtuli()));
                break;
        }
        id.setText(prvek.getId());
        nazevLabel.setText(prvek.getNazev());
        cenaLabel.setText(Integer.toString(prvek.getCena()));
        hmotnostLabel.setText(Integer.toString(prvek.getHmotnost()));
        vyrobceLabel.setText(prvek.getVyrobce());
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.showAndWait();
    }
    
    
    
    
    
}
