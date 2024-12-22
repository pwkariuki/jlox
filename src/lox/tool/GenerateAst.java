package lox.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * A utility class to generate abstract syntax tree (AST) classes for the Lox interpreter.
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
        "Binary   : Expr left, Token operator, Expr right",
        "Grouping : Expr expression",
        "Literal  : Object value",
        "Unary    : Token operator, Expr right"
    ));
  }

  private static void defineAst(String outputDir, String baseName, List<String> types)
          throws IOException {
    String path = outputDir + "/" + baseName + ".java";
    PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

    writer.println("package lox.AST;");
    writer.println();
    writer.println("import java.util.List;");
    writer.println("import lox.scanner.Token;");
    writer.println();
    writer.println("abstract class " + baseName + " {");

    // The AST classes.
    for (String type : types) {
      String className = type.split(":")[0].trim();
      String fields = type.split(":")[1].trim();
      defineTypes(writer, baseName, className, fields);
    }

    writer.println("}");
    writer.close();
  }

  private static void defineTypes(PrintWriter writer, String baseName,
                                  String className, String fieldList) {
    writer.println("  static class " + className + " extends " + baseName + " {");

    // Constructor.
    writer.println("    " + className + "(" + fieldList + ") {");

    // Store parameters in fields.
    String[] fields = fieldList.split(", ");
    for (String field : fields) {
      String name = field.split(" ")[1];
      writer.println("      this." + name + " = " + name + ";");
    }

    writer.println("    }");

    // Fields.
    writer.println();
    for (String field : fields) {
      writer.println("    final " + field + ";");
    }

    writer.println("  }");
    writer.println();
  }
}
