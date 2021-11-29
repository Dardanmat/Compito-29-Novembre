package it.fi.itismeucci.berisha.server;

import java.io.*;
import java.net.*;

public class Server {
    
    private int port;

    private ServerSocket server;
    private Socket client;

    private BufferedReader ricevi;
    private DataOutputStream invia;

    private String note;

    public Server(int port){
        this.port = port;
        note = "";
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Errore nella creazione del connection socket");
            e.printStackTrace();
        }
    }

    /**
     * Metodo per aspettare l'arrivo di un client
     */
    public void connetti(){
        try {
            System.out.println("\nServer in attesa dell'arrivo di un client...");
            client = server.accept();
            System.out.println("Client arrivato");

            ricevi = new BufferedReader(new InputStreamReader(client.getInputStream()));
            invia = new DataOutputStream(client.getOutputStream());

            server.close();
        } catch (IOException e) {
            System.out.println("Errore durante la connessione");
            e.printStackTrace();
        }

    }

    /**
     * Metodo per comunicare col client
     */
    public void comunica(){
        System.out.println("------Inizio comunicazione------I");
        String messaggio = "";

        while(true){
            System.out.println("Invio: messaggio di richiesta");
            inviaMessaggio("Inserisci la nota da memorizzare o digita LISTA per visualizzare le note salvate");

            System.out.println("In attesa di una risposta");
            messaggio = riceviMessaggio();
            System.out.println("Ricevuto: " + messaggio);

            //si controlla il tipo di richiesta del client e la validità del messaggio
            if(messaggio.equals("LISTA")){
                inviaLista();

            }else if(messaggio.contains(".")){
                inviaMessaggio(".");
            
            }else if(messaggio.equals("CHIUDI")){
                System.out.println("Arrivato il messaggio di chiusura, chiusura in corso...");
                inviaMessaggio("CHIUDI");
                break;

            }else{
                salvaNota(messaggio);
                inviaMessaggio("Nota Salvata");
            }
        }
        chiudi();
    }

    /**
     * Metodo che chiude tutti i canali di comunicazione
     */
    public void chiudi(){  
        try {
            invia.close();
            ricevi.close();
            client.close();
        } catch (IOException e) {
            System.out.println("Errore durante la chiusura");
            e.printStackTrace();
        }
    }

    /**
     * Metodo per salvare le note, le note vengono salvate in un'unica stringa e vengono separate da punti, sarà il client a separare le note quando gli verrà inviato il messaggio
     * @param nota  nota da salvare nella stringa che contiene tutte le note
     */
    public void salvaNota(String nota){
        note += nota + ".";
    }

    /**
     * Metodo che invia la lista delle note al client, invierà una lista di note separate da un punto, all'inizio della lista ci sarà uno slash per indicare che questa è una stringa da scomporre
    */
    public void inviaLista(){
        inviaMessaggio("/" + note);
    }

    /**
     * Invia il messaggio passato per parametro al client
     * @param messaggio messaggio da inviare al client
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
     * Rieceve il messaggio inviato dal client
     * @return il messaggio inviato da client
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
