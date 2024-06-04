
package ArbolSintactico;

public class Printx extends Statx {
    Expx s1;

    public Printx(Expx st1) {
        s1 = st1;
    }

    @Override
    public String toTreeString(int prof) {
        String indent = " ".repeat(prof * 2);
        return indent + " └── Print\n" + s1.toTreeString(prof + 1);
    }
}
