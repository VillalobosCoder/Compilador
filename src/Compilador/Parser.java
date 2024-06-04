package Compilador;

import ArbolSintactico.*;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.ArrayUtils;

public class Parser {

  // Declaración de variables----------------
  Programax p = null;
  String[] tipo = null;
  String[] variable;
  String byteString;
  private Vector tablaSimbolos = new Vector();
  private final Scanner s;
  final int ifx = 1, thenx = 2, elsex =
    3, /* beginx = 4, *//* endx = 5 */printx = 6, semi = 7, sum = 8, igual =
    9, igualdad = 10, intx = 11, stringx = 12, id = 13, EOF_CODE = 14;
  private int tknCode, tokenEsperado;
  private String token, tokenActual, log;

  // Sección de bytecode
  private int cntBC = 0;
  private String bc;
  private int jmp1, jmp2, jmp3;
  private int aux1, aux2, aux3;
  private String pilaBC[] = new String[100];
  private String memoriaBC[] = new String[10];
  private String pilaIns[] = new String[50];
  private int retornos[] = new int[10];
  private int cntIns = 0;

  // ---------------------------------------------

  public Parser(String codigo) {
    s = new Scanner(codigo);
    token = s.getToken(true);
    tknCode = stringToCode(token);
    p = P();
  }

  // INICIO DE ANÁLISIS SINTÁCTICO
  public void advance() {
    if (tknCode != EOF_CODE) {
      token = s.getToken(true);
      tokenActual = s.getToken(false);
      tknCode = stringToCode(token);
    }
  }

  public void eat(int t) {
    tokenEsperado = t;
    if (tknCode == t) {
      setLog("Token: " + token + "\n" + "Tipo:  " + s.getTipoToken());
      advance();
    } else {
      error(token, "token tipo:" + t);
    }
  }

  public Programax P() {
    if (p == null) {
      p = parseProgram();
    }
    return p;
  }

  private Programax parseProgram() {
    D();
    createTable();
    Statx s = S();
    return new Programax(tablaSimbolos, s);
  }

  public Declarax D() {
    if (tknCode == intx || tknCode == stringx) {
      Typex t = T();
      if (tknCode == id) {
        String identificador = token;
        eat(id);
        eat(semi);
        Declarax decl = new Declarax(identificador, t);
        tablaSimbolos.addElement(decl);

        D();
        return decl;
      } else {
        error(token, "(id) esperado después del tipo");
        return null;
      }
    } else if (
      tknCode == EOF_CODE ||
      tknCode == ifx ||
      tknCode == printx ||
      tknCode == id
    ) {
      return null;
    } else {
      return null;
    }
  }

  public Typex T() {
    Typex tipo = null;
    if (tknCode == intx) {
      tipo = new Typex("int");
      eat(intx);
    } else if (tknCode == stringx) {
      tipo = new Typex("string");
      eat(stringx);
    } else {
      error(token, "Tipo esperado (int/string)");
    }
    return tipo;
  }

  public Statx S() {
    switch (tknCode) {
      case ifx:
        Expx e1;
        Statx s1, s2;
        eat(ifx);
        e1 = E();
        eat(thenx);
        s1 = S();
        eat(elsex);
        s2 = S();
        return new Ifx(e1, s1, s2);
      /*
       * case beginx:
       * eat(beginx);
       * S();
       * L();
       * return null;
       */
      case id:
        Idx i;
        Expx e;
        eat(id);
        i = new Idx(tokenActual);
        declarationCheck(tokenActual);
        eat(igual);
        e = E();

        return new Asignax(i, e);
      case printx:
        Expx ex;
        eat(printx);
        ex = E();
        byteCode("print", tokenActual);
        return new Printx(ex);
      default:
        error(token, "(if | begin | id | print)");
        return null;
    }
  }

  public void L() {
    switch (tknCode) {
      /*
       * case endx:
       * eat(endx);
       * break;
       */
      case semi:
        eat(semi);
        S();
        L();
        break;
      default:
        error(token, "(end | ;)");
    }
  }

