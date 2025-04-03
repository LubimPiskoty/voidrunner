package sk.piskotka.render;

/**
 * Represents an object that can be drawn on a rendering context.
 */
public interface Drawable {

    /**
     * Draws the object using the provided rendering context.
     *
     * @param ctx the rendering context used to draw the object
     */
    public void draw(Renderer ctx);
}
