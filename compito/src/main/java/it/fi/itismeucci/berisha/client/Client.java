package it.fi.itismeucci.berisha.client;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
    
    private int port;
    private String address;

    private Socket server;

    private BufferedReader ricevi;
    private DataOutputStream invia;

    public Client(String address, int port){
        this.address = address;
        this.port = port;
    }

    public void connetti(){
        try {
            server = new Socket(address, port);

            ricevi = new BufferedReader(new InputStreamReader(server.getInputStream()));
            invia = new DataOutputStream(server.getOutputStream());
            System.out.println("\nConnessione effettuata");

        } catch (UnknownHostException e) {
            System.out.println("Errore: Host sconosciuto");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Errore nella connessione");
            e.printStackTrace();
        }
    }

    public void comunica(){

        Scanner tastiera = new Scanner(System.in);
        String messaggio = "";
        
        while(true){

            System.out.println(riceviMessaggio());

            messaggio = tastiera.nextLine();
            inviaMessaggio(messaggio);
            messaggio = riceviMessaggio();

            if(messaggio.startsWith("/")){
                System.out.println(scomponiLista(messaggio));

            }else if(messaggio.equals(".")){
                System.out.println("Non è possibile utilizzare punti");
            
            }else if(messaggio.equals("CHIUDI")){
                System.out.println("Chiusura in corso...");
                break;

            }else{
                System.out.println(messaggio);
            }

        }
        tastiera.close();
        chiudi();
    }

    public void chiudi(){
        try {
            invia.close();
            ricevi.close();
            server.close();
        } catch (IOException e) {
            System.out.println("Errore durante la chiusura");
            e.printStackTrace();
        }
    }

    public String scomponiLista(String messaggio){
        String str = "Lista:";
        String[] lista = messaggio.substring(1).split("\\.");

        for(int i = 0; i < lista.length; i++){
            str += "\n - " + lista[i];
        }

        return str;
    }

    public void inviaMessaggio(String messaggio){
        try {
            invia.writeBytes(messaggio + '\n');
        } catch (IOException e) {
            System.out.println("Errore durante l'invio del messaggio: \"" + messaggio + "\"");
            e.printStackTrace();
        }
    }

    public String riceviMessaggio(){
        try {
            return ricevi.readLine();
        } catch (IOException e) {
            System.out.println("Errore durante la ricezione del messaggio");
            e.printStackTrace();
        }
        return null;
    }

}
