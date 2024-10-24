package Compilador;

import javax.swing.JOptionPane;

public class Scanner {

    private int k;
    private final String[] tokens;
    private String tipoToken;
    private String token;
    private final String[] reservadas = {
        "if", "then", "else", "begin", "print", "end", "int", "string",
    };
    private final String[] operadores = {"==", ":=", "+"};
    private final String delimitador = ";";
    private final String EOF = "EOF";

    public Scanner(String codigo) {
        tokens = codigo.split("\\s+");
        k = 0;
        token = "";
    }

    public String getToken(boolean b) {
        boolean tokenValido = false;

        if (k == tokens.length) {
            return EOF; 
        }
        token = tokens[k];
        if (b) {
            k++;
        }

        for (String reservada : reservadas) {
            if (token.equalsIgnoreCase(reservada)) {
                tokenValido = true;
                setTipoToken("Palabra reservada", b);
                return token;
            }
        }

        if (esFlotante(token) || esEntero(token)) {
            setTipoToken("Numerico", b);
            return token;
        }

        for (String operador : operadores) {
            if (token.equals(operador)) {
                tokenValido = true;
                setTipoToken("Operador", b);
                return token;
            }
        }

        if (token.equals(delimitador)) {
            tokenValido = true;
            setTipoToken("Delimitador", b);
            return token;
        }

        if (validaIdentificador(token)) {
            tokenValido = true;
            setTipoToken("Identificador", b);
            return token;
        }

        if(!tokenValido) {
            error("el token \"" + token + "\" es inválido para el lenguaje.");
            return "TOKEN INVÁLIDO";
        }
        return token;
    }

    public boolean validaIdentificador(String t) {
        boolean tokenValido = false;
        char[] charArray;
        charArray = t.toCharArray();
        int i=0;
        
       //Validación del primer caracter:
        if((charArray[i]>='a' && charArray[i]<='z') || 
                (charArray[i] >= 'A' && charArray[i] <= 'Z') ||
                (charArray[i]=='_') || (charArray[i]=='-')){
            tokenValido = true;
        }
        //Validación del resto del token (si su longitud es mayor a 1):
        if(t.length() > 1 && tokenValido) {
            for(int j=1 ; j<charArray.length ; j++) {
                if((charArray[j]>='a' && charArray[j]<='z') || 
                (charArray[j] >= 'A' && charArray[j] <= 'Z') ||
                (charArray[j]=='_') || (charArray[j]=='-') || (charArray[j]>='0' && charArray[j]<='9')){
                    tokenValido = true;
                }
            }
        }
        else if(t.length() > 1 && tokenValido) {
            tokenValido = false;
        }
        return tokenValido;
    }
    public void setTipoToken(String tipo, boolean b) {
        if (b) {
            tipoToken = tipo;
        }
    }

    public String getTipoToken() {
        return tipoToken;
    }

    private boolean esEntero(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean esFlotante(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    
    public void error(String error) {
        switch(JOptionPane.showConfirmDialog(null,
                "Error léxico: " + error + ".\n"
                        + "¿Desea detener la ejecución?",
                "Ha ocurrido un error",
                JOptionPane.YES_NO_OPTION)) {
            case JOptionPane.NO_OPTION:
                int e = 1;
                break;
                    
            case JOptionPane.YES_OPTION:
                System.exit(0);
                break;
        }
    }
}