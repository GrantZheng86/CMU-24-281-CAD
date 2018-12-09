import java.util.ArrayList;


public class Octree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static ArrayList<double[]> traverseTree(Sphere sphere, Cube cube, int level, ArrayList<double[]> cubes){
		if (level >= 5) return cubes;
		if (allIn(cube, sphere)) return cubes;
		
		// Divide the cube into 8 smaller cubes
		return null;
	}
	
	/**
	 * This method calculates whether a cube is totally in a sphere/
	 * If the corner of the cube overlap with the sphere surface, it
	 * is still considered as all in.
	 * 
	 * @param cube a Cube object
	 * @param sphere a Sphere object
	 * @return a boolean, true if the cube is all in, false otherwise
	 */
	public static boolean allIn(Cube cube, Sphere sphere) {
		ArrayList<double[]> cubeCorners = cube.calculateCorners();
		
		for (int i = 0; i < cubeCorners.size(); i ++) {
			double currDist = calculateDist(cubeCorners.get(i), sphere.center);
			if (currDist > sphere.radius) 
				return false;
		}
		return true;
	}
	
	/**
	 * This method calculates the Euclidean distance
	 * between two points 
	 * 
	 * @param c1 point 1, a double array of size 3
	 * @param c2 point 2, a double array of size 3
	 * @return a double of the distances in absolute value
	 */
	public static double calculateDist(double[] c1, double[] c2) {
		double toReturn = 0;
		for (int i = 0; i < 3; i ++) {
			toReturn = (c1[i] - c2[i]) * (c1[i] - c2[i]);
		}
		
		toReturn = Math.sqrt(toReturn);
		return toReturn;
	}
	
	
	/**
	 * This is the inner class for Octree,
	 * Cube class contains information about a cube
	 * @author Grant Zheng
	 *
	 */
	class Cube{
		double x, y, z;
		double width, height, depth;
		
		/**
		 * The constructor takes two double list for information
		 * that can describe the cube
		 * 
		 * @param center a double list with a length 3 that indicates the
		 * coordinate of the center location of the cube
		 * 
		 * @param xyzLimits a double list with a length 3 that indicates 
		 * the width, height and depth. They corresponds to the x, y and z
		 * coordinates respectively
		 */
		public Cube(double center[], double[] xyzLimits) {
			this.x = center[0];
			this.y = center[1];
			this.z = center[2];
			this.width = xyzLimits[0];
			this.height = xyzLimits[1];
			this.depth = xyzLimits[2];
		}
		
		/**
		 * This method calculates and return all 8 corners of the cube
		 * 
		 * The corners are in counter-clockwise direction from the 
		 * upper left corner. Front then back
		 * 
		 * @return an ArrayList of double[] that indicates the corner locations
		 */
		
		public ArrayList<double[]> calculateCorners(){
			ArrayList<double[]> toReturn = new ArrayList<>();
			double halfWidth = this.width / 2;
			double halfHeight = this.height / 2;
			double halfDepth = this.depth / 2;
			
			double[] c0 = {this.x - halfWidth, this.y - halfHeight, this.z - halfDepth};
			double[] c1 = {this.x - halfWidth, this.y + halfHeight, this.z - halfDepth};
			double[] c2 = {this.x + halfWidth, this.y + halfHeight, this.z - halfDepth};
			double[] c3 = {this.x + halfWidth, this.y - halfHeight, this.z - halfDepth};
			double[] c4 = {this.x - halfWidth, this.y - halfHeight, this.z + halfDepth};
			double[] c5 = {this.x - halfWidth, this.y + halfHeight, this.z + halfDepth};
			double[] c6 = {this.x + halfWidth, this.y + halfHeight, this.z + halfDepth};
			double[] c7 = {this.x + halfWidth, this.y - halfHeight, this.z + halfDepth};
			
			toReturn.add(c0);
			toReturn.add(c1);
			toReturn.add(c2);
			toReturn.add(c3);
			toReturn.add(c4);
			toReturn.add(c5);
			toReturn.add(c6);
			toReturn.add(c7);
			
			return toReturn;
		}
		
		public ArrayList<Cube> divideTo8(){
			ArrayList<Cube> toReturn = new ArrayList<>();
			double subWidth = this.width / 4;
			double subHeight = this.height / 4;
			double subDepth = this.depth / 4;
			for (int i = 0; i < 8; i ++) {
				double newX;
				double newY;
				double newZ;
				
				if (i % 4 == 1 || i % 4 == 0) {
					newX = this.x - subWidth;
				} else {
					newX = this.x + subWidth;
				}
				
				if (i % 4 == 0 || i % 4 == 3) {
					newY = this.y - subHeight;
				} else {
					newY = this.y + subHeight;
				}
				
				if (i >= 4) {
					newZ = this.z - subDepth;
				} else {
					newZ = this.z + subDepth;
				}
				double[] newCenter = {newX, newY, newZ};
				double[] xyz = {subWidth, subHeight, subDepth};
				toReturn.add(new Cube(newCenter, xyz));
			}
			
			
			
			return toReturn;
		}
	}
	
	/**
	 * 
	 * @author Grant Zheng
	 *
	 */
	class Sphere{
		double center[];
		double radius;
		public Sphere(double center[], double radius ) {
			this.center = center;
			this.radius = radius;
		}
	}

}
