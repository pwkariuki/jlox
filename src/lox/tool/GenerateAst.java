package lox.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * A utility class to generate abstract syntax tree (AST) classes for the Lox interpreter.
 *
 * <p>
 *   Generates Java source file representing the AST for expressions.
 *   Each expression is implemented as a derived class of the Expr abstract class.
 * </p>
 */
public class GenerateAst {

  /**
   * Generates AST classes for the Lox language.
   *
   * @param args command line arguments - requires an output directory path as first argument
   * @throws IOException if there is an error writing the output file
   */
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("Usage: generate_ast <output directory>");
      System.exit(64);
    }
    String outputDir = args[0];
    // description of each class type and its fields
    defineAst(outputDir, "Expr", Arrays.asList(
        "Assign   : Token name, Expr value",
        "Binary   : Expr left, Token operator, Expr right",
        "Grouping : Expr expression",
        "Literal  : Object value",
        "Logical  : Expr left, Token operator, Expr right",
        "Unary    : Token operator, Expr right",
        "Variable : Token name"
    ));
    defineAst(outputDir, "Stmt", Arrays.asList(
        "Block      : List<Stmt> statements",
        "Expression : Expr expression",
        "If         : Expr condition, Stmt thenBranch, Stmt elseBranch",
        "Print      : Expr expression",
        "Var        : Token name, Expr initializer",
        "While      : Expr condition, Stmt body"
    ));
  }

  private static void defineAst(String outputDir, String baseName, List<String> types)
          throws IOException {
    String path = outputDir + "/" + baseName + ".java";
    PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

    writer.println("package lox.ast;");
    writer.println();
    writer.println("import java.util.List;");
    writer.println("import lox.scanner.Token;");
    writer.println();
    writer.println("public abstract class " + baseName + " {");

    defineVisitor(writer, baseName, types);

    // The AST classes.
    for (String type : types) {
      String className = type.split(":")[0].trim();
      String fields = type.split(":")[1].trim();
      defineTypes(writer, baseName, className, fields);
    }
    // The base accept() method.
    writer.println();
    writer.println("  public abstract <R> R accept(Visitor<R> visitor);");
    writer.println("}");
    writer.close();
  }

  private static void defineTypes(PrintWriter writer, String baseName,
                                  String className, String fieldList) {
    writer.println("  public static class " + className + " extends " + baseName + " {");

    // Constructor.
    writer.println("    public " + className + "(" + fieldList + ") {");

    // Store parameters in fields.
    String[] fields = fieldList.split(", ");
    for (String field : fields) {
      String name = field.split(" ")[1];
      writer.println("      this." + name + " = " + name + ";");
    }

    writer.println("    }");

    // Visitor pattern.
    writer.println();
    writer.println("    @Override");
    writer.println("    public <R> R accept(Visitor<R> visitor) {");
    writer.println("      return visitor.visit" + className + baseName + "(this);");
    writer.println("    }");

    // Fields.
    writer.println();
    for (String field : fields) {
      writer.println("    public final " + field + ";");
    }

    writer.println("  }");
    writer.println();
  }

  private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
    writer.println("  public interface Visitor<R> {");

    for (String type : types) {
      String typeName = type.split(":")[0].trim();
      writer.println("    R visit" + typeName + baseName + "(" + typeName + " "
              + baseName.toLowerCase() + ");");
      writer.println();
    }
    writer.println("  }");
    writer.println();
  }
}
