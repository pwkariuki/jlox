package lox.interpreter;

import java.util.List;
import lox.ast.Stmt;

/**
 * Represents a callable function in the Lox language. Each function maintains its
 * own closure environment and function declaration AST node. Functions can be either
 * regular functions or class initializers (constructors).
 *
 * <p>
 *   When called, a new environment is created for the function's scope, chained to
 *   its closure environment. Parameters are bound to arguments in this new environment.
 *   For class initializers, the function always returns 'this' regardless of any
 *   explicit return values.
 * </p>
 */
public class LoxFunction implements LoxCallable {
  private final Stmt.Function declaration;
  private final Environment closure;
  private final boolean isInitializer;

  LoxFunction(Stmt.Function declaration, Environment closure, boolean isInitializer) {
    this.declaration = declaration;
    this.closure = closure;
    this.isInitializer = isInitializer;
  }

  /**
   * Creates a new function instance with 'this' bound to a specific class instance.
   *
   * <p>
   *   This method is used to implement method calls, where 'this' needs to refer
   *   to the instance the method was called on. It creates a new environment where
   *   'this' is bound to the given instance, while preserving the original closure
   *   scope chain.
   * </p>
   *
   * @param instance the class instance to bind 'this' to
   * @return the new LoxFunction with 'this' bound to the instance
   */
  public LoxFunction bind(LoxInstance instance) {
    Environment environment = new Environment(closure);
    environment.define("this", instance);
    return new LoxFunction(declaration, environment, isInitializer);
  }

  @Override
  public int arity() {
    return declaration.params.size();
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    Environment environment = new Environment(closure);
    for (int i = 0; i < declaration.params.size(); i++) {
      environment.define(declaration.params.get(i).lexeme, arguments.get(i));
    }

    try {
      interpreter.executeBlock(declaration.body, environment);
    } catch (Return returnValue) {
      if (isInitializer) {
        return closure.getAt(0, "this");
      }

      return returnValue.value;
    }

    if (isInitializer) {
      return closure.getAt(0, "this");
    }

    // Function calls return nil by default.
    return null;
  }

  @Override
  public String toString() {
    return "<fn " + declaration.name.lexeme + ">";
  }
}
