package ArbolSintactico;

public class Sumax extends Expx{
    private Idx s1;
    private Idx s2;
    
    public Sumax(Idx st1, Idx st2){ 
        s1 = st1;
        s2 = st2;
    }

    @Override
    public String toTreeString(int prof) {
        String indent = " ".repeat(prof * 2);
        return indent + " └──  Suma\n     └──" +
               s1.toTreeString(prof + 1) + "\n    └──" +
               s2.toTreeString(prof + 1);
    }
}
