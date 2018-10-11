import java.util.ArrayList;
/**
 * This class for STL triangles, containing coordinates for the three vertices 
 * and the normal direction for the triangles. There are also some methods necessary
 * to generate the STL file
 * @author Grant Zheng
 *
 */
public class STLTriangles {
	private double normalDir[];
	private ArrayList<Vertex> vertexArray;
	
	public STLTriangles() {
		
		this.setNormalDir(new double[3]);
		this.vertexArray = new ArrayList<>();
		for(int i = 0; i < 3; i ++) {
			this.vertexArray.add(new Vertex());
		}
			
		
	}
	
	
	
	/**
	 * returns a three element double vector for normal direction
	 * @return the normal direction double[3]
	 */
	public double[] getNormalDir() {
		return normalDir;
	}
	
	/**
	 * Setup normal direction for the triangle
	 * @param normalDir double[3]
	 */
	public void setNormalDir(double normalDir[]) {
		this.normalDir = normalDir;
	}
	
	/**
	 * generate the text required for a single triangle from "facet
	 * normal" to "endfacet"
	 * @return a string for the STL file
	 */
	public String writeToSTL() {
		this.calculateNormal();
		String toReturn = "\tfacet normal";
		for (int i = 0; i < 3; i ++) {
			toReturn += " " + this.normalDir[i];
		}
		
		toReturn += "\r\n";
		toReturn += "\t\touter loop\r\n";
		
		for (int i = 0; i < 3; i ++) {
			toReturn += "\t\t\tvertex";
			double[] current = this.vertexArray.get(i).getVertex();
			for (int j = 0; j < 3; j ++) {
				toReturn += " "  + current[j];
			}
			toReturn += "\r\n";
		}
		
		toReturn += "\t\tendloop\r\n"
				+ "\tendfacet\r\n";
		
		return toReturn;
	}
	/**
	 * Put data into the vertices of the triangle
	 * @param vertexNum the numerical order of the current index, from 1 to 3	 
	 * @param vertex the x, y and z value for the current vertex, in double[3]
	 * @throws Exception when for format of vertex is illegal or the vertexNum 
	 * exceeds 3
	 */
	public void writeVertices(int vertexNum, double[] vertex) throws Exception {
		double[] curr = new double[3];
		curr[0] = vertex[0];
		curr[1] = vertex[1];
		curr[2] = vertex[2];
		switch (vertexNum) {
		case 1:
			this.vertexArray.get(vertexNum - 1).setVertex(curr);
			break;
		case 2:
			this.vertexArray.get(vertexNum - 1).setVertex(curr);
			break;
		case 3:
			this.vertexArray.get(vertexNum - 1).setVertex(curr);
			break;
		default:
			throw new Exception("Invalid points entered");
		}
		
	}
	
	
	/**
	 * Calculate the normal direction of the current triangle and assign it to the
	 * normal direction field
	 */
	public void calculateNormal() {
		double[] v1 = this.calculateVector(this.vertexArray.get(0).getVertex(), this.vertexArray.get(1).getVertex());
		double[] v2 = this.calculateVector(this.vertexArray.get(0).getVertex(), this.vertexArray.get(2).getVertex());
		double[] temp = Computation.crossProduct(v1, v2);
		double mag = Math.sqrt(temp[0] * temp[0] +temp[1] * temp[1]
				 + temp[2] * temp[2]);
		for (int i = 0; i < 3; i ++) {
			this.normalDir[i] = temp[i] / mag;
		}
	}
	
	/**
	 * Calculates the vector pointing from v2 to v1, by subtracting v2 from v1
	 * @param v1 vector 1 double[3]
	 * @param v2 vector 2 double[3]
	 * @return vector pointing from v2 to v1 double[3]
	 */
	public double[] calculateVector(double[] v1, double[] v2) {
		double[] toReturn = new double[3];
		for (int i = 0; i < 3; i ++) {
			toReturn[i] = v1[i] - v2[i];
		}
		return toReturn;
	}
	
	
	
}
