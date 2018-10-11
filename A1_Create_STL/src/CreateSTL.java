import java.io.PrintWriter;
import java.util.ArrayList;

public class CreateSTL {

	public static void main(String[] args) throws Exception {
		
		ArrayList<Double> x = new ArrayList<>();
		ArrayList<Double> y = new ArrayList<>();
		String toSTLFile = new String();
		int segmentation =50;
		double increment = Math.PI / segmentation;
		
		
		for (int i = 0; i <= segmentation; i ++) {
			x.add(i * increment);
			y.add(i * increment);
		}
		
		// Calculate vertices for triangles
		for (int i = 0; i < segmentation; i ++) {
			for (int j = 0; j < segmentation; j ++) {
				double[] currentXY = new double[] {x.get(j),y.get(i)};
				double[] rightXY = new double[]{x.get(j + 1), y.get(i)};
				double[] upperXY = new double[] {x.get(j + 1), y.get(i + 1)};
				double[] vertex1 = new double[] {currentXY[0], currentXY[1], Computation.calculateZ(currentXY)};
				double[] vertex2 = new double[] {rightXY[0], rightXY[1], Computation.calculateZ(rightXY)};
				double[] vertex3 = new double[] {upperXY[0], upperXY[1], Computation.calculateZ(upperXY)};
				STLTriangles temp = new STLTriangles();
				temp.writeVertices(1, vertex1);
				temp.writeVertices(2, vertex2);
				temp.writeVertices(3, vertex3);
				temp.calculateNormal();
				toSTLFile += temp.writeToSTL();
				rightXY = upperXY;
				upperXY = new double[] {x.get(j), y.get(i + 1)};
				vertex2 = new double[] {rightXY[0], rightXY[1], Computation.calculateZ(rightXY)};
				vertex3 = new double[] {upperXY[0], upperXY[1], Computation.calculateZ(upperXY)};
				temp.writeVertices(1, vertex1);
				temp.writeVertices(2, vertex2);
				temp.writeVertices(3, vertex3);
				temp.calculateNormal();
				toSTLFile += temp.writeToSTL();
			}
			
		}
		
		// Write a STL file
		PrintWriter writer = new PrintWriter("surface.stl");
		writer.println("solid test");
		writer.print(toSTLFile);
		writer.println("endsolid test");
		writer.close();
		System.out.println("Finished creating STL file");
	}

	
}
