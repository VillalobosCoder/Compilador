package ArbolSintactico;

public class Comparax extends Expx{
    private Expx s1;
    private Expx s2;
    
    public Comparax(Expx st1, Expx st2) {
        s1 = st1;
        s2 = st2;
    }

    @Override
    public String toTreeString(int prof) {
        String indent = " ".repeat(prof * 2);
        return indent + "Comparación:\n" +
               s1.toTreeString(prof + 1) + "\n" +
               s2.toTreeString(prof + 1);
    }
}
