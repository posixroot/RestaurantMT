import java.util.TimerTask;

class Diner extends TimerTask {

  private Thread t;
  private String threadName;
  private int timeGranularity;
  int timeArrived;
  int burgers;
  int fries;
  int coke;
  TableMonitor tm;
  CookMonitor cm;

  Diner(String threadName, int timeGranularity, int[] attr, TableMonitor tm, CookMonitor cm) {
    this.threadName = threadName;
    this.timeGranularity = timeGranularity;
    this.timeArrived = attr[0];
    this.burgers = attr[1];
    this.fries = attr[2];
    this.coke = attr[3];
    // this.tm = new TableMonitor();
    this.tm = tm;
    // this.cm = new CookMonitor();
    this.cm = cm;
  }

  public void run(){
    // System.out.println("inside thread:"+threadName);
    // int count =10;
    // while(count>0){
    //   System.out.println(threadName+": "+count--);
    // }
    System.out.println(threadName+" arrived");
    long startTime = System.currentTimeMillis();
    int table = tm.getTable();
    System.out.println(threadName+" assigned table "+table);
    // Cook cook = new Cook();
    Cook cook = cm.getCook();
    System.out.println(threadName+" is assigned "+cook.threadName);

    System.out.println(threadName+" placed order");
    cook.executeOrder(burgers, fries, coke);
    System.out.println(threadName+" asked for "+burgers+" "+fries+" and cook got "+cook.order[0]+" "+cook.order[1]);
    for(int i=0;i<2;i++){
      System.out.println(threadName+" says: "+tm.tablesAvailable);
    }
    cm.putCook(cook);
    try{
      Thread.sleep(30*this.timeGranularity);
    }catch(InterruptedException e){
      e.printStackTrace();
    }
    tm.putTable(table);
    long endTime = System.currentTimeMillis();
    System.out.println(threadName+" left at "+((timeArrived)+((endTime-startTime)/timeGranularity)));
  }

  public void start(){
    System.out.println("Starting "+threadName);
    if(t==null){
      t = new Thread(this, threadName);
      t.start();
    }
  }

}
