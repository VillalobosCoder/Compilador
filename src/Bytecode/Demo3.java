// ALACA  Fall, 2000
// Demo of Assembler in Java
// Simple array manipulation

public class Demo3 {

  public static void arrayManip() {
     int x[];
     int y[];
     int len;
     x = new int[3];
     x[2] = 4;
     x[0] = x[2];
     len = x.length;
     y = x;
     if ( y == x) 
        y = null;
   }
   
 


}