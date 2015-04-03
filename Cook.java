

class Cook extends Thread {

  // volatile boolean cancel;
  // volatile boolean orderReady;
  private boolean cancel;
  private boolean orderReady;
  private String threadName;
  private int id;
  private MachineMonitor mm;
  private int timeGranularity;
  private int[] order;

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
      orderReady = false;
      System.out.println(threadName+" processing order");
      int resourceSum = order[0]+order[1]+order[2];
      int perm = calculateNeed();
      while(resourceSum){
        String machine = mm.getMachine(perm);
        if(machine.equals("burger")){
          System.out.println(threadName+" making Burger");
          sleep(5*timeGranularity*1000);
          order[0]--;
        }else if (machine.equals("fries")) {
          System.out.println(threadName+" making Fries");
          sleep(3*timeGranularity*1000);
          order[1]--;
        }else if (machine.equals("coke")){
          System.out.println(threadName+" making Coke");
          sleep(1*timeGranularity*1000);
          order[2]--;
        }
        mm.putMachine(machine);
        resourceSum = order[0]+order[1]+order[2];
        perm = calculateNeed();
      }

    }
    return;
  }

  int calculateNeed(){
    int perm = 0;
    if (order[0])
      perm += 4;
    if(order[1])
      perm += 2;
    if(order[2])
      perm += 1;
    return perm;
  }

  void executeOrder(int burgers, int fries, int coke){
    order[0] = burgers;
    order[1] = fries;
    order[2] = coke;
    orderReady = true;
  }

  void endShift(){
    order[0] = 0;
    order[1] = 0;
    order[2] = 0;
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

}
