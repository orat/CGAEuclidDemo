package de.orat.math.cgaeucliddemo;

import de.orat.math.cga.api.CGAAttitudeVectorOPNS;
import de.orat.math.cga.api.CGABivector;
import de.orat.math.cga.api.CGAMultivector;
import de.orat.math.cga.api.CGARotor;
import de.orat.math.cga.api.CGATangentVectorIPNS;
import de.orat.math.cga.api.CGATangentVectorOPNS;
import de.orat.math.view.euclidview3d.ObjectLoader;
import java.util.ArrayList;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Tuple3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.lights.Light;
import org.jzy3d.plot3d.rendering.scene.Graph;

/**
  * @author Oliver Rettig (Oliver.Rettig@orat.de)
  */
public class CGAEuclidDemo {

    public static void main(String[] args) {
        //testBivectorCreation();
        //testRotationOfAttitude();
        testRotationOfTangent();
    }
    
    private static double epsilon = 0.0000001;
    
    private static boolean equals(Tuple3d a, Tuple3d b){
        boolean result = true;
        if (Math.abs(a.x-b.x) > epsilon){
            result = false;
        }
        if (Math.abs(a.y-b.y) > epsilon){
            result = false;
        }
        if (Math.abs(a.z-b.z) > epsilon){
            result = false;
        }
        return result;
    }
    
    /**
     * Create base tangent vector.
     * 
     * @param phi
     * @param theta
     * @return 
     */
    public CGATangentVectorOPNS createBaseTangentVector(double phi, double theta){
        CGATangentVectorOPNS result = null;
        return result;
    }
    
    /**
     * Determine location on a unit-sphere for a left-handed coordinate system,
     * with z-axis down, x-axis to the right, y-axis to the front. Phi around the
     * z-axis starting at the x-axis and theta around y-axis, starting at the 
     * z-axis.
     * 
     * @param phi
     * @param theta
     * @return location on a unit-sphere
     */
    public Point3d createLocationLeftHanded(double phi, double theta){
        return new Point3d(Math.sin(theta)*Math.cos(phi),
                                     Math.sin(theta)*Math.sin(phi),
                                     Math.cos(theta));
    }
    
    public static CGARotor createRotorZY(double phi, double theta){
        // Rotor around global z-axis
        CGABivector BZ = new CGABivector(new Vector3d(1d,0d,0d), new Vector3d(0d,1d,0d));
        CGARotor rotZ = new CGARotor(BZ, phi);
        
        // Rotor around rotated y-axis
        CGABivector BY = new CGABivector(new Vector3d(1d,0d,0d), new Vector3d(0d,0d,-1d));
        CGABivector BYRotZ = new CGABivector(rotZ.transform(BY));
        CGARotor rotZY =  new CGARotor(BYRotZ, theta);
        
        // first rotation around z, second rotation around y
        return new CGARotor(rotZY.gp(rotZ));
    }
    public static CGARotor createRotorConjunct(double phi, double theta){
        CGARotor rotZY = createRotorZY(phi, theta);
        
        CGABivector BX = new CGABivector(new Vector3d(0d,1d,0d), new Vector3d(0d,0d,1d));
        CGABivector BXRotZY = (CGABivector) rotZY.transform(BX);
        
        //TODO
        return null;
    }
    
   
    
    // test tangent vector construction and decomposition
    public static void testRotationOfTangent(){
        
        Point3d p = new Point3d(0d,1d,0d);
        toString("P", p);
        Vector3d u =  new Vector3d(0d,1d,0d);
        toString("u", u);
        
        // TangentVector ytangent = (eo^e2 + eo^ei + 0.5*e2^ei)
        CGATangentVectorOPNS ytangent = new CGATangentVectorOPNS(p,u);
        System.out.println(ytangent.toString("TangentVector ytangent"));
        // TangentVector* ytangentDual = (eo^e1^e3 - e1^e2^e3 + 0.45*e1^e3^ei)
        CGATangentVectorIPNS ytangentDual = ytangent.dual();
        System.out.println(ytangentDual.toString("TangentVector* ytangentDual"));
        
        // The given multivector m is not of grade 3! 
        // 5.55111512312578E-17*eo^e1^e2^e3 - 5.551115123125781E-17*eo^e1^e3^ei + 2.7755575615628904E-17*e1^e2^e3^ei
        //CGATangentVectorIPNS ytangentDual2 = new CGATangentVectorIPNS(p,u);
        //System.out.println("TangentVector* ytangentDual2 = "+ytangentDual2.toString());
        
        
        Vector3d yTangentVec = ytangent.attitude();
        toString("yTangent (decomposed) u",yTangentVec);
        if (!equals(yTangentVec,u)){
            throw new RuntimeException("Decompose of the direction of the tangent failed: "+
                    u.toString()+" != "+yTangentVec.toString());
        }
        
        Point3d yTangentPoint = ytangent.location();
        toString("yTangent (decomposed) P", yTangentPoint);
        if (!equals(yTangentPoint,p)){
            throw new RuntimeException("Decompose of the location of the tangent failed: "+p.toString()+
                    " != "+yTangentPoint.toString());
        }
        
        
        /*double phi = 0d; 
        double theta = Math.PI/2d;
        CGARotor ZYRot = createRotorZY(phi, theta);
        CGAAttitudeVectorOPNS ydirRotZY = new CGAAttitudeVectorOPNS(ZYRot.transform(ydir));
        // 1.0*e2^ei + 0.0*e1^e2^e3^ei 1.0*e2^ei + 0.0*e1^e2^e3^ei
        System.out.println("AttitudeVectorRotZY ydirRotZY = "+ydirRotZY.toString());*/
    }
    
    public static void testBivectorCreation(){
        CGABivector BZ = new CGABivector(new Vector3d(1d,0d,0d), new Vector3d(0d,1d,0d));
        // Bivector z = 1.0*e1^e2
        System.out.println("Bivector z = "+BZ.toString());
        
        CGABivector BZ1 = new CGABivector(CGAMultivector.createEx(1d).op(CGAMultivector.createEy(1d)));
        // Bivector z1 = 1.0*e1^e2
        System.out.println("Bivector z1 = "+BZ1.toString());
    }
    
    public static void toString(String name, Tuple3d value){
        System.out.println(name+" = ("+String.valueOf(value.x)+","+String.valueOf(value.y)+","+String.valueOf(value.z)+")");
    }
    
    
}
