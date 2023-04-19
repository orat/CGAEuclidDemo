package de.orat.math.cgaeucliddemo;

import de.orat.math.cga.api.CGACircleIPNS;
import de.orat.math.cga.api.CGACircleOPNS;
import de.orat.math.cga.api.CGALineIPNS;
import de.orat.math.cga.api.CGALineOPNS;
import de.orat.math.cga.api.CGAMultivector;
import de.orat.math.cga.api.CGAOrientedPointIPNS;
import de.orat.math.cga.api.CGAOrientedPointOPNS;
import de.orat.math.cga.api.CGAPlaneIPNS;
import de.orat.math.cga.api.CGAPlaneOPNS;
import de.orat.math.cga.api.CGAPointPairIPNS;
import de.orat.math.cga.api.CGAPointPairOPNS;
import de.orat.math.cga.api.CGARoundPointIPNS;
import de.orat.math.cga.api.CGARoundPointOPNS;
import de.orat.math.cga.api.CGASphereIPNS;
import de.orat.math.cga.api.CGASphereOPNS;
import de.orat.math.cga.api.iCGAFlat;
import de.orat.math.cga.api.iCGAPointPair;
import de.orat.math.cga.api.iCGATangentOrRound;
import de.orat.math.cga.api.iCGATangentOrRound.EuclideanParameters;
import de.orat.math.view.euclidview3d.GeometryView3d;
import de.orat.math.view.euclidview3d.ObjectLoader;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.primitives.EuclidRobot;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 *
 * TODO
 * - Größe der Punkte etc. sinnvoll kontrollieren
 * - Visualisierung-Volumen an Punkten, Kugeln, Kreisen orientieren und start-endpunkte
 *   von Linien sowie betreffende Parameter von Ebenen entsprechend automatisch anpassen
 * 
 * TODO
 * imaginäre circles, point-pairs, spheres: dashed
 * 
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class GeometryViewCGA extends GeometryView3d {
    
    public static Color COLOR_GRADE_1 = Color.RED;    // ipns: sphere, planes, round points
    public static Color COLOR_GRADE_2 = Color.GREEN;  // ipns: lines, ipns circle, oriented points; opns: flat-points, point-pairs
    public static Color COLOR_GRADE_3 = Color.BLUE;   // ipns: point-pairs, (tangent vector), flat-points; opns:circle, line, oriented-points
    public static Color COLOR_GRADE_4 = Color.YELLOW; // (ipns euclidean vector), opns: plane, round-point, spheres
    
    //TODO
    // nur als Faktoren verwenden und skalieren auf Basis des angezeigten Volumens
    public static float POINT_RADIUS = 0.01f; // in m
    public static float LINE_RADIUS = 0.005f; // in m
    public static float TANGENT_LENGTH = 0.1f;
   
    public static void main(String[] args) throws Exception {
        GeometryViewCGA gv = new GeometryViewCGA();
        AnalysisLauncher.open(gv);
        
        //Robots have to be rotated after initialisation.
        rotateRobotsCoordsystem();
        setRobotsDH();
        
        
        // gv.setUpSkeletons(); 
        EuclidRobot robot = robotList.get(0); // letzter Eintrag aus der Tabelle 
        /*robot.setTheta(1, 0,true); 
        robot.setTheta(2, 0,true); 
        robot.setTheta(3, 0,true); 
        robot.setTheta(4, 0,true); 
        robot.setTheta(5, 0,true); 
        robot.setTheta(6, 0,true);*/
        
        robot.setTheta(1, -42.29444839527154f,true); 
        robot.setTheta(2, -83.92752302017205f,true); 
        robot.setTheta(3, -95.53404922699703f,true); 
        robot.setTheta(4, -90.56670900909727f,true); 
        robot.setTheta(5, 90.21354316792402f,true); 
        robot.setTheta(6, -132.37747769068068f,true);
        
        
        gv.addGeometricObjects();
        
        gv.updateChessFloor(true, CHESS_FLOOR_WIDTH);
    }
    
    void addGeometricObjects(){
        
        // test points visualisation
        //Point3d center = new Point3d(1,2,3);
        //CGARoundPointIPNS p = new CGARoundPointIPNS(center);
        //gv.addCGAObject(p, "p");
        
        // TCP
        // point in ipns representation, gaalop ordered values, Punkt Endeffektor
        double[] values = new double[]{0, 0.24011911, -0.39999987, -0.49, 0.22887854, 1,
                                       0,0,0,0,0,0,
                                       0,0,0,0,0,0,
                                       0,0,0,0,0,0,
                                       0,0,0,0,0,0,
                                       0,0};
        CGARoundPointIPNS tcp = new CGARoundPointIPNS(CGAMultivector.fromGaalop(values));
        System.out.println(tcp.toString("tcp"));
        addCGAObject(tcp, "tcp");
        
        // Punkt P5 (letztes Gelenk)
        values = new double[]{
                    0,          0.24011911, -0.39999987, -0.3904,      0.18503462,  1,
                    0,          0,          0,          0,          0,          0,
                    0,          0,          0,          0,          0,          0,
                    0,          0,          0,          0,          0,          0,
                    0,          0,          0,          0,          0,          0,
                    0,          0    
        };
        CGARoundPointIPNS p5 = new CGARoundPointIPNS(CGAMultivector.fromGaalop(values));
        System.out.println(p5.toString("p5"));
        addCGAObject(p5, "p5");
        
        
        // spheres 
        
        // test sphere um P5 visualisation
        values = new double[]{0, 0.24011911, -0.39999987, -0.3904, 0.17615018,  1,
                                0,         0,         0,         0,         0,         0,
                                0,         0,         0,         0,         0,         0,
                                0,         0,         0,         0,         0,         0,
                                0,         0,         0 ,        0,         0,         0,
                                0,         0,       };
        CGASphereIPNS sp5 = new CGASphereIPNS(CGAMultivector.fromGaalop(values));
        System.out.println(sp5.toString("sp5"));
        //addCGAObject(sp5, "sp5");
        
        
        // Kugel um den Ursprung
        /*values = new double[]{0, 0, 0, 0, -0.17615018,  1,
                                0,         0,         0,         0,         0,         0,
                                0,         0,         0,         0,         0,         0,
                                0,         0,         0,         0,         0,         0,
                                0,         0,         0 ,        0,         0,         0,
                                0,         0,       };
        CGASphereIPNS so = new CGASphereIPNS(CGAMultivector.fromGaalop(values));
        System.out.println(so.toString("so"));
        addCGAObject(so, "so");
        */
        
        // circle visualisation 
        // FIXME scheint gekippt im Raum zu stehen
        values = new double[]{0,          0,         0,       0,         0,         0,
                    0,          0,         -0.04229702,  0.24011911,  0, 0.07046005,
                   -0.39999987,  0.06876903, -0.3904,     0.35230035,  0,          0,
                    0,          0,          0,          0,          0,          0,
                    0,          0,          0,          0,          0,          0,
                    0,          0       };
        CGACircleIPNS c = new CGACircleIPNS(CGAMultivector.fromGaalop(values));
        System.out.println(c.toString("c"));
        addCGAObject(c, "c");
        
        // testweise circle parameter ausgeben
        EuclideanParameters parameters = c.decompose();
        System.out.println("radius*radius= "+String.valueOf(parameters.squaredSize()));
        System.out.println("radius= "+String.valueOf(Math.sqrt(Math.abs(parameters.squaredSize()))));
        Vector3d n = parameters.attitude();
        System.out.println("normal= "+String.valueOf(n.x)+", "+String.valueOf(n.y)+", "+String.valueOf(n.z));
        Point3d loc = parameters.location();
        System.out.println("location= "+String.valueOf(loc.x)+", "+String.valueOf(loc.y)+", "+String.valueOf(loc.z));
        
        
        // circle ok
        Vector3d normal = new Vector3d(0,0,1);
        float radius = 2;
        CGACircleIPNS cc = new CGACircleIPNS(loc, normal, radius);
        addCGAObject(cc,"cc");
        //addCircle(loc, normal, radius, Color.BLUE, "test circle", false);
        
        // point-pair 
        
        // funktioniert
        Point3d pp1 = new Point3d(0.1,0.2,0.2);
        Point3d pp2 = new Point3d(0.1,0.2,-0.2);
        //CGAPointPairIPNS pp = (new CGAPointPairOPNS(pp1,pp2)).dual(); 
        //addCGAObject(pp,"pp");
        //addPointPair(pp.decomposePoints(), "pp", true, false);
        
        CGAPointPairOPNS pp = (new CGAPointPairOPNS(pp1,pp2)); 
        addCGAObject(pp,"pp");
        
        //FIXME decompose schlägt fehl
        /*
         * [0.          0.          0.          0.          0.          0.
            0.          0.          0.          0.          0.          0.
            0.          0.          0.          0.          0.          0.
            0.         -0.04229702  0.24011911 -0.0937425   0.07046005 -0.39999987
            0.15615995 -0.19988819  0.          0.          0.          0.
            0.          0.        ]
         */
        values = new double[]{  0,          0,          0,          0,          0,          0,
                                0,          0,          0,          0,          0,          0,
                                0,          0,          0,          0,          0,          0,
                                0,         -0.04229702,  0.24011911, -0.0937425,   0.07046005, -0.39999987,
                                0.15615995, -0.19988819,  0,          0,          0,          0,
                                0,          0        };
        CGAPointPairIPNS PP = new CGAPointPairIPNS(CGAMultivector.fromGaalop(values));
        System.out.println(PP.toString("PP"));
        // PP = (0.24011911*eo^e1^e3 + 0.39999987*eo^e2^e3 - 0.0937425*eo^e1^ei + 0.15615995*eo^e2^ei - 0.19988819*eo^e3^ei - 0.04229702*e1^e3^ei + 0.07046005*e2^e3^ei)
        // das ist also ein korrekter ipns point-pair
        // The given multivector is no k-vector: 1.0000000000000002*eo + NaN*e1 + NaN*e2 + 0.18356670723359603*e3 - 0.3445510478954948*e1^e2^e3 - 0.082826103312249*ei + 0.15546292605571413*e1^e2^ei
        // weightIntern2 (squaredSizeIntern2, CGAOrientedPointPairIPNS)=0.4665373328976028
        // locationIntern2 (CGAOrientedPointPairIPNS) = (0.22051648227377704*e1 + 0.3673450407273629*e2 + 0.18356670723359603*e3 + 0.3445510478954946*e1^e2^e3)
        // The given multivector is not not grade 1 or 0! grade()=-1 0.22051648227377704*e1 + 0.3673450407273629*e2 + 0.18356670723359603*e3 + 0.3445510478954946*e1^e2^e3
        // double r2 = PP.squaredSizeIntern5().value();
        //System.out.println("squaredSize(PP)="+String.valueOf(r2));
        double r2 = PP.squaredSizeIntern3().value(); // <0 funktioniert
        System.out.println("squaredSize(PP)3="+String.valueOf(r2));
        //r2 = PP.squaredSizeIntern2().value(); // schlägt fehl
        //System.out.println("squaredSize(PP)2="+String.valueOf(r2));
        r2 = PP.squaredSizeIntern1().value();
        System.out.println("squaredSize(PP)1="+String.valueOf(r2));
        r2 = PP.squaredSize();
        System.out.println("squaredSize(PP)="+String.valueOf(r2));
        //addCGAObject(PP, "PPTest");
       
        
        // Points
        
        // Punkt P_c:
        values = new double[]{ 0.00000000e+00, -3.30040856e-01,  3.01597789e-01,  3.90400000e-01,
            -1.76150176e-01, -1.00000000e+00,  0.00000000e+00,  0.00000000e+00,
             0.00000000e+00,  0.00000000e+00,  0.00000000e+00,  0.00000000e+00,
             0.00000000e+00,  0.00000000e+00,  0.00000000e+00,  0.00000000e+00,
             0.00000000e+00,  2.77555756e-17,  0.00000000e+00,  0.00000000e+00,
             0.00000000e+00,  0.00000000e+00,  0.00000000e+00,  0.00000000e+00,
             0.00000000e+00,  0.00000000e+00,  0.00000000e+00,  0.00000000e+00,
             0.00000000e+00,  0.00000000e+00,  0.00000000e+00,  0.00000000e+00};
        CGARoundPointIPNS P_c = new CGARoundPointIPNS(CGAMultivector.fromGaalop(values));
        System.out.println(P_c.toString("P_c"));
        addCGAObject(P_c, "P_c");
        
        
        // plane
        
        //Ebene PI_c:
        values = new double[]{0,         -0.30159779, -0.33004086,  0,          0,          0,
                                0,          0,          0,          0,          0,          0,
                                0,          0,          0,          0,          0,          0,
                                0,          0,          0,          0,          0,          0,
                                0,          0,          0,          0,          0,          0,
                                0,          0 };  
        CGAPlaneIPNS PI_c = new CGAPlaneIPNS(CGAMultivector.fromGaalop(values));
        System.out.println(PI_c.toString("PI_c"));
        addCGAObject(PI_c, "PI_c");
        
        // test Ebene selbst erzeugt
        // yz-Ebene
        // funktioniert
        //CGAPlaneIPNS pl = new CGAPlaneIPNS(new Vector3d(1,0,0),0d);
        //System.out.println(pl.toString("pl"));
        //addCGAObject(pl, "pl");
        
        // test opns plane funktioniert
        /*CGARoundPointIPNS p1 = new CGARoundPointIPNS(new Point3d(0,0,0));
        CGARoundPointIPNS p2 = new CGARoundPointIPNS(new Point3d(0,0,10));
        CGARoundPointIPNS p3 = new CGARoundPointIPNS(new Point3d(5,10,10));
        CGAPlaneOPNS pl = new CGAPlaneOPNS(p1,p2,p3);
        System.out.println(pl.toString("pl"));
        addCGAObject(pl, "pl");*/
        
        // opns aus gaalop sollte noch getestet werden
        /*values = new double[]{};  
        CGAPlaneOPNS PI_c2 = new CGAPlaneOPNS(CGAMultivector.fromGaalop(values));
        System.out.println(PI_c2.toString("PI_c2"));
        addCGAObject(PI_c2, "PI_c2");*/
        
        
        // funktioniert
        //boolean res = addPlaneDeprecated(new Point3d(0,0,0), new Vector3d(0,0,500), new Vector3d(0,500,500),
        //                  Color.BLUE, "plane");
        
        //boolean res = addPlaneDeprecated(new Point3d(0,0,-300), new Vector3d(0,0,1), Color.BLUE, "test_plane");
        
        // test line
        //addLine(new Vector3d(0d,0d,-1d), new Point3d(3d,0d,3d), Color.CYAN, 0.2f, 10, "ClipLinie");
        
        /*Point3d start = new Point3d(300,-300,-300);
        Vector3d v = new Vector3d(100,100,100);
        CGALineIPNS line = new CGALineIPNS(start, v);
        String label = "line";
        boolean result = addCGAObject(line, label);
        Point3d start2 = new Point3d(300,100,100);
        CGALineIPNS line2 = new CGALineIPNS(start2, v);
        String label2 = "line2";
        result = addCGAObject(line2, label2);*/
        
        // funktioniert
        //CGACircleIPNS _c = new CGACircleIPNS(CGAMultivector.fromGaalop(testCreateGaalopExampleCircle()));
        //System.out.println(_c.toString("_c"));
        //addCGAObject(_c, "_c");
        
    }
    
    /**
     * Add cga object into the visualization. 
     * 
     * @param values tuple of 32 values representing the multivector
     * @param fromGaalop set to true, if the values are in order of usage in Gaalop
     * @param isIPNS set to true, if an CGA object should be created in inner product null space representation
     * @param label label of the cga object shown in the visualisation
     */
    public void addCGAObject(double[] values, boolean fromGaalop, boolean isIPNS, String label){
        if (values.length != 32) throw new IllegalArgumentException("The given double[] is not of length 32 but \""+
                String.valueOf(values.length)+"\"!");
        double[] useValues = values;
        if (fromGaalop){
            useValues = CGAMultivector.fromGaalop(values);
        }
        //TODO
        // CGAMultivector.create() is incomplete
        addCGAObject(CGAMultivector.create(values, isIPNS), label);
    }
    
    /**
     * Add cga object into the visualization.
     * 
     * @param m cga multivector
     * @param label label
     * @return 
     * @throws IllegalArgumentException if multivector is no visualizable type
     */
    public boolean addCGAObject(CGAMultivector m, String label){
        
        // cga ipns objects
        if (m instanceof CGARoundPointIPNS){
            addPoint(m.decomposeTangentOrRound(), label, true);
            return true;
        } else if (m instanceof CGALineIPNS){
            return addLine(m.decomposeFlat(), label, true);
	} else if (m instanceof CGAPointPairIPNS cGAPointPairIPNS){
            //addPointPair(m.decomposeTangentOrRound(), label, true);
            // WORKAROUND, obige Methode solle funktionieren. Der Workaround funktioniert aber auch nicht
            //FIXME unklar ob normalize wirklich nötig ist
            CGAPointPairIPNS mn = cGAPointPairIPNS.normalize();
            
            double r2 = mn.squaredSize();
            if (r2 < 0){
                //FIXME
                // decomposition schlägt fehl
                // show imaginary point pairs as circles
                //CGACircleIPNS circle = new CGACircleIPNS(mn);
                //addCircle(mn.decomposeTangentOrRound(), label, true);
                //return true;
                System.out.println("Visualize imaginary point pair \""+label+"\" failed!");
                return false;
            // tangent vector
            } else if (r2 == 0){
                System.out.println("CGA-Object \""+label+"\" is a tangent vector - not yet supported!");
                return false;
                
                    // real point pair only?
                    //FIXME
            } else {
                iCGAPointPair.PointPair pp = cGAPointPairIPNS.decomposePoints();
                if (isValid(pp.p1()) && isValid(pp.p2())){
                    addPointPair(pp, label, true);
                    System.out.println("Visualize real point pair \""+label+"\"!");
                } else {
                    System.out.println("Point pair \""+label+"\" does not contain two valid points!");
                }
                return false;
            }
        } else if (m instanceof CGASphereIPNS){
            addSphere(m.decomposeTangentOrRound(), label, true);
            return true;
        } else if (m instanceof CGAPlaneIPNS){
            return addPlane(m.decomposeFlat(), label, true, true, true);
        } else if (m instanceof CGACircleIPNS){
            addCircle(m.decomposeTangentOrRound(), label, true);
            return true;
        } else if (m instanceof CGAOrientedPointIPNS){
            addOrientedPoint(m.decomposeTangentOrRound(), label, true);
            return true;
        }
        //TODO
        // flat-point
        
        // cga opns objects
        if (m instanceof CGARoundPointOPNS){
            addPoint(m.decomposeTangentOrRound(), label, false);
            return true;
        } else if (m instanceof CGALineOPNS){
            addLine(m.decomposeFlat(), label, false);
            return true;
        } else if (m instanceof CGAPointPairOPNS){
            addPointPair(m.decomposeTangentOrRound(), label, false);
            return true;
        } else if (m instanceof CGASphereOPNS){
            addSphere(m.decomposeTangentOrRound(), label, false);
            return true;
        } else if (m instanceof CGAPlaneOPNS){
            addPlane(m.decomposeFlat(), label, false, true, true);
            return true;
        } else if (m instanceof CGACircleOPNS){
            addCircle(m.decomposeTangentOrRound(), label, false);
            return true;
        } else if (m instanceof CGAOrientedPointOPNS){
            addOrientedPoint(m.decomposeTangentOrRound(), label, false);
            return true;
        }
        //TODO
        // flat-point
        
        throw new IllegalArgumentException("\""+m.toString("")+"\" has unknown type!");
    }
    
    
    // grade 1 multivectors
    
    /**
     * Add a point to the 3d view.
     * 
     * @param parameters unit is [m]
     * @param isIPNS 
     * @param label
     */
    public void addPoint(iCGATangentOrRound.EuclideanParameters parameters, String label, boolean isIPNS){
        Color color = COLOR_GRADE_1;
        if (!isIPNS) color = COLOR_GRADE_4;
        Point3d location = parameters.location();
        System.out.println("Add point \""+label+"\" at ("+String.valueOf(location.x)+","+
                String.valueOf(location.y)+", "+String.valueOf(location.z)+")!");
        location.scale(1000d);
        addPoint(location, color, POINT_RADIUS*2*1000, label); 
    }
    /**
     * Add a sphere to the 3d view.
     * 
     * @param parameters unit is [m]
     * @param label
     * @param isIPNS 
     */
    public void addSphere(iCGATangentOrRound.EuclideanParameters parameters, 
                          String label, boolean isIPNS){
        Color color = COLOR_GRADE_1;
        if (!isIPNS) color = COLOR_GRADE_4;
        Point3d location = parameters.location();
        location.scale(1000d);
        boolean imaginary = false;
        if (parameters.squaredSize() < 0){
            imaginary = true;
        }
        //TODO
        // Farbe ändern für imaginäre Kugeln
        double radius = Math.sqrt(Math.abs(parameters.squaredSize()));
        radius *= 1000;
        System.out.println("Add sphere \""+label+"\" at ("+String.valueOf(location.x)+"mm,"+
                String.valueOf(location.y)+"mm, "+String.valueOf(location.z)+"mm) with radius "+
                String.valueOf(radius)+"mm!");
        
        addSphere(location, radius, color, label);
    }
    /**
     * Add a plane to the 3d view.
     * 
     * @param parameters unit is [m]
     * @param label
     * @param isIPNS 
     * @param showPolygon 
     * @param showNormal 
     * @return true, if the plane is visible in the current bounding box
     */
     public boolean addPlane(iCGAFlat.EuclideanParameters parameters, String label, 
                         boolean isIPNS, boolean showPolygon, boolean showNormal){
        Color color = COLOR_GRADE_1;
        if (!isIPNS) color = COLOR_GRADE_4;
        Point3d location = parameters.location();
        location.scale(1000d);
        Vector3d a = parameters.attitude();
        System.out.println("plane "+label+" "+String.valueOf(a.x)+", "+String.valueOf(a.y)+", "+String.valueOf(a.z));
        boolean result = true;
        if (showPolygon){
            result = addPlane(location, a, color, label);
        }
        if (result && showNormal){
            addArrow(location, a, TANGENT_LENGTH, 
                         LINE_RADIUS*1000, color, label);
        }
        return result;
    }
    
    
    // grade 2
    
    /**
     * Add a line to the 3d view.
     * 
     * @param parameters, unit is [m]
     * @param isIPNS
     * @param label 
     * @return true if the line is inside the bounding box and therefore visible
     */
    public boolean addLine(iCGAFlat.EuclideanParameters parameters, String label, boolean isIPNS){
        Color color = COLOR_GRADE_2;
        if (!isIPNS) color = COLOR_GRADE_3;
        Point3d location = parameters.location();
        location.scale(1000d);
        Vector3d a = parameters.attitude();
		System.out.println("add line \""+label+"\" at ("+String.valueOf(location.x)+", "+String.valueOf(location.y)+
				", "+String.valueOf(location.z)+") with a=("+String.valueOf(a.x)+", "+String.valueOf(a.y)+", "+
				String.valueOf(a.z)+")");
        return addLine(location, a, color, LINE_RADIUS*1000,  label); 
    }
    
    /**
     * Add oriented-point visualized as arrow.
     * 
     * @param parameters
     * @param label
     * @param isIPNS 
     */
    public void addOrientedPoint(iCGATangentOrRound.EuclideanParameters parameters, 
                                                 String label, boolean isIPNS){
        Color color = COLOR_GRADE_2;
        if (!isIPNS) color = COLOR_GRADE_3;
        Point3d location = parameters.location();
        location.scale(1000d);
        Vector3d direction = parameters.attitude();
        addArrow(location, direction, TANGENT_LENGTH, 
                         LINE_RADIUS*1000, color, label);
    }
    
    /**
     * Add a circle to the 3d view.
     * 
     * @param parameters unit in [m]
     * @param label
     * @param isIPNS 
     */
    public void addCircle(iCGATangentOrRound.EuclideanParameters parameters, 
                          String label, boolean isIPNS){
        Color color = COLOR_GRADE_2;
        if (!isIPNS) color = COLOR_GRADE_3;
        boolean isImaginary = false;
        double r2 = parameters.squaredSize();
        if (r2 <0) {
            isImaginary = true;
            r2 = -r2;
        }
        r2*=1000000d;
        Point3d location = parameters.location();
        location.scale(1000d);
        Vector3d direction = parameters.attitude();
        System.out.println("Add circle \""+label+"\" at ("+String.valueOf(location.x)+"mm,"+
                String.valueOf(location.y)+"mm, "+String.valueOf(location.z)+"mm) with radius "+
                String.valueOf(Math.sqrt(r2)+
                "\"[mm] and n= ("+String.valueOf(direction.x)+","+
                String.valueOf(direction.y)+", "+String.valueOf(direction.z)+")!"));
        addCircle(location, direction, 
                (float) Math.sqrt(r2), color, label, isImaginary);
    }
    
    
    // grade 3
    
    /**
     * Add a point-pair to the 3d view.
     * 
     * No imaginary point-pairs, because these are ipns circles.
     * 
     * @param pp unit in [m]
     * @param label
     * @param isIPNS true, if ipns representation
     */
    public void addPointPair(iCGAPointPair.PointPair pp, String label, boolean isIPNS){
        
        if (!isValid(pp.p1()) || !isValid(pp.p2())){
            throw new IllegalArgumentException("addPointPair(): pp does not contain two valid points!");
        }
        Color color = COLOR_GRADE_3;
        if (!isIPNS) color = COLOR_GRADE_2;
        Point3d[] points = new Point3d[]{pp.p1(), pp.p2()};
        points[0].scale(1000d);
        points[1].scale(1000d);
        addPointPair(points[0], points[1], label, color, color, LINE_RADIUS*1000, POINT_RADIUS*2*1000);
    }
    
     /**
     * Add a point-pair to the 3d view.
     * 
     * Because parameters are decomposed the point-pair can not be imaginary.
     * 
     * @param parameters unit in [m]
     * @param label
     * @param isIPNS true, if ipns represenation
     */
    public void addPointPair(iCGATangentOrRound.EuclideanParameters parameters, 
                             String label, boolean isIPNS){
        Color color = COLOR_GRADE_3;
        if (!isIPNS) color = COLOR_GRADE_2;
        Point3d[] points = decomposePointPair(parameters);
        points[0].scale(1000d);
        points[1].scale(1000d);
        addPointPair(points[0], points[1], label, color, color, 
                LINE_RADIUS*1000, POINT_RADIUS*2*1000);
    }
    
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
                TANGENT_LENGTH, LINE_RADIUS*1000, color, label);
    }
   
  
    // helper methods
    
    private static double signedRadius(double squaredRadius){
        double r = Math.sqrt(Math.abs(squaredRadius));
        if (squaredRadius < 0) r = -r;
        return r;
    }
   
    /**
     * Decompose point-pair into two points.
     * 
     * Implementation based on determination of location and squared-size.
     * 
     * @param parameters
     * @return 
     */
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
    
    
    /**
     * This method must be invoked outside the EVT, because long running file
     * loading can be included. It is invoked via AnalysisLauncher.open().
     * 
     * @throws Exception 
     */
    @Override
    public void init() throws Exception {
        
        if (SwingUtilities.isEventDispatchThread()){
            System.out.println("init() invoked inside the EVT!");
            throw new RuntimeException("GeometryViewCGA.init() is invoked inside the EDT!");
        } 
        
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
        
        //Set up ObjectLoader and Mouse
        colladaLoader = ObjectLoader.getLoader();
        
        //Add the ChessFloor and set size
        /*this.setUpChessFloor(100f);
        chart.getScene().getGraph().addGraphListener(() -> {
            updateChessFloor(true, CHESS_FLOOR_WIDTH);
        });*/
        
        //setUpPickingSupport();
        
        //Light light = chart.addLightOnCamera();
        
        //addSkeleton("data/golembones/golembones.obj");
        
        
        //addPoint(new Point3d(1,1,1), Color.BLUE, 0.6f, "Point1");
        //addSphere(new Point3d(20,20,20), 10, Color.ORANGE, "Sphere1");
        
        //addPlane(new Point3d(5d,5d,5d), new Vector3d(0d,0d,5d), new Vector3d(5d,0d,0d), Color.RED, "Plane1");
        
        //addArrow(new Point3d(0d, 0d, 0d), new Vector3d(0d,0d,2d), 3f, 0.5f, Color.CYAN, "Arrow1");
        
        //addLabel(new Point3d(10d, 10d, 10d), "Label", Color.BLACK);
        //addCircle(new Point3d(20,20,20), new Vector3d(0,0,1),10,Color.RED, "Circle");
        
        //addLine(new Vector3d(0d,0d,-1d), new Point3d(3d,0d,3d), Color.CYAN, 0.2f, 10, "ClipLinie");
        
        //addPlane(new Point3d(0,1,5), new Vector3d(0,-10,0), new Vector3d(-10,0,0), Color.ORANGE, "ClipPlane");
        //addPoint(new Point3d(0,0,0), Color.BLUE, 0.6f, "Point1");
        //addPoint(new Point3d(1,10,1), Color.BLUE, 0.6f, "Point3");
        //addPoint(new Point3d(20,20,20), Color.BLUE, 0.6f, "Point2");    
        //addPlane(new Point3d(5d,5d,5d), new Vector3d(0d,0d,5d), new Vector3d(5d,0d,0d), Color.RED, "Plane1");
        //addLine(new Vector3d(0d,0d,-1d), new Point3d(3d,0d,3d), Color.CYAN, 0.2f, 10, "ClipLinie");
        //addArrow(new Point3d(7d, 7d, 7d), new Vector3d(0d,0d,2d), 3f, 0.5f, Color.CYAN, "Arrow1");
         
        
        // test
        
        
        double[] delta_theta_rad = new double[]{0d,0d,0d,0d,0d,0d,0d};      

        ArrayList<String> pathList = new ArrayList<>();
        pathList.add("data/objfiles/base.dae");
        pathList.add("data/objfiles/shoulder.dae");
        pathList.add("data/objfiles/upperarm.dae");
        pathList.add("data/objfiles/forearm.dae");
        pathList.add("data/objfiles/wrist1.dae");
        pathList.add("data/objfiles/wrist2.dae");
        pathList.add("data/objfiles/wrist3.dae");
        
        // Unklar, ob dies draw-Aufrufe auslöst und damit Codes in den EDT 
        // ausgelagert werden müssen
        addRobotUR5e(pathList, delta_theta_rad);
        
        //addGeometricObjects();
       
    }
   
}