  public Expx E() {
    Idx i1, i2;
    String comp1, comp2;

    if (tknCode == id) {
      comp1 = token;
      declarationCheck(comp1);
      eat(id);
      i1 = new Idx(comp1);

      if (tknCode == sum || tknCode == igualdad) {
        switch (tknCode) {
          case sum:
            eat(sum);
            comp2 = token;
            declarationCheck(comp2);
            eat(id);
            i2 = new Idx(comp2);
            compatibilityCheck(comp1, comp2);
            byteCode("suma", comp1, comp2);
            return new Sumax(i1, i2);
          case igualdad:
            eat(igualdad);
            comp2 = token;
            declarationCheck(comp2);
            eat(id);
            i2 = new Idx(comp2);
            compatibilityCheck(comp1, comp2);
            Declarax declIgual = new Declarax("==", new Typex("operador"));
            tablaSimbolos.addElement(declIgual);
            byteCode("igualdad", comp1, comp2);
            return new Comparax(i1, i2);
          default:
            error(token, "(+ / ==) esperado después del identificador");
            return null;
        }
      } else {
        return i1;
      }
    } else {
      error(token, "(id) esperado al inicio de la expresión");
      return null;
    }
  }

  // FIN DEL ANÁLISIS SINTÁCTICO

  public void error(String token, String t) {
    switch (
      JOptionPane.showConfirmDialog(
        null,
        "Error sintáctico:\n" +
        "El token:(" +
        token +
        ") no concuerda con la gramática del lenguaje,\n" +
        "se espera: " +
        t +
        ".\n" +
        "¿Desea detener la ejecución?",
        "Ha ocurrido un error",
        JOptionPane.YES_NO_OPTION
      )
    ) {
      case JOptionPane.NO_OPTION:
        int e = 1;
        break;
      case JOptionPane.YES_OPTION:
        System.exit(0);
        break;
    }
  }

  public int stringToCode(String t) {
    int codigo = 0;
    switch (t) {
      case "if":
        codigo = 1;
        break;
      case "then":
        codigo = 2;
        break;
      case "else":
        codigo = 3;
        break;
      /*
       * case "begin":
       * codigo = 4;
       * break;
       */
      case "end":
        codigo = 5;
        break;
      case "print":
        codigo = 6;
        break;
      case ";":
        codigo = 7;
        break;
      case "+":
        codigo = 8;
        break;
      case ":=":
        codigo = 9;
        break;
      case "==":
        codigo = 10;
        break;
      case "int":
        codigo = 11;
        break;
      case "string":
        codigo = 12;
        break;
      case "EOF":
        codigo = 14;
        break;
      default:
        codigo = 13;
        break;
    }
    return codigo;
  }

  public void setLog(String l) {
    if (log == null) {
      log = l + "\n \n";
    } else {
      log = log + l + "\n \n";
    }
  }

  public String getLog() {
    return log;
  }

  // -----------------------------------------------

  public void createTable() {
    variable = new String[tablaSimbolos.size()];
    tipo = new String[tablaSimbolos.size()];

    System.out.println("-----------------");
    System.out.println("TABLA DE SÍMBOLOS");
    System.out.println("-----------------");
    for (int i = 0; i < tablaSimbolos.size(); i++) {
      Object item = tablaSimbolos.get(i);
      if (item instanceof Declarax) {
        Declarax dx = (Declarax) item;
        variable[i] = dx.getVar();
        byteCode("igual", variable[i]);
        if (dx.isVariable()) {
          tipo[i] = dx.getType().getTypex();
        } else if (dx.isToken()) {
          tipo[i] = "Token";
        }
        System.out.println(variable[i] + ": " + tipo[i]);
      } else if (item instanceof String) {
        variable[i] = (String) item;
        tipo[i] = "Token";
        System.out.println(variable[i] + ": " + tipo[i]);
      }
    }

    ArrayUtils.reverse(variable);
    ArrayUtils.reverse(tipo);

    System.out.println("-----------------\n");
  }

  public Vector getTablaSimbolos() {
    return tablaSimbolos;
  }

  public void declarationCheck(String s) {
    boolean valido = false;
    for (int i = 0; i < tablaSimbolos.size(); i++) {
      if (s.equals(variable[i])) {
        valido = true;
        break;
      }
    }
    if (!valido) {
      System.out.println(
        "La varible " + s + " no está declarada.\nSe detuvo la ejecución."
      );
      javax.swing.JOptionPane.showMessageDialog(
        null,
        "La varible [" + s + "] no está declarada",
        "Error",
        javax.swing.JOptionPane.ERROR_MESSAGE
      );
      int e = 1;
    }
  }

