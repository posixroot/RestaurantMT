
class Diner extends Thread {

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
    System.out.println(threadName+" arrived at "+timeArrived);
    long startTime = System.currentTimeMillis();
    int table = tm.getTable();
    System.out.println(threadName+" is seated at table-"+table);
    Cook cook = cm.getCook();
    System.out.println(threadName+" is assigned "+cook.getThreadName());
    System.out.println(threadName+" asked for "+burgers+" Burger(s), "+fries+" Fries and "+coke+" coke");
    cook.executeOrder(burgers, fries, coke, threadName);
    cook.serveFood();
    cm.putCook(cook);
    System.out.println(threadName+"'s food is served!");
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

  public Thread getThread(){
    return t;
  }

}
