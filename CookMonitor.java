

class CookMonitor {

  private int cooksAvailable;
  private int maxCooks;
  private Cook[] cookObj;
  private int[] cooksBitMap;

  public CookMonitor(Cook[] cookThread, int cooks) {
    this.cooksAvailable = cooks;
    this.maxCooks = cooks;
    cookObj = new Cook[cooks];
    for(int i=0;i<cooks;i++){
      cookObj[i] = cookThread[i];
    }
    cooksBitMap = new int[cooks];
    for(int i=0;i<cooks;i++){
      cooksBitMap[i] = 0;
    }
  }

  // void spawnCooks(){
  //   cookObj = new Cook[cooks];
  //   for(int i=0; i<maxCooks; i++) {
  //     cookObj[i] = new Cook();
  //     cookObj[i].start();
  //   }
  // }

  synchronized Cook getCook(){
    while(cooksAvailable==0) wait();

    cooksAvailable--;
    int index = getFreeCookIndex();
    return cookObj[index];
  }

  int getFreeCookIndex(){
    for(int i=0;i<maxCooks;i++){
      if(cooksBitMap[i]==0){
        cooksBitMap[i] = 1;
        return i;
      }
    }
    return -1;
  }

  synchronized void putCook(Cook doneCook) {
    for(int i=0;i<maxCooks;i++){
      if(cookObj[i]==donecook){
        cooksBitMap[i] = 0;
        cooksAvailable++;
        break;
      }
    }
    notifyAll();
  }
}
