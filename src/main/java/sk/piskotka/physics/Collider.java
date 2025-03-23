package sk.piskotka.physics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.paint.Color;
import sk.piskotka.GameManager;
import sk.piskotka.logger.Logger;
import sk.piskotka.render.Drawable;
import sk.piskotka.render.Renderer;
import sk.piskotka.shapes.PolygonShape;
import sk.piskotka.shapes.Shape;

//TODO: Check if shape is convex and split up into multiple colliders if needed


public class Collider implements Drawable {
    private List<Vec2> vertices;
    private List<Vec2> edgeNormals;
    private Transform transform;
    
    private double boundingCircleRadius;
    private Vec2 boundingCircleCenter;

    private Set<Collider> colliders;
    private CollisionEvents collisionEvents;

    public class CollisionInfo {
        public boolean result;
        public Vec2 normal;
        public double penetration;
        public List<Vec2> contacts;
        private Collider refCollider;
        private Collider incCollider;
    }

    public Collider(PhysicsBody pBody, List<Vec2> vertices){
        if (vertices == null || vertices.size() < 3)
            Logger.throwError(getClass(), "Vertices are null or smaller then 3. Try checking object instantiation");
        
        if (pBody == null)
            Logger.throwError(getClass(), "PhysicalBody is null. Try checking object instantiation");
        
        this.transform = pBody;
        this.vertices = vertices;
        this.edgeNormals = computeEdgeNormals(vertices);
        this.colliders = new HashSet<>();
        this.collisionEvents = pBody;
        computeBoundingCircle();
    }

    private List<Vec2> computeEdgeNormals(List<Vec2> vertices){
        List<Vec2> normals = new ArrayList<>();

        Vec2 prev = vertices.get(vertices.size()-1);
        for(Vec2 next : vertices){
            normals.add(prev.subtract(next).normal().normalized());
            prev = next;
        }

        return normals;
    }

    private void computeBoundingCircle(){

        boundingCircleCenter = Vec2.ZERO();
        boundingCircleRadius = Double.POSITIVE_INFINITY;

        // Find the centroid
        for(Vec2 p : vertices)
            boundingCircleCenter.add(p);
        boundingCircleCenter.multiply(1/vertices.size());

        //Find the furthest points from centroid
        for(Vec2 p : vertices){
            double dist = boundingCircleCenter.subtract(p).length();
            if (boundingCircleRadius > dist)
                boundingCircleRadius = dist;
        }
    }

    //TODO: Refactor this monstrosity
    private boolean isBoundingColliding(Collider other){
        return boundingCircleCenter.subtract(other.boundingCircleCenter)
                .add(transform.getGlobalPos()).subtract(other.transform.getGlobalPos()).length() 
                <= boundingCircleRadius + other.boundingCircleRadius;
    }

    private static double[] projectColliderToNormal(Collider collider, Vec2 normal){
        double high = Double.NEGATIVE_INFINITY;
        double low = Double.POSITIVE_INFINITY;
        for(Vec2 vert : collider.transform.transformPoints(collider.vertices)){
            double projected = normal.dot(vert);
            high = Math.max(high, projected);
            low = Math.min(low, projected);
        }
        return new double[]{high, low};
    }

