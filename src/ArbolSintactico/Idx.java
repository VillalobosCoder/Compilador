package ArbolSintactico;

public class Idx extends Expx {
    private String s1;

    public Idx(String st1) {
        s1 = st1;
    }

    public String getIdx() {
        return s1;
    }

    @Override
    public String toTreeString(int prof) {
        return "".repeat(prof * 2) + "   Id -> " + s1;
    }
}
