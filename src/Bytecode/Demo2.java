// ALACA  Spring, 1999
// Demo of Assembler in Java
// Simple program control

public class Demo2 {

  public static void main(String[] args) {
     System.out.println("Demo2");

   }

  public static void ifManip() {
     int x, y, z;
     boolean  flag = false; 
          flag = true; 
   
     x = 2;  y = 3; z = 4; 
     
     
     if (x < 2)
        x = 0;
  
     flag = (x < y);


     if (!flag)
        y++;
     else 
        z--;

     if (x >= y)
       if (z != 0)
          x++;
       else
          y++;
     else
          z++;


     if ((x > 1) | (y < 2))
        z = 1;
     else 
        z = 2; 

      switch (x) {
        case 1:  x++; break;
        case 2:  y++; break;
        case 3:  z++; break;
         default: x =0;
      }

   }



  public static void loopManip() {
     int x, i;
     boolean  flag;
     x = 1;
     flag = false;
   
     while (flag) {
       x++;
     }

     for (i = 1; i < 10; i++)
       x += i;
     }
          
   
}
     