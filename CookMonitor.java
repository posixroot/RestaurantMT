

class CookMonitor {

  int cooksAvailable;
  int maxCooks;
  Cook[] cookObj;
  int[] cooksBitMap;

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
    // for(int i=0;i<cooks;i++){
    //   System.out.println(cookObj[i].threadName);
    // }

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
    // int index = getFreeCookIndex();
    return cookObj[index];
  }

  // int getFreeCookIndex(){
  //   int index=-1;
  //   for(int i=0;i<maxCooks;i++){
  //     if(cooksBitMap[i]==0){
  //       cooksBitMap[i] = 1;
  //       index = i;
  //       break;
  //     }
  //   }
  //   return index;
  // }

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
