package it.fi.itismeucci.berisha.client;

public class ClientMain 
{
    public static void main( String[] args )
    {
        Client client = new Client("localhost", 5000);
        client.connetti();
        client.comunica();
    }
}
