package pw.nergon.licenseserver.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pw.nergon.licenseserver.LicenseServer;
import pw.nergon.licenseserver.log.Logger;

import javax.net.ssl.SSLSocket;
import java.io.*;

public class ClientHandler extends Thread {

    private SSLSocket socket;
    private BufferedWriter dataOutputStream;
    private BufferedReader dataInputStream;


    public ClientHandler(SSLSocket socket, BufferedReader dataInputStream, BufferedWriter dataOutputStream) {
        this.socket = socket;
        socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void run() {
        try {
            dataOutputStream.write("{\"connected\": true}");
            dataOutputStream.newLine();
            dataOutputStream.flush();
            while (true) {
                String read = dataInputStream.readLine();
                if (read == null)
                    continue;

                Gson gson = new GsonBuilder().create();
                Protocol protocol = gson.fromJson(read, Protocol.class);
                if (protocol.getKey() != null) {
                    String key = protocol.getKey();
                    if (LicenseServer.instance.getHandler().checkLicense(key, socket.getInetAddress().toString(), protocol.getMac())) {
                        dataOutputStream.write("{\"success\": true}");
                    } else {
                        dataOutputStream.write("{\"success\": false}");
                    }
                    dataOutputStream.newLine();
                    dataOutputStream.flush();
                }
                if (protocol.isCloseAfter()) {
                    LicenseServer.instance.getLogger().log("Client closed connection");
                    break;
                }
            }
            LicenseServer.instance.getLogger().log("Stopping Client Thread: "+this.getName());
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
