

package com.nuc.smartcloud.common;

import java.util.Vector;


public final class Collections {

  private Collections() {
  }

 
  public static void insertionSort(Vector vector, Comparator comparator) {
    int max = vector.size();
    for (int i = 1; i < max; i++) {
      Object value = vector.elementAt(i);
      int j = i - 1;
      Object valueB;
      while (j >= 0 && comparator.compare((valueB = vector.elementAt(j)), value) > 0) {
        vector.setElementAt(valueB, j + 1);
        j--;
      }
      vector.setElementAt(value, j + 1);
    }
  }

}
