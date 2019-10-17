package target;
import java.util.HashSet;
import java.util.Set;
import java.io.*;
public class Triangle {
  private double side1;
  private double side2;
  private double side3;
  public Triangle(  double side1,  double side2,  double side3) throws TriangleException {
    saveLog("Triangle.java:methodDeclaration:Triangle - line: 13");
    saveLog("Triangle.java:assignment:this.side1 - line: 14");
    this.side1=side1;
    saveLog("Triangle.java:assignment:this.side2 - line: 15");
    this.side2=side2;
    saveLog("Triangle.java:assignment:this.side3 - line: 16");
    this.side3=side3;
    saveLog("Triangle.java:methodInvocation:allSidesAreZero - line: 18");
    saveLog("Triangle.java:methodInvocation:hasImpossibleSides - line: 18");
    saveLog("Triangle.java:methodInvocation:violatesTriangleInequality - line: 18");
    if (allSidesAreZero() || hasImpossibleSides() || violatesTriangleInequality()) {
      throw new TriangleException();
    }
  }
  public TriangleKind getKind(){
    saveLog("Triangle.java:methodDeclaration:getKind - line: 23");
    saveLog("Triangle.java:variableDeclaration:[uniqueSides=getNumberOfUniqueSides()] - line: 24");
    saveLog("Triangle.java:methodInvocation:getNumberOfUniqueSides - line: 24");
    int uniqueSides=getNumberOfUniqueSides();
    if (uniqueSides == 1) {
      saveLog("Triangle.java:return:TriangleKind.EQUILATERAL - line: 27");
      return TriangleKind.EQUILATERAL;
    }
    if (uniqueSides == 2) {
      saveLog("Triangle.java:return:TriangleKind.ISOSCELES - line: 31");
      return TriangleKind.ISOSCELES;
    }
    saveLog("Triangle.java:return:TriangleKind.SCALENE - line: 34");
    return TriangleKind.SCALENE;
  }
  public void a(){
    saveLog("Triangle.java:methodDeclaration:a - line: 37");
  }
  private boolean allSidesAreZero(){
    saveLog("Triangle.java:methodDeclaration:allSidesAreZero - line: 39");
    saveLog("Triangle.java:return:side1 == 0 && side2 == 0 && side3 == 0 - line: 40");
    return side1 == 0 && side2 == 0 && side3 == 0;
  }
  private boolean hasImpossibleSides(){
    saveLog("Triangle.java:methodDeclaration:hasImpossibleSides - line: 43");
    saveLog("Triangle.java:return:side1 < 0 || side2 < 0 || side3 < 0 - line: 44");
    return side1 < 0 || side2 < 0 || side3 < 0;
  }
  private boolean violatesTriangleInequality(){
    saveLog("Triangle.java:methodDeclaration:violatesTriangleInequality - line: 47");
    saveLog("Triangle.java:return:side1 + side2 <= side3 || side1 + side3 <= side2 || side2 + side3 <= side1 - line: 48");
    return side1 + side2 <= side3 || side1 + side3 <= side2 || side2 + side3 <= side1;
  }
  public int getNumberOfUniqueSides(){
    saveLog("Triangle.java:methodDeclaration:getNumberOfUniqueSides - line: 52");
    saveLog("Triangle.java:variableDeclaration:[sides=new HashSet<>()] - line: 53");
    Set<Double> sides=new HashSet<>();
    saveLog("Triangle.java:methodInvocation:add - line: 55");
    sides.add(side1);
    saveLog("Triangle.java:methodInvocation:add - line: 56");
    sides.add(side2);
    saveLog("Triangle.java:methodInvocation:add - line: 57");
    sides.add(side3);
    saveLog("Triangle.java:return:sides.size() - line: 59");
    saveLog("Triangle.java:methodInvocation:size - line: 59");
    return sides.size();
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
