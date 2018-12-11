import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public class Octree {

	// Some properties of the box
	static final double WIDTH = 18;
	static final double HEIGHT = 10;
	static final double DEPTH = 12;
	static final double[] boxCenter = { 0, 0, 0 };
	static final double[] boxDimension = { WIDTH, HEIGHT, DEPTH };
	static final int STOP_LEVEL = 5;
	static final int THREAD_COUNT = 5;
	// Some properties of the sphere
	static final double RADIUS = 8;

	/**
	 * The main function that handles user input and output
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Input coordinates");

		StringTokenizer st = new StringTokenizer(reader.readLine());
		double x0, y0, z0;
		x0 = Double.parseDouble(st.nextToken());
		y0 = Double.parseDouble(st.nextToken());
		z0 = Double.parseDouble(st.nextToken());
		double[] sphereCenter = { x0, y0, z0 };
		Cube mainCube = new Octree.Cube(boxCenter, boxDimension);
		Sphere mainSphere = new Sphere(sphereCenter, RADIUS);
		ArrayList<ColoredCube> leftCubes = traverseTree(mainSphere, mainCube, 0);

		PrintStream output = new PrintStream(new FileOutputStream(new File("Octree.wrl")));
		output.print(generateVRML(leftCubes, mainSphere));
		output.close();
		System.out.println(leftCubes.size());

	}

	/**
	 * This methods will traverse tree using a recursion method, usually user will start to call
	 * this method at level = 0
	 * @param sphere	a sphere object
	 * @param cube		a cube object
	 * @param level		an int indicates level number, usually starts at 0
	 * @return	an ArrayList of ColoredCubes for the VRML files
	 */
	public static ArrayList<ColoredCube> traverseTree(Sphere sphere, Cube cube, int level) {
		ArrayList<ColoredCube> cCubes = new ArrayList<>();
		int cubeSphere = locationStatus(cube, sphere);
		
		// Recursion stop condition, when executing the STOP_LEVEL, colors are assigned accroding 
		// to level information
		if (level == STOP_LEVEL) {
			if (cubeSphere == 1) {
				cCubes.add(new ColoredCube(-1, cube)); // -1 indicates that it is the last level half intersection cube
			} else if (cubeSphere == 2) {				// 2 indicates that the cube is totally outside the sphere
				cCubes.add(new ColoredCube(level, cube));
			}
			return cCubes;
			
		} else if (cubeSphere == 2) {
			cCubes.add(new ColoredCube(level, cube));
			return cCubes;
		}

		// Divide the cube into 8 smaller cubes
		ArrayList<Cube> subCubes = cube.divideTo8();
		for (int i = 0; i < 8; i++) {
			ArrayList<ColoredCube> curr = traverseTree(sphere, subCubes.get(i), level + 1);
			cCubes.addAll(curr);
		}

		return cCubes;
	}

	/**
	 * This class is a representation for a cube with an integer for color
	 * @author Grant Zheng
	 *
	 */
	static class ColoredCube {
		int color;
		Cube cube;

		public ColoredCube(int color, Cube cube) {
			this.color = color;
			this.cube = cube;
		}
	}
	
	/**
	 * This method return the location relationship between the sphere and the box
	 * @param cube	a cube object
	 * @param sphere	a sphere object
	 * @return	an integer, 0 indicates that the cube is completely in, 1 indicates that
	 * the box is partially in the sphere, 2 indicates that the box is competely outside
	 */
	
	
	public static int locationStatus(Cube cube, Sphere sphere) {
		ArrayList<double[]> cornerList = cube.calculateCorners();
		// Check for center relationships
		double[] cubeCenter = { cube.x, cube.y, cube.z };
		double[] sphereCenter = sphere.center;
		double widthL = cubeCenter[0] - cube.width / 2;
		double widthU = cubeCenter[0] + cube.width / 2;
		double heightL = cubeCenter[1] - cube.height / 2;
		double heightU = cubeCenter[1] + cube.height / 2;
		double depthL = cubeCenter[2] - cube.depth / 2;
		double depthU = cubeCenter[2] + cube.depth / 2;

		boolean sphereInBox = false;
		if (sphereCenter[0] > widthL && sphereCenter[0] < widthU && sphereCenter[1] > heightL
				&& sphereCenter[1] < heightU && sphereCenter[2] > depthL && sphereCenter[2] < depthU) {
			sphereInBox = true;
		}

		// Check for corner relationships
		int cornersIn = 0;
		for (int i = 0; i < 8; i++) {
			double[] currCorner = cornerList.get(i);
			if (calculateDist(currCorner, sphere.center) <= sphere.radius)
				cornersIn += 1;
		}

		if (sphereInBox) {
			if (cornersIn == 8)
				return 0; // Fully in
			return 1; // Partially In

		} else {
			if (cornersIn == 8)
				return 0; // Fully in
			else if (cornersIn == 0)
				return 2; // Fully out
			return 1; // Partially In
		}
	}

	/**
	 * Generates WRL file for rendering
	 * @param cubes	Cubes that are outside of the sphere
	 * @param sphere	The sphere
	 * @return a String containing for the VRML file
	 */
	public static String generateVRML(ArrayList<ColoredCube> cubes, Sphere sphere) {
		double x0 = sphere.center[0];
		double y0 = sphere.center[1];
		double z0 = sphere.center[2];
		StringBuilder returnBuilder = new StringBuilder();
		returnBuilder.append("#VRML V2.0 utf8 \n");
		returnBuilder.append("Background { skyColor 1 1 1 }\n");
		returnBuilder.append("NavigationInfo { type \"EXAMINE\"}\n");

		// This is the box
		returnBuilder.append("Shape {\n");
		returnBuilder.append("\tappearance Appearance { material Material { diffuseColor 0 0 1 }}\n");
		returnBuilder.append("\tgeometry Box { size " + WIDTH + " " + HEIGHT + " " + DEPTH + "}\n");
		returnBuilder.append("}\n");

		// This is the sphere
		returnBuilder.append("Transform {\n");
		returnBuilder.append("\ttranslation " + x0 + " " + y0 + " " + z0 + "\n");
		returnBuilder.append("\tchildren Shape {\n");
		returnBuilder.append("\t\tgeometry Sphere { radius " + RADIUS + "}\n");
		returnBuilder.append("\t\tappearance Appearance { material Material { diffuseColor 1 0 0 }}\n");
		returnBuilder.append("\t}\n");
		returnBuilder.append("}\n");

		// Translate 30 to draw the octree boxes
		returnBuilder.append("Transform {\n");
		returnBuilder.append("\ttranslation 0.0 30.0 0.0\n");
		returnBuilder.append("\tchildren [\n");
		
		ArrayList<double[]> colorList = new ArrayList<>();
		Random rand = new Random();
		for (int i = 0; i < STOP_LEVEL + 1; i ++) {
			double r = rand.nextDouble();
			double g = rand.nextDouble();
			double b = rand.nextDouble();
			double[] toAdd = {r, g, b};
			colorList.add(toAdd);
		}

		// Draw the cubes
		for (int i = 0; i < cubes.size(); i++) {
			ColoredCube temp = cubes.get(i);
			int levelColor = temp.color;
			Cube currCube = temp.cube;
			double[] thisColor;
			double curWidth, curHeight, curDepth;
			if (levelColor == -1.0) {
				curWidth = WIDTH / Math.pow(2, STOP_LEVEL);
				curHeight = HEIGHT / Math.pow(2, STOP_LEVEL);
				curDepth = DEPTH / Math.pow(2, STOP_LEVEL);
				thisColor = new double[] {0, 0, 0};
			} else {
				curWidth = WIDTH / Math.pow(2, levelColor);
				curHeight = HEIGHT / Math.pow(2, levelColor);
				curDepth = DEPTH / Math.pow(2, levelColor);
				thisColor = colorList.get(levelColor);
			}
			// String curSize = "size" + curWidth + " " + curHeight + " " + curDepth;
			String color = "diffuseColor ";
			
			color += thisColor[0] + " " + thisColor[1] + " " + thisColor[2];
			returnBuilder.append("\tTransform {\n");
			returnBuilder.append("\t\ttranslation " + currCube.x + " " + currCube.y + " " + currCube.z + "\n");
			returnBuilder.append("\t\tchildren Shape {\n");
			returnBuilder.append("\t\t\tappearance Appearance { material Material {" + color + " }}\n");
			returnBuilder.append("\t\t\tgeometry Box {size " + 0.9 * curWidth + " " + 0.9 * curHeight + " " + 0.9 * curDepth
					+ "}\n");
			returnBuilder.append("\t\t}\n");
			returnBuilder.append("\t},\n");
		}

		returnBuilder.append("\t]\n");
		returnBuilder.append("}\n");
		return returnBuilder.toString();
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
	 * A Sphere object
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

}
