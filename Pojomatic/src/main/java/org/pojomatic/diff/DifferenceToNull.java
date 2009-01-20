package org.pojomatic.diff;

public class DifferenceToNull implements Differences {
  private final Object instance;

  public DifferenceToNull(Object instance) {
    this.instance = instance;
  }

  @Override
  public String toString() {
    return "the object {" + instance + "} is different than null" ;
  }

  public boolean areEqual() {
    return false;
  }
}
