

class Cook extends Thread {

  volatile bool cancel = False;
  volatile bool orderReady = False;

  public void run() {
    while(!cancel){
      while(!orderReady);

      calculate_permission();

      return;
    }
  }

  void takeOrder(){
    return;
  }

  void endShift(){
    return;
  }

}
