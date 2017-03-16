

package com.nuc.smartcloud.common.reedsolomon;


public final class ReedSolomonDecoder {

  private final GF256 field;

  public ReedSolomonDecoder(GF256 field) {
    this.field = field;
  }

  
  public void decode(int[] received, int twoS) throws ReedSolomonException {
    GF256Poly poly = new GF256Poly(field, received);
    int[] syndromeCoefficients = new int[twoS];
    boolean dataMatrix = field.equals(GF256.DATA_MATRIX_FIELD);
    boolean noError = true;
    for (int i = 0; i < twoS; i++) {
      // Thanks to sanfordsquires for this fix:
      int eval = poly.evaluateAt(field.exp(dataMatrix ? i + 1 : i));
      syndromeCoefficients[syndromeCoefficients.length - 1 - i] = eval;
      if (eval != 0) {
        noError = false;
      }
    }
    if (noError) {
      return;
    }
    GF256Poly syndrome = new GF256Poly(field, syndromeCoefficients);
    GF256Poly[] sigmaOmega =
        runEuclideanAlgorithm(field.buildMonomial(twoS, 1), syndrome, twoS);
    GF256Poly sigma = sigmaOmega[0];
    GF256Poly omega = sigmaOmega[1];
    int[] errorLocations = findErrorLocations(sigma);
    int[] errorMagnitudes = findErrorMagnitudes(omega, errorLocations, dataMatrix);
    for (int i = 0; i < errorLocations.length; i++) {
      int position = received.length - 1 - field.log(errorLocations[i]);
      if (position < 0) {
        throw new ReedSolomonException("Bad error location");
      }
      received[position] = GF256.addOrSubtract(received[position], errorMagnitudes[i]);
    }
  }

  private GF256Poly[] runEuclideanAlgorithm(GF256Poly a, GF256Poly b, int R)
      throws ReedSolomonException {
    // Assume a's degree is >= b's
    if (a.getDegree() < b.getDegree()) {
      GF256Poly temp = a;
      a = b;
      b = temp;
    }

    GF256Poly rLast = a;
    GF256Poly r = b;
    GF256Poly sLast = field.getOne();
    GF256Poly s = field.getZero();
    GF256Poly tLast = field.getZero();
    GF256Poly t = field.getOne();

    // Run Euclidean algorithm until r's degree is less than R/2
    while (r.getDegree() >= R / 2) {
      GF256Poly rLastLast = rLast;
      GF256Poly sLastLast = sLast;
      GF256Poly tLastLast = tLast;
      rLast = r;
      sLast = s;
      tLast = t;

      // Divide rLastLast by rLast, with quotient in q and remainder in r
      if (rLast.isZero()) {
        // Oops, Euclidean algorithm already terminated?
        throw new ReedSolomonException("r_{i-1} was zero");
      }
      r = rLastLast;
      GF256Poly q = field.getZero();
      int denominatorLeadingTerm = rLast.getCoefficient(rLast.getDegree());
      int dltInverse = field.inverse(denominatorLeadingTerm);
      while (r.getDegree() >= rLast.getDegree() && !r.isZero()) {
        int degreeDiff = r.getDegree() - rLast.getDegree();
        int scale = field.multiply(r.getCoefficient(r.getDegree()), dltInverse);
        q = q.addOrSubtract(field.buildMonomial(degreeDiff, scale));
        r = r.addOrSubtract(rLast.multiplyByMonomial(degreeDiff, scale));
      }

      s = q.multiply(sLast).addOrSubtract(sLastLast);
      t = q.multiply(tLast).addOrSubtract(tLastLast);
    }

    int sigmaTildeAtZero = t.getCoefficient(0);
    if (sigmaTildeAtZero == 0) {
      throw new ReedSolomonException("sigmaTilde(0) was zero");
    }

    int inverse = field.inverse(sigmaTildeAtZero);
    GF256Poly sigma = t.multiply(inverse);
    GF256Poly omega = r.multiply(inverse);
    return new GF256Poly[]{sigma, omega};
  }

  private int[] findErrorLocations(GF256Poly errorLocator) throws ReedSolomonException {
    // This is a direct application of Chien's search
    int numErrors = errorLocator.getDegree();
    if (numErrors == 1) { // shortcut
      return new int[] { errorLocator.getCoefficient(1) };
    }
    int[] result = new int[numErrors];
    int e = 0;
    for (int i = 1; i < 256 && e < numErrors; i++) {
      if (errorLocator.evaluateAt(i) == 0) {
        result[e] = field.inverse(i);
        e++;
      }
    }
    if (e != numErrors) {
      throw new ReedSolomonException("Error locator degree does not match number of roots");
    }
    return result;
  }

  private int[] findErrorMagnitudes(GF256Poly errorEvaluator, int[] errorLocations, boolean dataMatrix) {
    // This is directly applying Forney's Formula
    int s = errorLocations.length;
    int[] result = new int[s];
    for (int i = 0; i < s; i++) {
      int xiInverse = field.inverse(errorLocations[i]);
      int denominator = 1;
      for (int j = 0; j < s; j++) {
        if (i != j) {
        
          int term = field.multiply(errorLocations[j], xiInverse);
          int termPlus1 = ((term & 0x1) == 0) ? (term | 1) : (term & ~1);
          denominator = field.multiply(denominator, termPlus1);
        }
      }
      result[i] = field.multiply(errorEvaluator.evaluateAt(xiInverse),
          field.inverse(denominator));
     
      if (dataMatrix) {
        result[i] = field.multiply(result[i], xiInverse);
      }
    }
    return result;
  }

}
