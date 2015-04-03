import java.util.TimerTask;

class Diner extends TimerTask {

  private Thread t;
  private String threadName;
  private int timeGranularity;
  private int timeArrived;
  private int burgers;
  private int fries;
  private int coke;
  private TableMonitor tm;
  private CookMonitor cm;

  Diner(String threadName, int timeGranularity, int[] attr, TableMonitor tm, CookMonitor cm) {
    this.threadName = threadName;
    this.timeGranularity = timeGranularity;
    this.timeArrived = attr[0];
    this.burgers = attr[1];
    this.fries = attr[2];
    this.coke = attr[3];
    this.tm = tm;
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
    Cook cook = cm.getCook();
    System.out.println(threadName+" placed order");
    cook.executeOrder(burgers, fries, coke);
    cm.putCook(cook);
    sleep(30*this.timeGranularity*1000);
    tm.putTable(table);
    long endTime = System.currentTimeMillis();
    System.out.println(threadName+" left at "+((timeArrived*timeGranularity)+((endTime-startTime)/(1000*timeGranularity))));
  }

  public void start(){
    System.out.println("Starting "+threadName);
    if(t==null){
      t = new Thread(this, threadName);
      t.start();
    }
  }

}
