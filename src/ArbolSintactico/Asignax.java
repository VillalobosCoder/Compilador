
package ArbolSintactico;

public class Asignax extends Statx {
    private Idx s1;
    private Expx s2;

    public Asignax(Idx st1, Expx st2) {
        s1 = st1;
        s2 = st2;
    }

    public String getAsignax() {
        return s1.toString();
    }

    @Override
    public String toTreeString(int prof) {
        String indent = " ".repeat(prof * 2); 
        return indent + "Asignación:\n" +
                s1.toTreeString(prof + 1) + "\n" +
                s2.toTreeString(prof + 1);
    }

}