  // Chequeo de tipos consultando la tabla de símbolos
  public void compatibilityCheck(String s1, String s2) {
    System.out.println(
      "CHECANDO COMPATIBILIDAD ENTRE TIPOS (" + s1 + ", " + s2 + "). "
    );
    for (int i = 0; i < tablaSimbolos.size(); i++) {
      Object item = tablaSimbolos.get(i);
      if (item instanceof Declarax) {
        Declarax elementoCompara1 = (Declarax) item;
        if (s1.equals(elementoCompara1.getVar())) {
          System.out.println(
            "Se encontró el primer elemento en la tabla de símbolos..."
          );
          for (int j = 0; j < tablaSimbolos.size(); j++) {
            Object item2 = tablaSimbolos.get(j);
            if (item2 instanceof Declarax) {
              Declarax elementoCompara2 = (Declarax) item2;
              if (s2.equals(elementoCompara2.getVar())) {
                System.out.println(
                  "Se encontró el segundo elemento en la tabla de símbolos..."
                );
                if (
                  elementoCompara1.isVariable() && elementoCompara2.isVariable()
                ) {
                  Typex tipo1 = elementoCompara1.getType();
                  Typex tipo2 = elementoCompara2.getType();
                  if (!tipo1.getTypex().equals(tipo2.getTypex())) {
                    JOptionPane.showMessageDialog(
                      null,
                      "Incompatibilidad de tipos: " +
                      elementoCompara1.getVar() +
                      " (" +
                      tipo1.getTypex() +
                      "), " +
                      elementoCompara2.getVar() +
                      " (" +
                      tipo2.getTypex() +
                      ").",
                      "Error",
                      JOptionPane.ERROR_MESSAGE
                    );
                    return; // Salimos del método después de encontrar incompatibilidad
                  }
                  return; // Salimos del método después de verificar compatibilidad (sin errores)
                }
              }
            }
          }
        }
      }
    }
  }

  public void byteCode(String tipo, String s1, String s2) {
    int pos1 = -1, pos2 = -1;

    for (int i = 0; i < variable.length; i++) {
      if (s1.equals(variable[i])) {
        pos1 = i;
      }
      if (s2.equals(variable[i])) {
        pos2 = i;
      }
    }

    switch (tipo) {        
      case "igualdad":
        System.out.println("Comparando " + s1 + " == " + s2 + ".");
        ipbc(cntIns + ": iload_" + pos1);
        ipbc(cntIns + ": iload_" + pos2);
        ipbc(cntIns + ": ifne " + (cntIns + 5));
        jmp1 = cntBC;
        break;
      case "suma":
        System.out.println("Sumando " + s1 + " + " + s2 + ".");
        ipbc(cntIns + ": iload_" + pos1);
        ipbc(cntIns + ": iload_" + pos2);
        ipbc(cntIns + ": iadd");
        jmp2 = cntBC;
        break;
    }
  }

  public void byteCode(String tipo, String s1) {
    int pos1 = -1;
    for (int i = 0; i < variable.length; i++) {
      if (s1.equals(variable[i])) {
        pos1 = i;
      }
    }
    switch (tipo) {
      case "igual":
        ipbc(cntIns +  ": istore_" + pos1);
        pilaBC[cntBC + 3] = cntIns + 4 + ": istore_" + pos1;
        
        jmp2 = cntBC;
      break;
      case "print":
        System.out.println("Imprimiendo " + s1 + ".");
        ipbc(cntIns + ": iprint");
        jmp3 = cntBC;
      break;
    }
  }

  public void ipbc(String ins) {
    while (pilaBC[cntBC] != null) {
      cntBC++;
    }
    cntIns++;
    pilaBC[cntBC] = ins;
    cntBC++;
  }

  public String getBytecode() {
    String JBC = "";
    for (int i = 0; i < pilaBC.length; i++) {
      if (pilaBC[i] != null) {
        JBC = JBC + pilaBC[i] + "\n";
      }
    }
    return JBC;
  }

}
