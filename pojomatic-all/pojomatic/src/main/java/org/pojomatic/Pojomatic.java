package org.pojomatic;

import org.pojomatic.diff.Differences;
import org.pojomatic.internal.PojomatorFactory;
import org.pojomatic.internal.SelfPopulatingMap;

/**
 * <style>
 *   .java { margin-left: 2em; }
 * </style>
 * Static methods for implementing the {@link java.lang.Object#equals(Object)},
 * {@link java.lang.Object#hashCode()} and {@link java.lang.Object#toString()} methods on a
 * annotated POJO.  The actual work for a given class is done by a {@link Pojomator} created for
 * that class.  This class is careful to create only a single {@code Pojomator} per POJO class.
 * The overhead for looking up the {@code Pojomator} by POJO class is light, so a typical use in a
 * POJO class would be
 * <p class="java">
 * <code>
 * &nbsp;&nbsp;<font color="#646464">@Override</font>&nbsp;<font color="#7f0055"><b>public&nbsp;</b></font><font color="#7f0055"><b>int&nbsp;</b></font><font color="#000000">hashCode() {</font><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;<font color="#7f0055"><b>return&nbsp;</b></font><font color="#000000">Pojomatic.hashCode(</font><font color="#7f0055"><b>this</b></font><font color="#000000">);</font><br />
 * &nbsp;&nbsp;<font color="#000000">}</font><br>
 * <br/>
 * &nbsp;&nbsp;<font color="#646464">@Override</font>&nbsp;<font color="#7f0055"><b>public&nbsp;</b></font><font color="#7f0055"><b>boolean&nbsp;</b></font><font color="#000000">equals(Object other) {</font><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;<font color="#7f0055"><b>return&nbsp;</b></font><font color="#000000">Pojomatic.equals(</font><font color="#7f0055"><b>this</b></font><font color="#000000">, other);</font><br />
 * &nbsp;&nbsp;<font color="#000000">}</font><br>
 * <br/>
 * &nbsp;&nbsp;<font color="#646464">@Override</font>&nbsp;<font color="#7f0055"><b>public&nbsp;</b></font><font color="#000000">String toString() {</font><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;<font color="#7f0055"><b>return&nbsp;</b></font><font color="#000000">Pojomatic.toString(</font><font color="#7f0055"><b>this</b></font><font color="#000000">);</font><br />
 * &nbsp;&nbsp;<font color="#000000">}</font><br>
 * <br/>
 * </code>
 * </p>
 * Under the covers, these methods are referencing a {@link org.pojomatic.Pojomator Pojomator} instance
 * which is created lazily and cached on a per-class basis.  The performance penalty for this is
 * negligible, but if an interface is annotated for Pojomation, using the {@code Pojomator} directly
 * is required, since the {@code Pojomator} for a class will only reference properties in the class
 * and it's superclasses, but not any implemented interfaces.  To do this, first define a static
 * constant {@code POJOMATOR} in the interface:
 * <p class="java">
 * <code>
 *   <font color="#7f0055"><b>import&nbsp;</b></font>org.pojomatic.annotations.AutoProperty;<br />
 *   <font color="#7f0055"><b>import&nbsp;</b></font>org.pojomatic.Pojomator;<br />
 *   <font color="#7f0055"><b>import&nbsp;</b></font>org.pojomatic.Pojomatic;<br />
 *   <br />
 *   <font color="#646464">@AutoProperty</font><br />
 *   <font color="#7f0055"><b>public&nbsp;interface&nbsp;</b></font>Interface&nbsp;{<br />
 *   &nbsp;&nbsp;<font color="#7f0055"><b>static&nbsp;</b></font>Pojomator&lt;Interface&gt;&nbsp;POJOMATOR&nbsp;=&nbsp;Pojomatic.pojomator(Interface.<font color="#7f0055"><b>class</b></font>);<br />
 *   &nbsp;&nbsp;...<br />
 * }</code>
 * </p>
 * and then delegate to {@code POJOMATOR} in the implementing classes:
 * <p class="java">
 * <code>
 *   <font color="#7f0055"><b>public&nbsp;class&nbsp;</b></font>Implementation&nbsp;<font color="#7f0055"><b>implements&nbsp;</b></font>Interface&nbsp;{<br />
 *   &nbsp;&nbsp;<font color="#646464">@Override</font>&nbsp;<font color="#7f0055"><b>public&nbsp;</b></font><font color="#7f0055"><b>int&nbsp;</b></font>hashCode()&nbsp;{<br />
 *   &nbsp;&nbsp;&nbsp;&nbsp;<font color="#7f0055"><b>return&nbsp;</b></font>POJOMATOR.doHashCode(<font color="#7f0055"><b>this</b></font>);<br />
 *   &nbsp;&nbsp;}<br />
 *   <br />
 *   &nbsp;&nbsp;<font color="#646464">@Override</font>&nbsp;<font color="#7f0055"><b>public&nbsp;</b></font><font color="#7f0055"><b>boolean&nbsp;</b></font>equals(Object&nbsp;other)&nbsp;{<br />
 *   &nbsp;&nbsp;&nbsp;&nbsp;<font color="#7f0055"><b>return&nbsp;</b></font>POJOMATOR.doEquals(this,&nbsp;other);<br />
 *   &nbsp;&nbsp;}<br />
 *   <br />
 *   &nbsp;&nbsp;<font color="#646464">@Override</font>&nbsp;<font color="#7f0055"><b>public&nbsp;</b></font>String&nbsp;toString()&nbsp;{<br />
 *   &nbsp;&nbsp;&nbsp;&nbsp;<font color="#7f0055"><b>return&nbsp;</b></font>POJOMATOR.doToString(<font color="#7f0055"><b>this</b></font>);<br />
 *   &nbsp;&nbsp;}<br />
 *   &nbsp;&nbsp;...<br />
 *   }</code>
 * </p>
 *
 * @see Pojomator
 */