    private static boolean isCollidingFromA(Collider A, Collider B, CollisionInfo outInfo){
        outInfo.penetration = Double.POSITIVE_INFINITY;
        // Find separating line
        for(Vec2 edgeNorm : A.edgeNormals){
            double[] projA = projectColliderToNormal(A, edgeNorm);
            double[] projB = projectColliderToNormal(B, edgeNorm);

            if (projA[0] < projB[1] || projA[1] > projB[0])
                return outInfo.result = false;
            
            double penetration = Math.min(projA[0] - projB[1], projB[0] - projA[1]);
            // Logger.logDebug(Collider.class,
            //     String.format("projA: %s, projB: %s", new Vec2(projA[0], projA[1]), new Vec2(projB[0], projB[1])));
            
            if (penetration < outInfo.penetration){
                outInfo.penetration = penetration;
                outInfo.normal = edgeNorm;
                outInfo.refCollider = A;
                outInfo.incCollider = B;
            }
        }

        for(Vec2 edgeNorm : B.edgeNormals){
            double[] projA = projectColliderToNormal(A, edgeNorm);
            double[] projB = projectColliderToNormal(B, edgeNorm);

            if (projA[0] < projB[1] || projA[1] > projB[0])
                return outInfo.result = false;
                
            double penetration = Math.min(projA[0] - projB[1], projB[0] - projA[1]);
            if (penetration < outInfo.penetration){
                // Logger.logDebug(Collider.class,
                //     String.format("projA: %s, projB: %s", new Vec2(projA[0], projA[1]), new Vec2(projB[0], projB[1])));
                outInfo.penetration = penetration;
                outInfo.normal = edgeNorm;
                outInfo.refCollider = B;
                outInfo.incCollider = A;
            }
            
        }   
        return outInfo.result = true;
    }

    // First check the bounding collision and then construct SAT
    public CollisionInfo checkCollisionWith(Collider other){
        CollisionInfo cInfo = new CollisionInfo();
        //                                                                    Why was this part in the blog if it makes it worse?
        if (isBoundingColliding(other) && isCollidingFromA(this, other, cInfo)){// || isCollidingFromA(other, this));
            collisionEvents.onCollision(other);
            other.collisionEvents.onCollision(this);
            colliders.add(other);
            other.colliders.add(this);
            // Find the collision manifold
            findCollisionManifold(cInfo.refCollider, cInfo.incCollider, cInfo);

        }else{
            colliders.remove(other);
            other.colliders.remove(this);
        }
        return cInfo;
    }

    public boolean isColliding(){
        return colliders.size() != 0;
    }

    private static void findCollisionManifold(Collider A, Collider B, CollisionInfo info) {
        // Step 1: Identify the reference edge on A. (This is the edge whose normal was used.)
        int refEdgeIndex = findReferenceEdgeIndex(A, info.normal);
        Vec2 refV1 = A.vertices.get((refEdgeIndex + A.vertices.size()-1) % A.vertices.size());
        Vec2 refV2 = A.vertices.get(refEdgeIndex);

        // Compute the reference edge direction and its perpendicular normals.
        Vec2 refEdge = A.edgeNormals.get(refEdgeIndex).rotated(A.transform.getLocalRot());

        // Step 2: Find the incident edge on B – the edge on B that is most anti-parallel to the reference normal.
        int incEdgeIndex = findIncidentEdgeIndex(B, info.normal);
        Vec2 incV1 = B.vertices.get((incEdgeIndex + B.vertices.size()-1) % B.vertices.size());
        Vec2 incV2 = B.vertices.get(incEdgeIndex);

        //DEBUG - Render out the incident and reference edges
        if (GameManager.isDebug()){
            debugEdgeNormals(A, refEdgeIndex, Color.AQUA);
            debugEdgeNormals(B, incEdgeIndex, Color.RED);
            Logger.logDebug(Collider.class, 
                String.format("info.penetration %s", info.penetration));
            //Logger.logDebug(Collider.class, String.format("incV1: %s, incV2: %s", globalInc1, globalInc2));
            //Logger.logDebug(Collider.class, String.format("refV1: %s, refV2: %s", globalRef1, globalRef2));
        }

        // Step 3: Clip the incident edge against the side planes of the reference edge.
        // Compute side (or “clip”) normals for the reference edge.
        // They are the normals perpendicular to the reference edge (pointing inward).
        Vec2 sideNormal1 = refEdge.normal();
        Vec2 sideNormal2 = sideNormal1.multiply(-1);

        // Offsets for the clipping lines (distance from origin)
        double offset1 = sideNormal1.dot(refV1);
        double offset2 = sideNormal2.dot(refV2);

        // Prevent exceptions
        List<Vec2> contacts = new ArrayList<>();
        info.contacts = contacts;
        // First clip: against sideNormal1
        List<Vec2> clippedPoints = clipSegmentToLine(incV1, incV2, sideNormal1, offset1);
        if (clippedPoints.size() < 2) return; // Not enough points

        // Second clip: against sideNormal2
        clippedPoints = clipSegmentToLine(clippedPoints.get(0), clippedPoints.get(0), sideNormal2, offset2);
        if (clippedPoints.size() < 2) return;
        
        // Step 4: Only keep points that are within the penetration depth.
        double refOffset = info.normal.dot(refV1);
        for (Vec2 p : clippedPoints) {
            if (info.normal.dot(p) - refOffset <= info.penetration + 1e-6) {
                contacts.add(p);
            }
        }
    }

