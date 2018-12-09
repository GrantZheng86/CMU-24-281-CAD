import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class voxel {
	
	/*
	 *                 24786  GEOMETRIC MODELING
	 *                 Carnegie Mellon University
	 *                          VOXEL
	 */
	
	/*
	 * constants
	 */
	
	static double BOXPOSX = -10.0;       /* x position of voxel box          */
	static double BOXPOSY = -10.0;       /* y position of voxel box          */
	static double BOXPOSZ = -10.0;       /* z position of voxel box          */
	static double BOXSIZE = 20.0;        /* size of the voxel box            */
	static int    NDIV    = 16;          /* number of division in x,y,z axes */
	
	static double WIDTH   = 18.0;        /* width of the cube (x direction)  */
	static double HEIGHT  = 10.0;        /* height of the cube (y direction) */
	static double DEPTH   = 12.0;        /* depth of the cube (z direction)  */
	
	static double CUBX1   = -9.0;        /* min-x face of the cube           */
	static double CUBX2   =  9.0;        /* max-x face of the cube           */
	static double CUBY1   = -5.0;        /* min-y face of the cube           */
	static double CUBY2   =  5.0;        /* max-y face of the cube           */
	static double CUBZ1   = -6.0;        /* min-z face of the cube           */
	static double CUBZ2   =  6.0;        /* max-z face of the cube           */
	
	static double RADIUS  =  8.0;        /* radius of the sphere             */
	static double RADIUS2 = 64.0;        /* radius of the sphere **2         */
	
	public static void main(String[] args) throws Exception{
	  int       i, j, k;                   /* voxel index in x,y,z          */
	  double    x0, y0, z0;                /* sphere center position        */
	  double    x, y, z;                   /* current cell position         */
	  double    cellsize = BOXSIZE/NDIV;   /* current cell size             */
	  
	  
	  /*
	   * Create a buffered reader to read the user input from the console
	   * User will enter three doubles to specify the sphere's center
	   */
	  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	  
	  System.out.println("Input coordinates (e.g. 2 0 0):");
	  StringTokenizer st = new StringTokenizer(br.readLine());
	  
	  x0 = Double.parseDouble(st.nextToken());
	  y0 = Double.parseDouble(st.nextToken());
	  z0 = Double.parseDouble(st.nextToken());
	
	  /* open the target VRML file, "voxel.wrl" */
	  PrintStream output = new PrintStream(new FileOutputStream(new File("voxel.wrl")));
	
	  output.print("#VRML V2.0 utf8\n\n");
	  /* draw the cube and the sphere */
	  output.print("Background { skyColor 1 1 1 }\n");
	  output.print("NavigationInfo { type \"EXAMINE\"}\n");
	
	  output.print("Shape {\n");
	  output.print("\tappearance Appearance { material Material { diffuseColor 0 0 1 }}\n");
	  output.print("\tgeometry Box { size " + WIDTH + " " + HEIGHT + " " + DEPTH + "}\n");
	  output.print("}\n");
	
	  output.print("Transform {\n");
	  output.print("\ttranslation " + x0 + " " + y0 + " " + z0 + "\n");
	  output.print("\tchildren Shape {\n");
	  output.print("\t\tgeometry Sphere { radius " + RADIUS + "}\n");
	  output.print("\t\tappearance Appearance { material Material { diffuseColor 1 0 0 }}\n");
	  output.print("\t}\n");
	  output.print("}\n");
	
	  output.print("Transform {\n");
	  output.print("\ttranslation 0.0 " + 1.5*BOXSIZE + "0.0\n");
	  output.print("\tchildren [\n");
	
	  
	  /*
	   *  visit each voxel and draw a cube if the voxel center is inside
	   *  the solid defined as: Solid = Cube -* Sphere.
	   */
	  for(k=0; k<NDIV; k++){
	    for(j=0; j<NDIV; j++){
	      for(i=0; i<NDIV; i++){
	        x = BOXPOSX + i*cellsize;
	        y = BOXPOSY + j*cellsize;
	        z = BOXPOSZ + k*cellsize;
	        if ( in_cube(x,y,z) == 1 && in_sphere(x,y,z,x0,y0,z0) == 0 ) {
			  output.print("\tTransform {\n");
			  output.print("\t\ttranslation " + x + " " + y + " " + z + "\n");
			  output.print("\t\tchildren Shape {\n");
			  output.print("\t\t\tappearance Appearance { material Material { diffuseColor 0 0 1 }}\n");
			  output.print("\t\t\tgeometry Box {size " + 0.9*cellsize + " " + 0.9*cellsize + " " + 0.9*cellsize + "}\n");
	          output.print("\t\t}\n");
	          output.print("\t},\n");
	        }
	      }
	    }
	  }
	
	  output.print("\t]\n");  // close children
	  output.print("}\n");      // close Transform
	
	
	  /* close the target VRML file "output.wrl" */
	  output.close();
	}
	
	
	/*
	 * Check if the point is inside the cube. Return 1 if inside, 0 if outside.
	 */
	public static int in_cube(double x, double y, double z){
	  if (x < CUBX1 || CUBX2 < x || y < CUBY1 || CUBY2 < y
	                || z < CUBZ1 || CUBZ2 < z)
	     return 0;
	  else return 1;
	}
	
	/*
	 * Check if the point is inside the sphere. Return 1 if inside, 0 if outside.
	 */
	public static int in_sphere(double x, double y, double z, double x0, double y0, double z0){
	  if ( ((x-x0)*(x-x0) + (y-y0)*(y-y0) + (z-z0)*(z-z0) ) < RADIUS2 )
	     return 1;
	  else return 0;
	}
	
	
	
	class Cube{
		double x, y, z;
		double width, height, depth;
		public Cube(double center[], double[] xyzLimits) {
			this.x = center[0];
			this.y = center[1];
			this.z = center[2];
			this.width = xyzLimits[0];
			this.height = xyzLimits[1];
			this.depth = xyzLimits[2];
		}
	}
	
	class Sphere{
		double center[];
		double radius;
		public Sphere(double center[], double radius ) {
			this.center = center;
			this.radius = radius;
		}
	}
}
	
	
	
	
	
	
	
