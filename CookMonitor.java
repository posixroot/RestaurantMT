

class CookMonitor {

  private int cooks;
  private int maxCooks;
  private Cook[] cookObj;

  public CookMonitor(int cooks) {
    this.cooks = cooks;
    this.maxCooks = cooks;
  }

  void spawnCooks(){
    cookObj = new Cook[cooks];
    for(int i=0; i<maxCooks; i++) {
      cookObj[i] = new Cook();
      cookObj[i].start();
    }
  }

  synchronized Cook getCook(){
    return cookObj;
  }

  synchronized putCook(Cook doneCook) {
    return;
  }
}
