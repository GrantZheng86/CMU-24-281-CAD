import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class RecursionForLoop {
	// Some properties of the box
	static final double WIDTH = 18;
	static final double HEIGHT = 10;
	static final double DEPTH = 12;
	static final double[] boxCenter = { 0, 0, 0 };
	static final double[] boxDimension = { WIDTH, HEIGHT, DEPTH };
	static final int STOP_LEVEL = 5;
	// Some properties of the sphere
	static final double RADIUS = 8;

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Input coordinates");

		StringTokenizer st = new StringTokenizer(reader.readLine());
		double x0, y0, z0;
		x0 = Double.parseDouble(st.nextToken());
		y0 = Double.parseDouble(st.nextToken());
		z0 = Double.parseDouble(st.nextToken());
		double[] sphereCenter = { x0, y0, z0 };
		Cube mainCube = new Cube(boxCenter, boxDimension);
		Sphere mainSphere = new Sphere(sphereCenter, RADIUS);
		ArrayList<Cube> cubesToPaint = new ArrayList<>();
		
		int inStatus = locationStatus(mainCube, mainSphere);
		

	}

	/**
	 * This method calculates the Euclidean distance between two points
	 * 
	 * @param c1 point 1, a double array of size 3
	 * @param c2 point 2, a double array of size 3
	 * @return a double of the distances in absolute value
	 */
	public static double calculateDist(double[] c1, double[] c2) {
		double toReturn = 0;
		for (int i = 0; i < 3; i++) {
			toReturn += (c1[i] - c2[i]) * (c1[i] - c2[i]);
		}

		toReturn = Math.sqrt(toReturn);
		return toReturn;
	}

	public static int locationStatus(Cube cube, Sphere sphere) {
		int cornersIn = numCorIn(cube, sphere);
		if (cornersIn == 8) return 0;			//Indicates that the cube is all in the sphere
		else if (cornersIn == 0) return 2;		//Indicates that the cube is all outside
		else return 1;							//Indicates that the cube is partially in
	}

	public static int numCorIn(Cube cube, Sphere sphere) {
		int cornersIn = 0;
		ArrayList<double[]> cornerList = cube.calculateCorners();
		
		for (int i = 0; i < cornerList.size(); i ++) {
			double currDist = calculateDist(cornerList.get(i), sphere.center);
			if (currDist <= sphere.radius) 
				cornersIn += 1;
		}
		
		return cornersIn;
		
	}
	/**
	 * 
	 * @author Grant Zheng
	 *
	 */
	static class Sphere {
		double[] center;
		double radius;

		public Sphere(double[] center, double radius) {
			this.center = center;
			this.radius = radius;
		}
	}

	/**
	 * 
	 * @author Grant Zheng
	 *
	 */
	static class Cube {
		double x, y, z;
		double width, height, depth;
		double center[];

		public Cube(double center[], double[] xyzLimits) {
			this.x = center[0];
			this.y = center[1];
			this.z = center[2];
			this.width = xyzLimits[0];
			this.height = xyzLimits[1];
			this.depth = xyzLimits[2];
			this.center = center;
		}

		/**
		 * This method calculates and return all 8 corners of the cube
		 * 
		 * The corners are in counter-clockwise direction from the upper left corner.
		 * Front then back
		 * 
		 * @return an ArrayList of double[] that indicates the corner locations
		 */

		public ArrayList<double[]> calculateCorners() {
			ArrayList<double[]> toReturn = new ArrayList<>();
			double halfWidth = this.width / 2;
			double halfHeight = this.height / 2;
			double halfDepth = this.depth / 2;

			double[] c0 = { this.x - halfWidth, this.y - halfHeight, this.z - halfDepth };
			double[] c1 = { this.x - halfWidth, this.y + halfHeight, this.z - halfDepth };
			double[] c2 = { this.x + halfWidth, this.y + halfHeight, this.z - halfDepth };
			double[] c3 = { this.x + halfWidth, this.y - halfHeight, this.z - halfDepth };
			double[] c4 = { this.x - halfWidth, this.y - halfHeight, this.z + halfDepth };
			double[] c5 = { this.x - halfWidth, this.y + halfHeight, this.z + halfDepth };
			double[] c6 = { this.x + halfWidth, this.y + halfHeight, this.z + halfDepth };
			double[] c7 = { this.x + halfWidth, this.y - halfHeight, this.z + halfDepth };

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
	}
}
