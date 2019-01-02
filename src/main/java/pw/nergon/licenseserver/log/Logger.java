package pw.nergon.licenseserver.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private File logFile;

    public Logger() {
        DateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        File logFolder = new File("logs");
        if (!logFolder.exists()) {
            logFolder.mkdirs();
        }
        logFile = new File("logs/log-"+fileDateFormat.format(date)+".txt");
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        log("Restarted Server on "+format.format(date));
    }

    public void log(String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true))) {
            System.out.println("[*]"+message+ConsoleColors.RESET);
            bw.write("[*]"+message+"\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logSuccess(String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true))) {
            System.out.println(ConsoleColors.GREEN+"[+]"+message+ConsoleColors.RESET);
            bw.write("[+]"+message+"\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logError(String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true))) {
            System.out.println(ConsoleColors.RED+"[+]"+message+ConsoleColors.RESET);
            bw.write("[*]"+message+"\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
