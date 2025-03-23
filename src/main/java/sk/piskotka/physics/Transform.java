package sk.piskotka.physics;

import java.util.ArrayList;
import java.util.List;

import sk.piskotka.logger.Logger;

public class Transform {
    private Transform parent;
    private List<Transform> children;

    private Vec2 position;
    private double rotation;

    public Transform(){
        children = new ArrayList<>();
    }

    public void setParent(Transform parent){
        if (!isRoot())
            this.parent.removeChild(this);

        if (parent == null)
            Logger.throwError(getClass(), "Transform was created with null parent which cannot happen!! Set a valid parent");
        this.parent = parent;
        parent.addChild(this);
    }

    public Transform getParent(){
        return parent;
    }

    public List<Transform> getChildren(){
        return children;
    }

    public void addChild(Transform transform){
        children.add(transform);
    }

    public Transform getChild(int index){
        return children.get(index);
    }

    public void removeChild(Transform transform){
        children.remove(transform); // Not sure if java can garbage collect its children or not
        //TODO: Possible memory leak
    }

    public Vec2 getLocalPos() {
        return position;
    }
    public void setLocalPos(Vec2 position) {
        this.position = position;
    }

    public double getLocalRot(){
        return rotation;
    }

    public void setLocalRot(double rotation){
        this.rotation = rotation;
    }

    public Vec2 getGlobalPos(){
        if (isRoot() || parent.isRoot())
            return getLocalPos(); // Ill just ignore root rotation and position

        return getLocalPos().rotated(parent.getLocalRot()).add(parent.getGlobalPos());
    }

    public double getGlobalRot(){
        if (isRoot() || parent.isRoot())
            return getLocalRot();
        return getLocalRot()+parent.getGlobalRot();
    }

    public Vec2 getGlobalFromLocalPos(Vec2 local){
        return local.rotated(getLocalRot()).add(getGlobalPos());
    }

    public List<Vec2> transformPoints(List<Vec2> local){
        Vec2 globalPos = getGlobalPos();
        List<Vec2> global = new ArrayList<>();
        for (Vec2 point : local) {
            global.add(point.rotated(getLocalRot()).add(globalPos));
        }
        return global;
    }

    public List<Vec2> movePoints(List<Vec2> local){
        Vec2 globalPos = getGlobalPos();
        List<Vec2> global = new ArrayList<>();
        for (Vec2 point : local) {
            global.add(globalPos);
        }
        return global;
    }

    public boolean isRoot(){
        return parent == null;
    }

    public static Transform createRoot(){
        return new Transform();
    }

    @Override
    public String toString() {
        String name = super.toString();
        return name.substring(name.lastIndexOf(".")+1);
    }

    public void onDeath(){
        getParent().removeChild(this);
    };
}
