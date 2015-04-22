
class Cook extends Thread {

  private volatile boolean orderReady;
  private Thread t;
  private volatile boolean cancel;
  private String threadName;
  private int id;
  private MachineMonitor mm;
  private int timeGranularity;
  private int[] order;
  private String dName;

  public Cook(String threadName, int id, int timeGranularity, MachineMonitor mm){
    this.threadName = threadName;
    this.id = id;
    this.mm = mm;
    this.timeGranularity = timeGranularity;
    this.cancel = false;
    this.orderReady = false;
    this.order = new int[]{0,0,0};
  }

  public void run() {
    while(!cancel){
      while(!orderReady);
      if(dName!=null)
        System.out.println(threadName+" processing "+dName+"'s order");
      int resourceSum = order[0]+order[1]+order[2];
      int perm = calculateNeed();
      while(resourceSum>0){
        String machine = mm.getMachine(perm);
        if(machine.equals("burger")){
          System.out.println(threadName+" making Burger");
          try{
            Thread.sleep(5*timeGranularity);
          }catch(InterruptedException e){
            e.printStackTrace();
          }
          order[0]--;
        }else if (machine.equals("fries")) {
          System.out.println(threadName+" making Fries");
          try{
            Thread.sleep(3*timeGranularity);
          }catch(InterruptedException e){
            e.printStackTrace();
          }
          order[1]--;
        }else if (machine.equals("coke")){
          System.out.println(threadName+" making Coke");
          try{
            Thread.sleep(1*timeGranularity);
          }catch(InterruptedException e){
            e.printStackTrace();
          }
          order[2]--;
        }
        mm.putMachine(machine);
        resourceSum = order[0]+order[1]+order[2];
        perm = calculateNeed();
      }
      if(dName!=null)
        System.out.println(threadName+": Order complete!");
      orderReady = false;
    }
    System.out.println(threadName+"'s shift ends.");
    return;
  }

  //idea similar to unix rwx permissions
  int calculateNeed(){
    int perm = 0;
    if (order[0]>0)
      perm += 4;
    if(order[1]>0)
      perm += 2;
    if(order[2]>0)
      perm += 1;
    return perm;
  }

  void serveFood(){
    while(orderReady);
    return;
  }

  void executeOrder(int burgers, int fries, int coke, String dinerName){
    order[0] = burgers;
    order[1] = fries;
    order[2] = coke;
    dName = dinerName;
    orderReady = true;
  }

  void endShift(){
    order[0] = 0;
    order[1] = 0;
    order[2] = 0;
    dName=null;
    cancel = true;
    orderReady = true;
  }

  public void start(){
    System.out.println("Starting "+threadName);
    if(t==null){
      t = new Thread(this, threadName);
      t.start();
    }
  }

  public String getThreadName(){
    return threadName;
  }

}
