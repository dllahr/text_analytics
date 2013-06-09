package controller.prediction.principalComponent;

import math.linearAlgebra.DenseFixedVector;
import math.linearAlgebra.Vector;
import orm.Eigenvalue;

public class PrincipalComponentVector {
	public final Eigenvalue eigenvalue;
	
	public final Vector vector;
	
	public PrincipalComponentVector(Eigenvalue eigenvalue, int numStems) {
		this.eigenvalue = eigenvalue;
		
		vector = new DenseFixedVector(numStems);
	}
}
