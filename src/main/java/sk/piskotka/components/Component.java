package sk.piskotka.components;

import sk.piskotka.physics.PhysicsBody;

/**
 * Abstract base class for all components in the system.
 * A component is attached to a {@link PhysicsBody} and can be enabled or disabled.
 */
public abstract class Component {
    protected PhysicsBody attachedTo;
    public boolean isEnabled;

    /**
     * Constructs a new Component attached to the specified {@link PhysicsBody}.
     *
     * @param attachedTo the {@link PhysicsBody} this component is attached to
     */
    public Component(PhysicsBody attachedTo){
        this.attachedTo = attachedTo;
        this.isEnabled = true;
    }

    /**
     * Disables the component, marking it as destroyed.
     */
    public void Destroy(){
        isEnabled = false;
    }

    /**
     * Gets the {@link PhysicsBody} this component is attached to.
     *
     * @return the attached {@link PhysicsBody}
     */
    public PhysicsBody getPhysicsBody(){return attachedTo;}
}
