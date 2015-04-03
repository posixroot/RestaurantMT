
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

  synchronized Cook getCook(){
    while(cooksAvailable==0){
      try{
        wait();
      }catch(InterruptedException e){
        e.printStackTrace();
      }
    }

    int index=-1;
    for(int i=0;i<maxCooks;i++){
      if(cooksBitMap[i]==0){
        index = i;
        break;
      }
    }
    cooksBitMap[index]=1;
    cooksAvailable--;
    return cookObj[index];
  }

  synchronized void putCook(Cook doneCook) {
    int index = -1;
    for(int i=0;i<maxCooks;i++){
      if(cookObj[i]==doneCook){
        index = i;
        break;
      }
    }
    cooksBitMap[index] = 0;
    cooksAvailable++;
    notifyAll();
  }
}
