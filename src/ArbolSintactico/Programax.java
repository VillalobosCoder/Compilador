package ArbolSintactico;
import java.util.Vector;

public class Programax {
    //private Declarax s1;
    private Vector s1;
    private Statx s2;
    
    public Programax(Vector st1, Statx st2) {
        s1 = st1;
        s2 = st2;
    }
    
    public Vector getDeclaration() {return s1;}
    
    public Statx getStatement() {return s2;}

    public String toTreeString(int prof) {
        StringBuilder builder = new StringBuilder();
        String indent = " ".repeat(prof * 2);
        builder.append(indent).append("Programa:\n");
        builder.append(indent).append("   └── D\n");
        for (Object decl : s1) {
            if (decl instanceof Declarax) {
                builder.append(((Declarax)decl).toTreeString(prof + 1)).append("\n");
            }
        }
        builder.append(indent).append(" └──  S\n");
        if (s2 != null) {
            builder.append(s2.toTreeString(prof + 1));
        }
        return builder.toString();
    }
}
