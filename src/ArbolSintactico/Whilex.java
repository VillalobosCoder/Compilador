
package ArbolSintactico;

public class Whilex extends Statx{
    private Comparax s1;
    private Statx s2;
    
    public Whilex(Comparax st1, Statx st2) {
        s1 = st1;
        s2 = st2;
    }

    @Override
    public String toTreeString(int prof) {
        String indent = " ".repeat(prof * 2);
        return indent + "While:\n" +
               s1.toTreeString(prof + 1) + "\n" +
               s2.toTreeString(prof + 1);
    }
}
