package sk.piskotka.components;

import sk.piskotka.physics.PhysicsBody;

public abstract class Component {
    protected PhysicsBody attachedTo;
    public boolean isEnabled;

    public Component(PhysicsBody attachedTo){
        this.attachedTo = attachedTo;
        this.isEnabled = true;
    }

    public void Destroy(){
        isEnabled = false;
    }

    public PhysicsBody getPhysicsBody(){return attachedTo;}
}
