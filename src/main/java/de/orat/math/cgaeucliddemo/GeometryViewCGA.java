package de.orat.math.cgaeucliddemo;

import de.orat.math.cga.api.CGACircleIPNS;
import de.orat.math.cga.api.CGACircleOPNS;
import de.orat.math.cga.api.CGALineIPNS;
import de.orat.math.cga.api.CGALineOPNS;
import de.orat.math.cga.api.CGAMultivector;
import de.orat.math.cga.api.CGAPlaneIPNS;
import de.orat.math.cga.api.CGAPlaneOPNS;
import de.orat.math.cga.api.CGAPointPairIPNS;
import de.orat.math.cga.api.CGAPointPairOPNS;
import de.orat.math.cga.api.CGARoundPointIPNS;
import de.orat.math.cga.api.CGARoundPointOPNS;
import de.orat.math.cga.api.CGASphereIPNS;
import de.orat.math.cga.api.CGASphereOPNS;
import de.orat.math.cga.api.iCGAFlat;
import de.orat.math.cga.api.iCGAPointPair.PointPair;
import de.orat.math.cga.api.iCGATangentOrRound;
import de.orat.math.view.euclidview3d.GeometryView3d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.lights.Light;
import org.jzy3d.plot3d.rendering.scene.Graph;

/**
 *
 * TODO
 * - Größe der Punkte etc. sinnvoll kontrollieren
 * - Visualisierung-Volumen an Punkten, Kugeln, Kreisen orientieren und start-endpunkte
 *   von Linien sowie betreffende Parameter von Ebenen entsprechend automatisch anpassen
 * 
 * Default Farben:
 * spheres and planes (points) grade 1 yellow
 * dual spheres and planes (grade 4) red
 * dual lines and dual circle and point pairs (grade 3) objekte blau
 * circle, lines and dual point pairs (grade 2) color?
 * imaginäre kreise,point-pairs, sphere: dashed
 * 
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class GeometryViewCGA extends GeometryView3d {
    
    public static Color COLOR_GRADE_1 = Color.RED;    // ipns sphere, ipns planes, ipns round points
    public static Color COLOR_GRADE_2 = Color.GREEN;  // ipns lines, ipns circle
    public static Color COLOR_GRADE_3 = Color.BLUE;   // ipns point-pairs, ipns tangent vector, ipns flat-point 
    public static Color COLOR_GRADE_4 = Color.YELLOW; // 
    
    //TODO
    // nur als Faktoren verwenden und skalieren auf Basis des angezeigten Volumens
    public static float POINT_RADIUS = 0.02f;
    public static float LINE_RADIUS = 0.02f;
    public static float TANGENT_LENGTH = 0.1f;
   
    
    public static void main(String[] args) throws Exception {
        GeometryViewCGA gv = new GeometryViewCGA();
        AnalysisLauncher.open(gv);
        
        gv.updateChessFloor(true);
        
        //GeometryView3d viewer = new GeometryView3d();
        //viewer.open();
    }
    /**
     * Add cga object into the visualization. 
     * 
     * @param values
     * @param isIPNS
     * @param label 
     */
    public void addCGAObject(double[] values, boolean isIPNS, String label){
        addCGAObject(CGAMultivector.create(values, isIPNS), label);
    }
    
    /**
     * Add cga object into the visualization.
     * 
     * @param m cga multivector
     * @param label label
     * @throws IllegalArgumentException if multivector is no visualizable type
     */
    public void addCGAObject(CGAMultivector m, String label){
        
        // cga ipns objects
        if (m instanceof CGARoundPointIPNS){
            addPoint(m.decomposeTangentOrRound(), label, true);
        } else if (m instanceof CGALineIPNS){
            addLine(m.decomposeFlat(), label, true);
        } else if (m instanceof CGAPointPairIPNS){
            addPointPair(m.decomposeTangentOrRound(), label, true);
        } else if (m instanceof CGASphereIPNS){
            addSphere(m.decomposeTangentOrRound(), label, true);
        } else if (m instanceof CGAPlaneIPNS){
            addPlane(m.decomposeFlat(), label, true);
        } else if (m instanceof CGACircleIPNS){
            addCircle(m.decomposeTangentOrRound(), label, true);
        }
        //TODO
        // oriented-point
        // flat-point
        
        // cga opns objects
        if (m instanceof CGARoundPointOPNS){
            addPoint(m.decomposeTangentOrRound(), label, false);
        } else if (m instanceof CGALineOPNS){
            addLine(m.decomposeFlat(), label, false);
        } else if (m instanceof CGAPointPairOPNS){
            addPointPair(m.decomposeTangentOrRound(), label, false);
        } else if (m instanceof CGASphereOPNS){
            addSphere(m.decomposeTangentOrRound(), label, false);
        } else if (m instanceof CGAPlaneOPNS){
            addPlane(m.decomposeFlat(), label, false);
        } else if (m instanceof CGACircleOPNS){
            addCircle(m.decomposeTangentOrRound(), label, false);
        }
        
        //TODO
        // oriented-point
        // flat-point
        
        throw new IllegalArgumentException("\""+m.toString("")+"\" has unknown type!");
    }
    
    
    // grade 1 multivectors
    
    /**
     * Add a point to the 3d view.
     * 
     * @param parameters
     * @param isIPNS 
     * @param label
     */
    public void addPoint(iCGATangentOrRound.EuclideanParameters parameters, String label, boolean isIPNS){
        Color color = COLOR_GRADE_1;
        if (!isIPNS) color = COLOR_GRADE_4;
        addPoint(parameters.location(), color, POINT_RADIUS*2, label); 
    }
    /**
     * Add a sphere to the 3d view.
     * 
     * @param parameters
     * @param label
     * @param isIPNS 
     */
    public void addSphere(iCGATangentOrRound.EuclideanParameters parameters, 
                          String label, boolean isIPNS){
        Color color = COLOR_GRADE_1;
        if (!isIPNS) color = COLOR_GRADE_4;
        addSphere(parameters.location(), parameters.squaredSize(), color, label);
    }
    /**
     * Add a plane to the 3d view.
     * 
     * @param parameters
     * @param label
     * @param isIPNS 
     */
    public void addPlane(iCGAFlat.EuclideanParameters parameters, String label, boolean isIPNS){
        Color color = COLOR_GRADE_1;
        if (!isIPNS) color = COLOR_GRADE_4;
        addPlane(parameters.location(), parameters.attitude(), color, label);
    };
    
    
    // grade 2
    
    /**
     * Add a line to the 3d view.
     * 
     * @param parameters
     * @param isIPNS
     * @param label 
     */
    public void addLine(iCGAFlat.EuclideanParameters parameters, String label, boolean isIPNS){
        Color color = COLOR_GRADE_2;
        if (!isIPNS) color = COLOR_GRADE_3;
        float length = 1;
        addLine(parameters.location(), parameters.attitude(), color, 
                LINE_RADIUS, length, label); 
    }
    /**
     * Add a circle to the 3d view.
     * 
     * @param parameters
     * @param label
     * @param isIPNS 
     */
    public void addCircle(iCGATangentOrRound.EuclideanParameters parameters, 
                          String label, boolean isIPNS){
        Color color = COLOR_GRADE_2;
        if (!isIPNS) color = COLOR_GRADE_3;
        boolean isImaginary = false;
        if (parameters.squaredSize() <0) isImaginary = true;
        addCircle(parameters.location(), parameters.attitude(), 
                (float) parameters.squaredSize(), color, label, isImaginary);
    }
    
    
    // grade 3
    
    /**
     * Add a point-pair to the 3d view.
     * 
     * @param parameters
     * @param label
     * @param isIPNS 
     */
    public void addPointPair(iCGATangentOrRound.EuclideanParameters parameters, 
                             String label, boolean isIPNS){
        Color color = COLOR_GRADE_3;
        if (!isIPNS) color = COLOR_GRADE_2;
        boolean isImaginary = false;
        if (parameters.squaredSize()<0) isImaginary = true;
        //TODO
        float radius = 1;
        float width =1;
        Point3d[] points = decomposePointPair(parameters);
        addPointPair(points[0], points[1], label, color, radius, width, isImaginary);
    };
    /**
     * Add a tangent to the 3d view.
     * 
     * @param parameters
     * @param label 
     * @param isIPNS 
     */
    public void addTangentVector(iCGATangentOrRound.EuclideanParameters parameters, 
                                 String label, boolean isIPNS){
        Color color = COLOR_GRADE_3;
        if (!isIPNS) color = COLOR_GRADE_2;
        addArrow(parameters.location(), parameters.attitude(), 
                TANGENT_LENGTH, LINE_RADIUS, color, label);
    }
   
  
    // helper methods
    
    private static double signedRadius(double squaredRadius){
        double r = Math.sqrt(Math.abs(squaredRadius));
        if (squaredRadius < 0) r = -r;
        return r;
    }
    private static Point3d[] decomposePointPair(iCGATangentOrRound.EuclideanParameters parameters){
        Point3d c = parameters.location();
        double r = Math.sqrt(Math.abs(parameters.squaredSize())); 
        Vector3d v = parameters.attitude();
        v.normalize();
        v.scale(r/2);
        Point3d[] result = new Point3d[2];
        result[0] = new Point3d(c);
        result[0].add(v);
        result[1] = new Point3d();
        result[1].sub(v);
        return result;
    }
    
    
    
    @Override
    public void init() throws Exception {
        
        Quality q = Quality.Advanced(); 
        q.setDepthActivated(true);
        //q.setAlphaActivated(false);
        q.setAnimated(false); 
        q.setHiDPIEnabled(true); 
        q.setDisableDepthBufferWhenAlpha(false);
        q.setPreserveViewportSize(true);        
        //chart = initializeChart(q);       
        
        chart = new Chart(this.getFactory(), q);
        
        //chart = myfactory.newChart(q);
        chart.getView().setSquared(false);
        chart.getView().setBackgroundColor(Color.WHITE);
        chart.getView().getAxis().getLayout().setMainColor(Color.BLACK);
        //Add the ChessFloor and set size
        
        
        
        this.setUpChessFloor(100.f);
        chart.getScene().getGraph().addGraphListener(new Graph.GraphListener() {
            @Override
            public void onMountAll() {
                updateChessFloor(true);
            }
        });
        
       
        setUpMouse();
        //Light light = chart.addLight(chart.getView().getBounds().getCorners().getXmaxYmaxZmax());
        //light.setType(Light.Type.POSITIONAL);
        Light light = chart.addLightOnCamera();
        
        //addSkeleton("data/golembones/golembones.obj");
        
        /**
        addPoint(new Point3d(1,1,1), Color.BLUE, 0.6f, "Point1");
        addSphere(new Point3d(20,20,20), 10, Color.ORANGE, "Sphere1");
        
        addPlane(new Point3d(5d,5d,5d), new Vector3d(0d,0d,5d), new Vector3d(5d,0d,0d), Color.RED, "Plane1");
        
        addArrow(new Point3d(0d, 0d, 0d), new Vector3d(0d,0d,2d), 3f, 0.5f, Color.CYAN, "Arrow1");
        
        addLabel(new Point3d(10d, 10d, 10d), "Label", Color.BLACK);
        addCircle(new Point3d(20,20,20), new Vector3d(0,0,1),10,Color.RED, "Circle");
        
        addLine(new Vector3d(0d,0d,-1d), new Point3d(3d,0d,3d), Color.CYAN, 0.2f, 10, "ClipLinie");
        
        addPlane(new Point3d(0,1,5), new Vector3d(0,-10,0), new Vector3d(-10,0,0), Color.ORANGE, "ClipPlane");
        addPoint(new Point3d(0,0,0), Color.BLUE, 0.6f, "Point1");
        addPoint(new Point3d(1,10,1), Color.BLUE, 0.6f, "Point3");
        addPoint(new Point3d(20,20,20), Color.BLUE, 0.6f, "Point2");    
        addPlane(new Point3d(5d,5d,5d), new Vector3d(0d,0d,5d), new Vector3d(5d,0d,0d), Color.RED, "Plane1");
        addLine(new Vector3d(0d,0d,-1d), new Point3d(3d,0d,3d), Color.CYAN, 0.2f, 10, "ClipLinie");
        addArrow(new Point3d(7d, 7d, 7d), new Vector3d(0d,0d,2d), 3f, 0.5f, Color.CYAN, "Arrow1");
        **/
        
        /*
        double[] delta_theta_rad = new double[]{0d,0d,0d,0d,0d,0d,0d};
        //double[] delta_theta_rad = new double[]{0d, -8.27430119976213518e-08, 0.732984551101984239, 5.46919521494736127, 0.0810043775014757245, -3.53724730506321805e-07, -9.97447025669062626e-08};
        double[] delta_a_m = new double[]{0d, 0, -425, -392.2, 0, 0, 0};
        double[] delta_d_m = new double[]{0d, 162.5, 0, 0, 133.3, 997, 996};
        double[] delta_alpha_rad= new double[]{0d, Math.PI/2, 0, 0, Math.PI/2, Math.PI/2, 0};
        */
        
    }
    
}
