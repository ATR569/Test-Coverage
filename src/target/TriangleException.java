package target;
import java.io.*;
public class TriangleException extends Exception {
  public TriangleException(){
    saveLog("TriangleException.java:methodDeclaration:TriangleException - line: 6");
  }
  private void saveLog(  String log){
    try {
      PrintWriter printW;
      FileWriter fileW;
      fileW=new FileWriter("log.txt",true);
      printW=new PrintWriter(fileW);
      printW.println(log);
      fileW.close();
    }
 catch (    IOException ex) {
    }
  }
}
