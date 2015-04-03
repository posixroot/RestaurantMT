import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class RestaurantMT {

  public static void main(String[] args){

    if(args.length!=1){
      System.out.println("Invalid Invocation. Usage: java <input-file>");
      System.exit(0);
    }

    String filename = (String)args[0];
    String line = null;
    int diners = -1;
    int cooks = -1;
    int tables = -1;
    int timeGranularity = 1000;//To make program faster, if needed.
                               //Default of 1000 ensures
                               // 1min real-time = 1sec program-time
    ArrayList<Object> arrList = new ArrayList<Object>();

    //Read the input file and populate the ArrayList
    try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
      while((line = br.readLine())!=null) {
        arrList.add(line);
      }
    } catch (IOException e){
      System.out.println(filename+" File doesn't exist!");
      e.printStackTrace();
      System.exit(0);
    }

    //Make sure we have at least 3 lines, namely, #diners, #tables, #cooks
    if(arrList.size()<3) {
      System.out.println("Invalid Input. Input: line1:<#diners>"
                          +"line2:<#tables> line3:<#cooks>"
                          +"line4...<#diners><in-time, #burgers, #fries, #coke>");
      System.exit(0);
    }

    //Convert the Strings to numbers
    try{
      diners = Integer.parseInt(arrList.get(0).toString());
    }catch(Exception e){
      System.out.println("Enter only Numbers");
      e.printStackTrace();
      System.exit(0);
    }

    //Preprocessing: Make sure we have sufficient attribute lines for all diners
    if(arrList.size()!=diners+3) {
      System.out.println("Invalid Input. Input: line1:<#diners>"
                          +"line2:<#tables> line3:<#cooks>"
                          +"line4...<#diners><in-time, #burgers, #fries, #coke>");
      System.exit(0);
    }

    //Update the tables and cooks variables
    for(int i=0; i<3; i++){
      arrList.set(i, Integer.parseInt(arrList.get(i).toString()));
    }
    tables = (int)arrList.get(1);
    cooks = (int)arrList.get(2);

    //Preprocessing: Convert the Diner's attributes to Integers
    //and store them as vectors
    for(int i=3; i<arrList.size();i++){
      String[] strArr = arrList.get(i).toString().split(" ");
      int[] dinerVector = new int[4];

      try{
        for(int j=0; j<4; j++){
          dinerVector[j] = Integer.parseInt(strArr[j]);
        }
      }catch (Exception e){
        System.out.println("Enter only Numbers");
        e.printStackTrace();
        System.exit(0);
      }
      if(dinerVector.length!=4){
        System.out.println("The diner vector needs to be of length 4");
        System.exit(0);
      }
      if(dinerVector[1]<1 || dinerVector[2]<0 || dinerVector[3]<0 || dinerVector[3]>1) {
        System.out.println("Please check the order bounds."
                            +"Burgers:1 or higher,"
                            +"Fries: 0 or higher,"
                            +"Coke: 0 or 1.");
        System.exit(0);
      }
      arrList.set(i, dinerVector);
    }

    System.out.println("Restaurant 6431 is now open!");

    //Initialize the Monitors
    //Responsible for assigning tables to diners
    TableMonitor tm = new TableMonitor(tables);
    //Responsible for assigning machines to cooks
    MachineMonitor mm = new MachineMonitor();

    //spawn cook threads
    Cook[] cookThread = new Cook[cooks];
    for(int i=0;i<cooks;i++){
      cookThread[i] = new Cook("Cook-"+(i+1), i, timeGranularity, mm);
      cookThread[i].start();
    }
    //Responsible for assigning cooks to diners
    CookMonitor cm = new CookMonitor(cookThread, cooks);

    //spawn diner threads
    Diner[] dinerThread = new Diner[diners];
    LinkedHashMap<Integer, ArrayList<Diner>> lhm
                              = new LinkedHashMap<Integer, ArrayList<Diner>>();
    for(int i=0;i<diners;i++) {
      int[] attrs = (int[])arrList.get(i+3);
      dinerThread[i]
                  = new Diner("Diner-"+(i+1), timeGranularity, attrs, tm, cm);
      if(!lhm.containsKey(attrs[0]))
        lhm.put(attrs[0], new ArrayList<Diner>());
      lhm.get(attrs[0]).add(dinerThread[i]);
    }

    for(int i=0; i<120; i++){
      if(lhm.containsKey(i)){
        for(Diner dt : lhm.get(i))
          dt.start();
      }
      try{
        Thread.sleep(1*timeGranularity);
      }catch(InterruptedException e){
        e.printStackTrace();
      }
    }
    System.out.println("Restaurant Closed!");

    //Wait for unfinished diners
    for(int i=0;i<diners;i++){
      if(dinerThread[i].getThread().isAlive()){
        try{
          dinerThread[i].getThread().join();
        }catch(InterruptedException e){
          e.printStackTrace();
        }
      }
    }

    //End Cook threads
    for(int i=0;i<cooks;i++){
      cookThread[i].endShift();
    }
  }

}
