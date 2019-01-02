package pw.nergon.licenseserver.network;

import pw.nergon.licenseserver.LicenseServer;

import javax.net.ssl.*;
import java.io.*;
import java.nio.Buffer;
import java.security.KeyStore;

public class ConnectionHandler {

    private SSLServerSocket serverSocket;

    public ConnectionHandler() {
        startServerSSL();
    }

    private void startServerSSL() {
        LicenseServer.instance.getLogger().log("Starting ServerSocket");
        loadSSLConfig();
        try {
            serverSocket = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(5678);
            serverSocket.setEnabledCipherSuites(serverSocket.getSupportedCipherSuites());
            LicenseServer.instance.getLogger().logSuccess("Started ServerSocket on Port: "+5678);
        } catch (Exception e) {
            LicenseServer.instance.getLogger().logError("Couldn't start ServerSocket");
            e.printStackTrace();
        }
        LicenseServer.instance.getLogger().log("Waiting for Client...");
        startListening();
    }

    private void startListening() {
        while (true) {
            try {
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                LicenseServer.instance.getLogger().log("New Client has connected. IP: "+socket.getInetAddress().toString());
                BufferedReader dataInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter dataOutputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                LicenseServer.instance.getLogger().log("Creating new thread for Client.");
                Thread thread = new ClientHandler(socket, dataInputStream, dataOutputStream);
                thread.start();
            } catch (IOException e) {
                LicenseServer.instance.getLogger().logError("Server crashed");
                e.printStackTrace();
                break;
            }
        }
    }

    private void loadSSLConfig() {
        try {
            System.setProperty("javax.net.ssl.keyStore", LicenseServer.instance.getConfigInfo().getPathToKeyStore());
            System.setProperty("javax.net.ssl.keyStorePassword", LicenseServer.instance.getConfigInfo().getKeyStorePass());
            System.setProperty("javax.net.ssl.trustStorePassword", LicenseServer.instance.getConfigInfo().getTrustStorePass());
            System.setProperty("javax.net.ssl.trustStore", LicenseServer.instance.getConfigInfo().getPathToTrustStore());
        } catch (Exception e){
            LicenseServer.instance.getLogger().logError("Couldn't load SSL Configuration");
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
