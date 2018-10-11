/**
 * 
 * @author Grant Zheng
 * The Computation class contains static mathematical computations required to 
 * compute triangles and the normal directions 
 */
public class Computation {

	/**
	 * The crossProduct methods compute the result of the cross product of two 
	 * 3 element vectors, arrayOne and arrayTwo, and returns a 3 element vector
	 * @param arrayOne arrayOne x arrayTwo
	 * @param arrayTwo arrayOne x arrayTwo
	 * @return Three vector result
	 */
	public static double[] crossProduct(double[] arrayOne, double[] arrayTwo) {
		double[] toReturn = new double[3];
		double u1 = arrayOne[0];
		double u2 = arrayOne[1];
		double u3 = arrayOne[2];
		double v1 = arrayTwo[0];
		double v2 = arrayTwo[1];
		double v3 = arrayTwo[2];
		
		toReturn[0] = u2 * v3 - v2 * u3;
		toReturn[1] = v1 * u3 - u1 * v3;
		toReturn[2] = u1 * v2 - v1 * u2;
		return toReturn;
	}
	
	/**
	 * This function computes the result from subtraction from two 3 element
	 * vector. This operation returns a 3 element vector which is pointing 
	 * from v2 to v1
	 * @param v1 v1 - v2
	 * @param v2 v1 - v2
	 * @return 3 element vector pointing from v2 to v1
	 */
	public static double[] vectorSubs(double[] v1, double[] v2) {
		
		double[] toReturn = new double[3];
		for (int i = 0; i < 3; i ++) {
			toReturn[i] = v1[i] - v2[i];
		}
		return toReturn;
	}
	
	/**
	 * This function normalizes a three element vector to a vector with a 
	 * unit length. This operation returns a 3 element vector which is the
	 * result of the normalization
	 * @param vector original 3 element vector with any length
	 * @return a 3 element vector pointing in the same direction with unit length
	 */
	public static double[] normalize(double[] vector) {
		double[] toReturn = new double[3];
		double length = Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2] );
		for (int i = 0; i < 3; i ++) {
			toReturn[i] = vector[i] / length;
		}
		return toReturn;
	}
	
	
	/**
	 * This function computes the z value for given x and y values. The function is 
	 * cos(x) * cos(2y), x and y are in rad. 
	 * @param xyValues a double array with length of 2, the first is x and the second is y
	 * @return a double value of the computation result
	 */
	public static double calculateZ(double[] xyValues) {
		return Math.cos(xyValues[0])*Math.cos(xyValues[1] * 2);
	}
}
