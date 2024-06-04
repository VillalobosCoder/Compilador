// ALACA  Spring, 1999
// Demo of Assembler in Java
// Local variables, integer constants and the stack

public class Demo1 {

  public static void main(String[] args) {
     System.out.println("Hello World!");

   }

  public static void stackManip() {
     int x, y, z, u, v, w;

     // Note the different load instructions
     x = 2;   
     y = 6;
     z = 200;
     u = 2000000;  // 2000000 is in constant pool
                   // Note store instruction is different

    

    // arithemetic is done on the stack
     u = x + u;
     v = 3*(x/y - 2/(u+y));  // Note use of stack for intermediate results
     x++;
     y <<=  3;  
     w = 2*(x = 5);  // bad idea! But want to show dup 

    // calling a function (static method)
     v = add(x,y,-1);
   }



  public static int add(int first, int second, int third) {
     int sum;
     sum = first + second + third;
     return sum;
  }



}
     