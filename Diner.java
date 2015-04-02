import java.util.TimerTask;

class Diner extends TimerTask {

  private Thread t;
  private String threadName;
  private int timeGranularity;

  private int burgers;
  private int fries;
  private int coke;
  // private TableMonitor tm;

  Diner(String threadName, int timeGranularity) {
    this.threadName = threadName;
    this.timeGranularity = timeGranularity;
  }

  public void setAttributes(int[] attr, int size){
    this.burgers = attr[1];
    this.fries = attr[2];
    this.coke = attr[3];
  }

  // public void setTableMonitor(TableMonitor tm) {
  //   this.tm = tm;
  // }

  public void run(){
    System.out.println("inside thread:"+threadName);
    int count =10;
    while(count>0){
      System.out.println(threadName+": "+count--);
    }
    // int table = tm.getTable();
    // Cook cook = cm.getCook();
    // cook.takeOrder();
    // cm.putCook(cook);
    // sleep(30*this.timeGranularity*1000);
    // tm.putTable(table);
  }

  public void start(){
    System.out.println("Starting "+threadName);
    if(t==null){
      t = new Thread(this, threadName);
      t.start();
    }
  }

}
