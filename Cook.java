

class Cook extends Thread {

  volatile bool cancel;
  volatile bool orderReady;
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
    cancel = False;
    orderReady = False;
    order = new int[]{0,0,0};
  }

  public void run() {
    while(!cancel){
      while(!orderReady);
      orderReady = False;
      int resourceSum = order[0]+order[1]+order[2];
      int perm = calculateNeed();
      while(resourceSum){
        String machine = mm.getMachine(perm);
        if(machine.equals("burger")){
          sleep(5*timeGranularity*1000);
          order[0]--;
        }else if (machine.equals("fries")) {
          sleep(2*timeGranularity*1000);
          order[1]--;
        }else if (machine.equals("coke")){
          sleep(1*timeGranularity*1000);
          order[2]--;
        }
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

  void takeOrder(int burgers, int fries, int coke){
    order[0] = burgers;
    order[1] = fries;
    order[2] = coke;
    orderReady = True;
  }

  void endShift(){
    order[0] = 0;
    order[1] = 0;
    order[2] = 0;
    cancel = True;
    orderReady = True;
  }

  public void start(){
    System.out.println("Starting "+threadName);
    if(t==null){
      t = new Thread(this, threadName);
      t.start();
    }
  }

}
