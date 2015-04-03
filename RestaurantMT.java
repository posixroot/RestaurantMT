import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

public class RestaurantMT {

  public static void main(String[] args){

    String filename = "simulatorConfig.txt";

    if(args.length!=0){
      System.out.println("No arguments needed."+filename+" file used!");
      System.exit(0);
    }

    String line = null;
    int diners = -1;
    int cooks = -1;
    int tables = -1;
    int timeGranularity = 1;
    ArrayList<Object> arrList = new ArrayList<Object>();

    try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
      while((line = br.readLine())!=null) {
        arrList.add(line);
      }
    } catch (IOException e){
      System.out.println(filename+" File doesn't exist!");
      e.printStackTrace();
      System.exit(0);
    }
    if(arrList.size()<1) {
      System.out.println("Invalid Invocation. Usage: java <#diners> <#tables> <#cooks> <#diners*<in-time, #burgers, #fries, #coke>>");
      System.exit(0);
    }
    try{
      diners = Integer.parseInt(arrList.get(0).toString());
    }catch(Exception e){
      System.out.println("Enter only Numbers");
      e.printStackTrace();
      System.exit(0);
    }
    // System.out.println("arrList[0]: "+diners);
    if(arrList.size()!=diners+3) {
      System.out.println("Invalid Invocation. Usage: java <#diners> <#tables> <#cooks> <#diners*<in-time, #burgers, #fries, #coke>>");
      System.exit(0);
    }

    for(int i=0; i<3; i++){
      arrList.set(i, Integer.parseInt(arrList.get(i).toString()));
    }
    tables = (int)arrList.get(1);
    cooks = (int)arrList.get(2);
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
      // if(dinerVector[0]>120||dinerVector[0]<0){
      //   System.out.println("The diners need to arrive in the interval between 0 and 120");
      //   System.exit(0);
      // }
      if(dinerVector[1]<1 || dinerVector[2]<0 || dinerVector[3]<0 || dinerVector[3]>1) {
        System.out.println("Please check the order bounds. Burgers:1 or higher, Fries: 0 or higher, Coke: 0 or 1.");
        System.exit(0);
      }
      arrList.set(i, dinerVector);
    }

    System.out.println("Restaurant 6431 is now open!");

    //Initialize the Monitors
    TableMonitor tm = new TableMonitor(tables);
    MachineMonitor mm = new MachineMonitor();


    //spawn cooks
    Cook[] cookThread = new Cook[cooks];
    for(int i=0;i<cooks;i++){
      cookThread[i] = new Cook("Cook-"+(i+1), i, timeGranularity, mm);
      cookThread[i].start();
    }
    CookMonitor cm = new CookMonitor(cookThread, cooks);

    //spawn diners
    Timer timer = new Timer();
    Diner[] dinerThread = new Diner[diners];
    for(int i=0;i<diners;i++) {
      int[] attrs = (int[])arrList.get(i+3);
      dinerThread[i] = new Diner("Diner-"+(i+1), timeGranularity, attrs, tm, cm);
      if(attrs[0]<=120)
        timer.schedule(dinerThread[i], attrs[0]*1000);
    }

    //sleep to make sure all the Diners finish
    sleep(200*timeGranularity*1000);

    //End Cook threads
    for(int i=0;i<cooks;i++){
      cookThread[i].endShift();
    }
    //End Diner threads
    timer.cancel();
  }

}
