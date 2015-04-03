

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
    while(tablesAvailable==0) wait();

    tablesAvailable--;
    return (getFreeTableIndex()+1);
  }

  int getFreeTableIndex(){
    for(int i=0; i<maxTables; i++){
      if(table[i]==0){
        table[i] = 1;
        return i;
      }
    }
    return -1; //If you are here something went wrong with the Monitor
  }

  synchronized void putTable(int t){
    tablesAvailable++;
    table[t-1] = 0;
    notifyAll();
  }

}