public class Pojomatic {

  private final static SelfPopulatingMap<Class<?>, Pojomator<?>> POJOMATORS =
    new SelfPopulatingMap<Class<?>, Pojomator<?>>() {
      @Override
      // compiler does not know that the type parameter to Pojomator is the same as the type
      // parameter to Class
      protected Pojomator<?> create(Class<?> key) {
        return PojomatorFactory.makePojomator(key);
      }
  };

  private Pojomatic() {}

  /**
   * Compute the {@code toString} representation for a POJO.
   * @param <T> the type of the POJO
   * @param pojo the POJO - must not be null
   * @return the {@code toString} representation of {@code pojo}.
   * @throws NoPojomaticPropertiesException if {@code pojo}'s class has no properties annotated for
   * use with Pojomatic
   * @see Pojomator#doToString(Object)
   */
  public static <T> String toString(T pojo) throws NoPojomaticPropertiesException {
    return pojomator(getClass(pojo)).doToString(pojo);
  }

  /**
   * Compute the {@code hashCode} for a POJO.
   * @param <T> the type of the POJO
   * @param pojo the POJO - must not be null
   * @return the {@code hashCode} for {@code pojo}.
   * @throws NoPojomaticPropertiesException if {@code pojo}'s class has no properties annotated for
   * use with Pojomatic
   * @see Pojomator#doHashCode(Object)
   */
  public static <T> int hashCode(T pojo) throws NoPojomaticPropertiesException {
    return pojomator(getClass(pojo)).doHashCode(pojo);
  }

  /**
   * Compute whether {@code pojo} and {@code other} are equal to each other in the sense of
   * {@code Object}'s {@code equals} method.
   * @param <T> the type of the POJO
   * @param pojo the POJO - must not be null
   * @param other the object to compare to for equality
   * @return whether {@code pojo} and {@code other} are equal to each other in the sense of
   * {@code Object}'s {@code equals} method.
   * @throws NoPojomaticPropertiesException if {@code pojo}'s class has no properties annotated for
   * use with Pojomatic
   * @see Pojomator#doEquals(Object, Object)
   */
  public static <T> boolean equals(T pojo, Object other) throws NoPojomaticPropertiesException {
    return pojomator(getClass(pojo)).doEquals(pojo, other);
  }

  /**
   * Compute whether {@code classA} and {@code classB} are compatible for equality as specified
   * by the documentation for {@link Pojomator#isCompatibleForEquality(Class)}.
   * @param classA the first class to check for compatibility for equality
   * @param classB the second class to check for compatibility for equality
   * @return {@code true} if the two classes are compatible for equality, or {@code false}
   * otherwise.
   */
  public static boolean areCompatibleForEquals(Class<?> classA, Class<?> classB) {
    return pojomator(classA).isCompatibleForEquality(classB);
  }

  /**
   * Compute the differences between {@code pojo} and {@code other} among the properties
   * examined by {@link #equals(Object, Object)} for type {@code T}.
   *
   * @param <T> the static type of the first object to compare
   * @param <S> the static type of the first object to compare
   * @param pojo the instance to diff against
   * @param other the instance to diff
   * @return the list of differences (possibly empty) between {@code instance} and {@code other}
   * among the properties examined by {@link #equals(Object, Object)} for type {@code T}.
   * @throws NullPointerException if {@code pojo} or {@code other} are null
   * (this behavior may change in future releases).
   * @throws NoPojomaticPropertiesException if {@code pojo}'s class has no properties
   * annotated for use with Pojomatic, or if the types of {@code pojo} and {@code other} are not
   * compatible for equality with each other (this behavior may change in future releases).
   */
  public static <T, S extends T> Differences diff(T pojo, S other)
  throws NullPointerException, NoPojomaticPropertiesException {
    if (pojo == null) {
      throw new NullPointerException("pojo is null");
    }
    if (other == null) {
      throw new NullPointerException("other is null");
    }
    return pojomator(getClass(pojo)).doDiff(pojo, other);
  }

  /**
   * Get the {@code Pojomator} for {@code pojoClass}. While the same instance will be returned every time
   * for a given value of {@code pojoClass}, highly performance-sensitive applications may want to cache the value
   * returned in a static variable on the class in question.
   * @param <T> the type represented by {@code pojoClass}
   * @param pojoClass the class to create a {@code Pojomator} for.
   * @return a {@code Pojomator<T>}
   * @throws NoPojomaticPropertiesException if {@code pojoClass} has no properties annotated for use
   * with Pojomatic
   */
  @SuppressWarnings("unchecked") // compiler does not know that the type parameter to Pojomator is T
  public static <T> Pojomator<T> pojomator(Class<T> pojoClass)
  throws NoPojomaticPropertiesException {
    return (Pojomator<T>) POJOMATORS.get(pojoClass);
  }

  @SuppressWarnings("unchecked") // Since Object.getClass returns Class<?>
  private static <T> Class<T> getClass(T pojo) {
    return (Class<T>) pojo.getClass();
  }
}
