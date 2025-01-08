package lox.interpreter;

import java.util.List;
import java.util.Map;

/**
 * Represents a class in the Lox language. A class is a callable object that creates
 * instances when called, and maintains a collection of methods that can be inherited
 * through a superclass chain.
 *
 * <p>
 *   When called as a function, a LoxClass creates a new instance and invokes its
 *   initializer ("init" method) if it exists. The class also provides method lookup
 *   functionality that supports inheritance by searching the superclass chain.
 * </p>
 */
public class LoxClass implements LoxCallable {
  final String name;
  final LoxClass superclass;
  private final Map<String, LoxFunction> methods;

  LoxClass(String name, LoxClass superclass, Map<String, LoxFunction> methods) {
    this.name = name;
    this.methods = methods;
    this.superclass = superclass;
  }

  /**
   * Looks up a method by name in this class or its superclass chain.
   *
   * <p>
   *   The search starts in the current class's method map. If the method
   *   is not found and a superclass exists, the search continues up the
   *   inheritance chain.
   * </p>
   *
   * @param name the name of the method to find
   * @return the method if found, null otherwise
   */
  public LoxFunction findMethod(String name) {
    if (methods.containsKey(name)) {
      return methods.get(name);
    }

    // Reuse method from superclass.
    if (superclass != null) {
      return superclass.findMethod(name);
    }

    return null;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int arity() {
    LoxFunction initializer = findMethod("init");
    if (initializer == null) {
      return 0;
    }
    return initializer.arity();
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    LoxInstance instance = new LoxInstance(this);
    LoxFunction initializer = findMethod("init");
    if (initializer != null) {
      initializer.bind(instance).call(interpreter, arguments);
    }

    return instance;
  }
}
