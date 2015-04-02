

class TableMonitor {

  private int tables;
  private int maxtables;

  public TableMonitor(int tables){
    this.tables = tables;
    this.maxtables = tables;
  }

  synchronized int getTable(){

    while(tables==0) wait();

    tables--;
    return (maxtables - tables);
  }

  synchronized void putTable(){
    tables++;
    notifyAll();
  }

}
