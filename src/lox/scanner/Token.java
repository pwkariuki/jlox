package lox.scanner;

/**
 * Represents a single comment in Lox.
 *
 * <p>
 *   A token is a categorized piece of source code (e.g. a keyword,
 *   identifier, literal, or operator). Each token contains information
 *   about its type, the raw lexeme from the source, an optional literal value,
 *   and the line number where it was parsed from.
 * </p>
 */
public class Token {
  final TokenType type;
  public final String lexeme;
  final Object literal;
  final int line;

  Token(TokenType type, String lexeme, Object literal, int line) {
    this.type = type;
    this.lexeme = lexeme;
    this.literal = literal;
    this.line = line;
  }

  public String toString() {
    return type + " " + lexeme + " " + literal;
  }
}
