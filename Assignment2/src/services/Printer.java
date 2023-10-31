package services;

import java.util.ArrayList;

public class Printer {
    String printerName;
    ArrayList<String> queue = new ArrayList<>();
    String status;

    public Printer(String name){
        printerName = name;
        ArrayList<String> queue = new ArrayList<>();
        status = "ON";
    }

    public String getstatus(){
        return status;
    }

    public ArrayList<String> getQueue(){
        return queue;
    }

    public void addQueue(String job){
        queue.add(job);
    }

    public String getPrinterName(){
        return printerName;
    }

    public void setPrinterName(String name){
        printerName = name;
    }

    public void moveTopQueue(int job) {
        String temp = queue.get(job);
        queue.remove(job);
        queue.add(0, temp);
    }

    public void clearQueue(){
        queue.clear();
    }


}
