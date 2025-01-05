package lox.ast;

/**
 * Converts Lox expressions into a string representation with full parenthesized syntax.
 * Used primarily for debugging and testing AST structure.
 */
public class AstPrinter implements Expr.Visitor<String> {

  /**
   * Prints an expression as a string.
   *
   * @param expr the expression to print
   * @return the expression's string representation
   */
  public String print(Expr expr) {
    return expr.accept(this);
  }

  /**
   * Creates a parenthesized string representation of an expression.
   *
   * @param name the name or operator to use
   * @param exprs the expressions to parenthesize
   * @return a parenthesized string combining the name and expressions
   */
  private String parenthesize(String name, Expr... exprs) {
    StringBuilder builder = new StringBuilder();

    builder.append("(").append(name);
    for (Expr expr : exprs) {
      builder.append(" ");
      builder.append(expr.accept(this));
    }
    builder.append(")");

    return builder.toString();
  }

  @Override
  public String visitAssignExpr(Expr.Assign expr) {
    return "";
  }

  @Override
  public String visitBinaryExpr(Expr.Binary expr) {
    return parenthesize(expr.operator.lexeme,
            expr.left, expr.right);
  }

  @Override
  public String visitCallExpr(Expr.Call expr) {
    return "";
  }

  @Override
  public String visitGetExpr(Expr.Get expr) {
    return "";
  }

  @Override
  public String visitGroupingExpr(Expr.Grouping expr) {
    return parenthesize("group", expr.expression);
  }

  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    if (expr.value == null) {
      return "nil";
    }
    return expr.value.toString();
  }

  @Override
  public String visitLogicalExpr(Expr.Logical expr) {
    return "";
  }

  @Override
  public String visitSetExpr(Expr.Set expr) {
    return "";
  }

  @Override
  public String visitThisExpr(Expr.This expr) {
    return "";
  }

  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    return parenthesize(expr.operator.lexeme, expr.right);
  }

  @Override
  public String visitVariableExpr(Expr.Variable expr) {
    return "";
  }
}
