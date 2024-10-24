package ArbolSintactico;

public class Declarax {
    private String token; // Representa un token
    private String var; // Representa un identificador
    private Typex type; // Representa el tipo de la variable
    
    // Constructor para variables
    public Declarax(String var, Typex type) {
        this.var = var;
        this.type = type;
    }

    // Constructor para tokens
    public Declarax(String token) {
        this.token = token;
    }

    // Métodos para acceder a la información
    public String getVar() {
        return var;
    }

    public Typex getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    // Método para determinar si es una variable o un token
    public boolean isVariable() {
        return var != null && type != null;
    }

    public boolean isToken() {
        return token != null;
    }
        public String toTreeString(int prof) {
            String indent = " ".repeat(prof * 2);
            if (isVariable()) {
                return indent + " └──  " + type.getTypex() + " -> " + var;
            } else if (isToken()) {
                return indent + "Token: " + token;
            } else {
                return indent + "Declaracion rara";
            }
        }
}
