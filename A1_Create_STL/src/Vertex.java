/**
 * The vertex class, containing the coordinates of the vertex
 * @author Grant Zheng
 *
 */
public class Vertex {
	private double[] vert;
	
	public Vertex() {
		this.setVertex(new double[3]);
	}

	public double[] getVertex() {
		return vert;
	}

	public void setVertex(double[] vert) {
		this.vert = vert;
	}

	

	
}