    // Finds the index of the edge in collider A whose normal is closest to the given normal.
    private static int findReferenceEdgeIndex(Collider A, Vec2 collisionNormal) {
        int bestIndex = 0;
        double bestDot = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < A.edgeNormals.size(); i++) {
            double dot = collisionNormal.dot(A.edgeNormals.get(i).rotated(A.transform.getLocalRot()));
            if (dot > bestDot) {
                bestDot = dot;
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    // Finds the incident edge on collider B: the edge whose normal is most anti-parallel to the collision normal.
    private static int findIncidentEdgeIndex(Collider B, Vec2 refNormal) {
        int bestIndex = 0;
        double bestDot = Double.POSITIVE_INFINITY;
        for (int i = 0; i < B.edgeNormals.size(); i++) {
            double dot = refNormal.dot(B.edgeNormals.get(i).rotated(B.transform.getLocalRot()));
            if (dot < bestDot) {
                bestDot = dot;
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    // Clips the segment (defined by v1 and v2) against the line: (n dot v) = offset.
    // Returns the points that lie on the "inside" half-space.
    private static List<Vec2> clipSegmentToLine(Vec2 v1, Vec2 v2, Vec2 n, double offset) {
        List<Vec2> output = new ArrayList<>();
        double d1 = n.dot(v1) - offset;
        double d2 = n.dot(v2) - offset;

        // If v1 is inside, keep it.
        if (d1 >= 0) {
            output.add(v1);
        }
        // If v2 is inside, keep it.
        if (d2 >= 0) {
            output.add(v2);
        }
        // If they are on different sides, compute intersection.
        if (d1 * d2 < 0) {
            double t = d1 / (d1 - d2);
            Vec2 intersection = v1.add(v2.subtract(v1).multiply(t));
            output.add(intersection);
        }
        return output;
    }


    @Override
    public void draw(Renderer ctx) {
        // Debug only
        Shape circleShape = new PolygonShape(boundingCircleCenter.getX(), boundingCircleCenter.getY(),
                                            (int)boundingCircleRadius, 16);
        Vec2 pos = transform.getGlobalPos();
        ctx.drawPolygonWithOffset(pos.getX(), pos.getY(), circleShape.getPoints(), isColliding() ? Color.RED.darker() : Color.GREEN.darker());

    }

    private static void debugEdgeNormals(Collider c, int edgeIndex, Color color){
        if (!GameManager.isDebug())
            return;

        Vec2 prev = c.vertices.get((edgeIndex + c.vertices.size()-1)%c.vertices.size());
        Vec2 next = c.vertices.get(edgeIndex);
        GameManager.getRenderer().drawVector(
            c.transform.getGlobalFromLocalPos(prev.add(next).multiply(0.5)),
            c.edgeNormals.get(edgeIndex).multiply(prev.subtract(next).length()/2).rotated(c.transform.getLocalRot()), color);
    
    }

    @Override
    public String toString() {
        return transform.toString()+"-Collider";
    }

    private void removeCollider(Collider other){
        colliders.remove(other);
    }

    public void onDestroy() {
        for(Collider collider : colliders){
            collider.removeCollider(this);
        }
    }
}