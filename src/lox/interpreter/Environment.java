package lox.interpreter;

import java.util.HashMap;
import java.util.Map;
import lox.scanner.Token;

/**
 * Represents a scope environment for variable storage in the Lox interpreter.
 * Implements lexical scoping through a chain of nested environments, where each
 * environment has a reference to its enclosing scope. Handles variable definition,
 * lookup, and assignment.
 */
public class Environment {
  private final Environment enclosing; // reference to its enclosing scope
  private final Map<String, Object> values = new HashMap<>();

  /**
   * Creates a new global environment with no enclosing scope.
   */
  Environment() {
    enclosing = null;
  }

  /**
   * Creates a new local environment with the given enclosing scope.
   *
   * @param enclosing the outer scope that encloses this environment
   */
  Environment(Environment enclosing) {
    this.enclosing = enclosing;
  }

  /**
   * Defines a new variable in the current scope.
   * If the variable already exists, its value is overwritten.
   *
   * @param name the variable name
   * @param value the value to bind to the name
   */
  void define(String name, Object value) {
    values.put(name, value);
  }

  /**
   * Looks up a variable's value in the current scope or any enclosing scope.
   *
   * @param name token containing the variable name to look up
   * @return the variable's value
   * @throws RuntimeError if the variable is not found in any scope
   */
  Object get(Token name) {
    if (values.containsKey(name.lexeme)) {
      return values.get(name.lexeme);
    }

    // Recursively lookup a variable.
    if (enclosing != null) {
      return enclosing.get(name);
    }

    throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
  }

  /**
   * Assigns a new value to an existing variable in the current or enclosing scope.
   *
   * @param name token containing the variable name
   * @param value the new value to assign
   * @throws RuntimeError if the variable does not exist in any scope
   */
  void assign(Token name, Object value) {
    if (values.containsKey(name.lexeme)) {
      values.put(name.lexeme, value);
      return;
    }

    // Recursively lookup a variable.
    if (enclosing != null) {
      enclosing.assign(name, value);
      return;
    }

    throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
  }
}
