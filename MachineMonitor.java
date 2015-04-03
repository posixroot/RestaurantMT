

class MachineMonitor {

  private boolean burgerMachine = false;
  private boolean friesMachine = false;
  private boolean cokeMachine = false;

  synchronized String getMachine(int perm){
    if(perm==7){
      while(!burgerMachine && !friesMachine && !cokeMachine)
        wait();
    }else if(perm==6){
      while(!burgerMachine && !friesMachine)
        wait();
    }else if(perm==5){
      while(!burgerMachine && !cokeMachine)
        wait();
    }else if(perm==4){
      while(!burgerMachine)
        wait();
    }else if(perm==3){
      while(!friesMachine && !cokeMachine)
        wait();
    }else if(perm==2){
      while(!friesMachine)
        wait();
    }else if(perm==1){
      while(!cokeMachine)
        wait();
    }else if(perm==0){
      return "default";
    }

    if(burgerMachine && perm>=4){
      burgerMachine=false;
      return "burger";
    }else if(friesMachine && (perm==7||perm==6||perm==3||perm==2)){
      friesMachine=false;
      return "fries";
    }else if(cokeMachine && (perm==7||perm==5||perm==3||perm==1)){
      cokeMachine=false;
      return "coke";
    }

    return "default"; //If you are here, error in monitor code
  }

  synchronized void putMachine(String str) {
    if(str.equals("burger")){
      burgerMachine=true;
    }else if(str.equals("fries")){
      friesMachine=true;
    }else if(str.equals("coke")){
      cokeMachine=true;
    }
    notifyAll();
  }

}
