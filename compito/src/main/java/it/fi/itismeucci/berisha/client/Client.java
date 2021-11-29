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

    /**
     * Metodo per connettersi al server
     */
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

    /**
     * Metodo per iniziare la comunicazione col server per poter salvare i messaggi inviati nella lista delle note
     */
    public void comunica(){

        Scanner tastiera = new Scanner(System.in); //si apre l'input da tastiera
        String messaggio = "";
        
        while(true){

            System.out.println(riceviMessaggio());

            messaggio = tastiera.nextLine(); //si salva in messaggio l'input preso da tastiera
            inviaMessaggio(messaggio);
            messaggio = riceviMessaggio();

            //si controlla la risposta inviata dal server per decidere cosa stampare come risposta
            if(messaggio.startsWith("/")){ //il server ha inviato la lista delle note, la stringa è da scomporre
                System.out.println(scomponiLista(messaggio));

            }else if(messaggio.equals(".")){ //il server comunica che si sta utilizzando un carattere non valido
                System.out.println("Non è possibile utilizzare punti");
            
            }else if(messaggio.equals("CHIUDI")){ //il server comunica che è pronto alla chiusura (inizilmente richiesta dall'utente che scrive CHIUDI)
                System.out.println("Chiusura in corso...");
                break;

            }else{ //non è successo niente quindi si stampa direttamente il messaggio del server (Che sarà "Nota salvata")
                System.out.println(messaggio);
            }

        }
        tastiera.close();
        chiudi();
    }

    /**
     * Metodo che chiude tutti i canali di comunicazione
     */
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

    /**
     * Metodo che scompone la lista ricevuta, la lista avrà un formato come per esempio: /comprare latte.pagare bolletta luce.revisione macchina...
     * @param messaggio la lista da scomporre
     * @return la stringa da stampare che sarà la lista scomposta in un formato più facilmente leggibile
     */
    public String scomponiLista(String messaggio){
        String str = "Lista:";
        String[] lista = messaggio.substring(1).split("\\.");

        for(int i = 0; i < lista.length; i++){
            str += "\n - " + lista[i];
        }

        return str;
    }

    /**
     * Metodo che invia il messaggio passato per parametro al server
     * @param messaggio messaggio da inviare al server
     */
    public void inviaMessaggio(String messaggio){
        try {
            invia.writeBytes(messaggio + '\n');
        } catch (IOException e) {
            System.out.println("Errore durante l'invio del messaggio: \"" + messaggio + "\"");
            e.printStackTrace();
        }
    }

    /**
     * Metodo che riceve il messaggio inviato dal server
     * @return messaggio ricevuto dal server
     */
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
