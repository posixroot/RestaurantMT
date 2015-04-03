
class TableMonitor {

  private int tablesAvailable;
  private int maxTables;
  private int[] table;

  public TableMonitor(int tables){
    tablesAvailable = tables;
    maxTables = tables;
    table = new int[tables];
    for(int i=0;i<maxTables;i++){
      table[i] = 0;
    }
  }

  synchronized int getTable(){
    while(tablesAvailable==0){
      try{
        wait();
      }catch(InterruptedException e){
        e.printStackTrace();
      }
    }

    int index = -1;
    for(int i=0; i<maxTables; i++){
      if(table[i]==0){
        index = i;
        break;
      }
    }
    table[index] = 1;
    tablesAvailable--;
    return (index+1);
  }

  synchronized void putTable(int t){
    table[t-1] = 0;
    tablesAvailable++;
    notifyAll();
  }

}
