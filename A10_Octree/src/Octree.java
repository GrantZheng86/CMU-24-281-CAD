import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Octree {

	// Some properties of the box
	static final double WIDTH = 18;
	static final double HEIGHT = 10;
	static final double DEPTH = 12;
	static final double[] boxCenter = {0, 0, 0};
	static final double[] boxDimension = {WIDTH, HEIGHT, DEPTH};
	
	
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
		double[] sphereCenter = {x0, y0, z0};
		Cube mainCube = new Octree.Cube(boxCenter, boxDimension); 
		Sphere mainSphere = new Sphere(sphereCenter, RADIUS);
		AllPartialInCubesWrapper leftCubes = traverseTree(mainSphere, mainCube, 0);
		
		PrintStream output = new PrintStream(new FileOutputStream(new File("Octree.wrl")));
		output.print(generateVRML(leftCubes, mainSphere));
		output.close();
	
	}

	/**
	 * 
	 * @param sphere
	 * @param cube
	 * @param level
	 * @return
	 */
	public static AllPartialInCubesWrapper traverseTree(Sphere sphere, Cube cube, int level) {
		ArrayList<Cube> allOut = new ArrayList<>();
		ArrayList<Cube> partialIn = new ArrayList<>();
		AllPartialInCubesWrapper toReturn = new AllPartialInCubesWrapper(allOut, partialIn, level);
		AllPartialInWrapper inStatus = allIn(cube, sphere);
		if (level >= 5) {
			
			if (inStatus.allIn) return toReturn;
			if (inStatus.partialIn) {
				toReturn.partialIn.add(cube);
			}
			if (!inStatus.partialIn) {
				toReturn.allOut.add(cube);
			}
			return toReturn;
		}

		if (inStatus.allIn)
			return toReturn;

		// Divide the cube into 8 smaller cubes
		ArrayList<Cube> subCubes = cube.divideTo8();
		for (int i = 0; i < 8; i++) {
			AllPartialInCubesWrapper curr = traverseTree(sphere, subCubes.get(i), level + 1);
			toReturn.allOut.addAll(curr.allOut);
			toReturn.partialIn.addAll(curr.partialIn);
		}

		return toReturn;
	}

	/**
	 * This method calculates whether a cube is totally in a sphere If the corner of
	 * the cube overlap with the sphere surface, it is still considered as all in.
	 * 
	 * @param cube   a Cube object
	 * @param sphere a Sphere object
	 * @return a boolean, true if the cube is all in, false otherwise
	 */
	public static AllPartialInWrapper allIn(Cube cube, Sphere sphere) {
		ArrayList<double[]> cubeCorners = cube.calculateCorners();
		int cornersIn = 0;
		
		for (int i = 0; i < cubeCorners.size(); i++) {
			double currDist = calculateDist(cubeCorners.get(i), sphere.center);
			if (currDist <= sphere.radius)
				cornersIn += 1;
		}
		
		if (cornersIn == 8) return new AllPartialInWrapper(true, true);
		if (cornersIn == 0) return new AllPartialInWrapper(false, false);
		return new AllPartialInWrapper(false, true);
	}
	
	public static String generateVRML(AllPartialInCubesWrapper cubes, Sphere sphere) {
		double x0 = sphere.center[0];
		double y0 = sphere.center[1];
		double z0 = sphere.center[2];
		String toReturn = "#VRML V2.0 utf8 \n";
		toReturn += "Background { skyColor 1 1 1 }\n";
		toReturn += "NavigationInfo { type \"EXAMINE\"}\n";
		
		// Shape for the original box
		toReturn += "Shape{\n";
		toReturn += "\tappearance Appearance { material Material { diffuseColor 0 0 1 }}\n";
		toReturn += "\tgeometry Box { size " + WIDTH + " " + HEIGHT + " " + DEPTH + "}\n";
		toReturn += "}\n";
		
		//Shape for the original sphere
		toReturn += "Transform {\n";
		toReturn += "\ttranslation " + x0 + " " + y0 + " " + z0 + "\n";
		toReturn += "\tchildren Shape {\n";
		toReturn += "\t\tgeometry Sphere { radius " + RADIUS + "}\n";
		toReturn += "\t\tappearance Appearance { material Material { diffuseColor 1 0 0 }}\n";
		toReturn += "\t}\n";
		toReturn += "}\n";
		
		// Subboxes
		toReturn += "Transform {\n";
		toReturn += "\ttranslation 0.0 " + "30" + "0.0\n";
		
		/*
		toReturn += "	geometry PointSet {\n";
		toReturn += "		coord Coordinate {\n";
		toReturn += " 			point [\n";	
		
		
		for (int i = 0; i < cubes.size(); i ++) {
			Cube currCube = cubes.get(i);
			toReturn += "  			" + String.valueOf(currCube.x) + " " + 
						String.valueOf(currCube.y) + " " + String.valueOf(currCube.z) 
						+ ",\n";
		}
		
		toReturn += " 			]\n";
		toReturn += "  		}\n";
		toReturn += "  		color Color {\n";
		toReturn += "  		color [\n";
		for (int i = 0; i < cubes.size(); i ++) {
			toReturn += "			1.0 1.0 1.0,\n";
		}
		toReturn += "] \n } \n} \n}";
		*/
		
		
		return toReturn;
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

	/**
	 * This is the inner class for Octree, Cube class contains information about a
	 * cube
	 * 
	 * @author Grant Zheng
	 *
	 */
	static class Cube {
		double x, y, z;
		double width, height, depth;

		/**
		 * The constructor takes two double list for information that can describe the
		 * cube
		 * 
		 * @param center    a double list with a length 3 that indicates the coordinate
		 *                  of the center location of the cube
		 * 
		 * @param xyzLimits a double list with a length 3 that indicates the width,
		 *                  height and depth. They corresponds to the x, y and z
		 *                  coordinates respectively
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

		/**
		 * This method divide the current cube into 8 smaller cubes
		 * 
		 * @return an ArrayList of Cube objects
		 */
		public ArrayList<Cube> divideTo8() {
			ArrayList<Cube> toReturn = new ArrayList<>();
			double subWidth = this.width / 4;
			double subHeight = this.height / 4;
			double subDepth = this.depth / 4;
			for (int i = 0; i < 8; i++) {
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
				double[] newCenter = { newX, newY, newZ };
				double[] xyz = { subWidth * 2, subHeight * 2, subDepth * 2 };
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
	static class Sphere {
		double center[];
		double radius;

		public Sphere(double center[], double radius) {
			this.center = center;
			this.radius = radius;
		}
	}
	
	static class AllPartialInWrapper {
		boolean allIn;
		boolean partialIn;
		
		public AllPartialInWrapper(boolean allIn, boolean partialIn) {
			this.allIn = allIn;
			this.partialIn = partialIn;
		}
	}
	
	static class AllPartialInCubesWrapper {
		ArrayList<Cube> allOut = new ArrayList<>();
		ArrayList<Cube> partialIn = new ArrayList<>();
		int level;
		
		public AllPartialInCubesWrapper(ArrayList<Cube> allOut, ArrayList<Cube> partialIn, int level) {
			this.allOut = allOut;
			this.partialIn = partialIn;
			this.level = level;
		}
	}
}
