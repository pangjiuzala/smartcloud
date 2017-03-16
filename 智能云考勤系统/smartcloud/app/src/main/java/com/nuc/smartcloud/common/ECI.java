
package com.nuc.smartcloud.common;


public abstract class ECI {

  private final int value;

  ECI(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

 
  public static ECI getECIByValue(int value) {
    if (value < 0 || value > 999999) {
      throw new IllegalArgumentException("Bad ECI value: " + value);
    }
    if (value < 900) { // Character set ECIs use 000000 - 000899
      return CharacterSetECI.getCharacterSetECIByValue(value);
    }
    return null;
  }

}
