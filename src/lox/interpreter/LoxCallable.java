package lox.interpreter;

import java.util.List;

/**
 * Interface representing any callable object in the Lox language.
 * This includes both native functions and user-defined functions/methods.
 *
 * <p>
 *   The interface defines the core functionality needed to make an object
 *   callable within the Lox interpreter: determining its parameter count and
 *   executing the call with given arguments.
 * </p>
 */
public interface LoxCallable {
  /**
   * Returns the number of arguments this callable object expects.
   *
   * @return the number of parameters the callable accepts
   */
  int arity();

  /**
   * Executes this callable with the given arguments.
   *
   * @param interpreter the interpreter instance executing the call
   * @param arguments the list of evaluated arguments to pass to the callable
   * @return the result of executing the callable
   */
  Object call(Interpreter interpreter, List<Object> arguments);
}
