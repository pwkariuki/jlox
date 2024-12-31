package lox.interpreter;

import lox.scanner.Token;

/**
 * Custom runtime exception for Lox interpreter errors.
 * Extends RuntimeException to add token information for error reporting,
 * allowing errors to be traced back to their source location in the code.
 */
public class RuntimeError extends RuntimeException {
  public final Token token;

  RuntimeError(Token token, String message) {
    super(message);
    this.token = token;
  }
}
