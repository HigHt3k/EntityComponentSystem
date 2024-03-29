package de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.render;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.*;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.Query;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.ColorPalette;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.fonts.FontCollection;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * The RenderingEngine class is responsible for rendering various graphical elements in the aircraft systems architect engine.
 * It provides methods for rendering text, shapes, images, lines, and animations, as well as managing render layers and
 * entities with render components.
 */
public class RenderingEngine {
    private static final float scaleW = Game.config().renderConfiguration().getScaleWidth();
    private static final float scaleH = Game.config().renderConfiguration().getScaleHeight();
    public static final int MARGIN = (int) (15 * scaleW); //px
    private int cursorXPosition = 0;
    private int cursorYPosition = 0;

    private ArrayList<Entity> renderEntities;

    private final Random randomizer = new Random();

    private Graphics2D g;

    /**
     * Create a new RenderingEngine instance.
     * Initializes a new empty array for entities considered for the rendering cycle
     */
    public RenderingEngine() {
        this.renderEntities = new ArrayList<>();
    }

    /**
     * Render a text
     *
     * @param g:                   the graphics context
     * @param text:                the text to render
     * @param color:               the text color
     * @param font:                font type
     * @param x:                   x location of text box
     * @param y:                   y location of text box
     * @param width:               width of text box
     * @param height:              height of text box
     * @param horizontalAlignment: horizontal alignment within box
     * @param verticalAlignment:   vertical alignment within box
     */
    public static synchronized void renderText(Graphics2D g, String text, Color color, Font font, int x, int y, int width, int height,
                                  TextHorizontalAlignment horizontalAlignment, TextVerticalAlignment verticalAlignment) {
        // TODO: Handle text rendering with vertical / horizontal alignment options
        if (horizontalAlignment == TextHorizontalAlignment.CENTER && verticalAlignment == TextVerticalAlignment.CENTER) {
            int left = 0;
            int top = 0;

            g.setFont(font);
            int w = g.getFontMetrics().stringWidth(text);
            int h = g.getFontMetrics().getHeight();

            left = (width - w) / 2;

            TextRenderer.render(g, text, color, font, new Point(x + left, y + top));
        } else {
            renderText(g, text, color, font, x, y, width, height);
        }
    }

    /**
     * Render a text
     *
     * @param g:      the graphics context
     * @param text:   the text to render
     * @param color:  the text color
     * @param font:   font type
     * @param x:      x location of text box
     * @param y:      y location of text box
     * @param width:  width of text box
     * @param height: height of text box
     */
    public static synchronized void renderText(Graphics2D g, String text, Color color, Font font, int x, int y, int width, int height) {
        if (text == null) {
            return;
        }
        g.setFont(font);
        if (text.contains("@")) {
            String temp = text;
            try {
                int id = Integer.parseInt(temp.replace("@", ""));
                text = Game.res().language().getStringValue(id, Game.config().getLanguage());
            } catch(NumberFormatException ex) {
                Game.logger().severe("Cannot parse id: " + temp);
            }
        }

        if (text.contains("\n")) {
            long lineCount = text.chars().filter(ch -> ch == '\n').count();
            int fontHeight = g.getFontMetrics().getHeight();

            if(lineCount * fontHeight > height) {
                int maxLines = height / fontHeight;
                text = text.substring(0,StringUtils.ordinalIndexOf(text, "\n", maxLines));
            }

            TextRenderer.render(g, text, color, font, new Point(x + MARGIN, y));

            return;
        }

        text = text.trim().replaceAll(" +", " ");
        text = text.replaceAll("\n ", "\n");

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                Game.config().renderConfiguration().getAliasingText());

        int maxWidth = width - 2 * MARGIN;
        int rows = 1;

        // reformat the string
        StringBuilder tempLine = new StringBuilder();
        String tempWord = "";
        StringBuilder newText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (rows * g.getFontMetrics().getHeight() > height) {
                TextRenderer.render(g, newText.toString(), color, font, new Point(x + MARGIN, y));
                break;
            }
            tempWord += text.charAt(i);
            if (Character.isWhitespace(text.charAt(i)) || Character.isSpaceChar(text.charAt(i))) {
                if (g.getFontMetrics().stringWidth(String.valueOf(tempLine)) + g.getFontMetrics().stringWidth(tempWord) > maxWidth) {
                    rows++;
                    if (rows * g.getFontMetrics().getHeight() <= height) {
                        newText.append(tempLine).append("\n");
                        tempLine = new StringBuilder();
                    }
                } else {
                    tempLine.append(tempWord);
                    tempWord = "";
                }
            }
        }

        if (g.getFontMetrics().stringWidth(String.valueOf(tempLine)) + g.getFontMetrics().stringWidth(tempWord) > maxWidth) {
            newText.append(tempLine).append("\n").append(tempWord);
            rows++;
        } else {
            tempLine.append(tempWord);
            newText.append(tempLine);
        }

        TextRenderer.render(g, newText.toString(), color, font, new Point(x + MARGIN, y));
    }

    /**
     * Render a shape (e.g. Rectangle, Ellipse2D, ...) to the graphics context. Can also be filled.
     *
     * @param g:           graphics context
     * @param shape        Shape to render, e.g. Rectangle or Ellipse2D
     * @param borderColor: Color to draw border
     * @param fillColor:   fill color, if null then not filled
     */
    public static synchronized void renderShape(Graphics2D g, Shape shape, Color borderColor, Color fillColor, int thickness) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                Game.config().renderConfiguration().getAntialiasing());

        ShapeRenderer.render(g, shape, borderColor, fillColor, thickness);
    }

    /**
     * Render a Buffered Image to the screen at a given position and size in the graphics context.
     *
     * @param g:      Graphics2D context
     * @param img:    Image to render
     * @param x:      position x
     * @param y:      position y
     * @param width:  size in x direction (px)
     * @param height: size in y direction (px)
     */
    public static synchronized void renderImage(Graphics2D g, BufferedImage img, int x, int y, int width, int height) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                Game.config().renderConfiguration().getAntialiasing());

        ImageRenderer.render(g, img, x, y, width, height);
    }

    /**
     * Render a line to the graphics context
     * @param g: the graphics context
     * @param p1: Point 1
     * @param p2: Point 2
     * @param color: Line Color
     * @param thickness: Line thickness
     */
    public static synchronized void renderLine(Graphics2D g, Point p1, Point p2, Color color, int thickness) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                Game.config().renderConfiguration().getAntialiasing());

        LineRenderer.render(g, p1, p2, color, thickness);
    }

    /**
     * Render an animated image (gif) to the screen
     * @param g: graphics context
     * @param animation: animated image (gif)
     * @param x: x coordinate in px of top-left corner of image
     * @param y: y coordinate in px of top-left corner of image
     * @param width: width of image to render in px
     * @param height: height of image to render in px
     */
    public static synchronized void renderAnimation(Graphics2D g, Image animation, int x, int y, int width, int height) {
        AnimationRenderer.renderAnimation(g, animation, x, y, width, height);
    }

    /**
     * Collects all entities and detects if they have a @{@link RenderComponent} which should be rendered
     */
    public synchronized void collectAndRenderEntities() {
        recollectEntities();
        renderLayer(Layer.BACKGROUND);
        renderLayer(Layer.GAMELAYER1);
        renderLayer(Layer.GAMELAYER2);
        renderLayer(Layer.GAMELAYER3);
        renderLayer(Layer.HOVER);
        renderLayer(Layer.UI);
        renderLayer(Layer.UI_FRONT);
        renderLayer(Layer.TOOLTIP);
        renderLayer(Layer.UI_HOVER);
        renderLayer(Layer.CURSOR);
        showDesignLocationOnScreen(cursorXPosition, cursorYPosition);
    }

    /**
     * set the graphics context to render to
     *
     * @param g: the graphics context
     */
    public void setGraphics(Graphics2D g) {
        this.g = g;
    }

    /**
     * Render a specific layer.
     * Gets all render objects of all RenderComponents that are
     * defined on this layer.
     * @param layer: the layer to render
     */
    private synchronized void renderLayer(Layer layer) {
        for (RenderObject r : collectRenderObjects(layer)) {
            if (r.isHidden()) {
                continue;
            }
            render(r);
        }
    }

    /**
     * Reset / Reinitialize the list with relevant entities that should be considered
     */
    public synchronized void recollectEntities() {
        renderEntities = Query.getEntitiesWithComponent(RenderComponent.class);
    }

    /**
     * Collect all render objects from a layer of all entities in the renderEntities list
     * @param layer: the layer to collect the objects from
     * @return a list with all render objects in the layer
     */
    private ArrayList<RenderObject> collectRenderObjects(Layer layer) {
        ArrayList<RenderObject> renderObjects = new ArrayList<>();
        for (Entity e : renderEntities) {
            renderObjects.addAll(e.getComponent(RenderComponent.class).getObjectsAtLayer(layer));
        }
        return renderObjects;
    }

    /**
     * Method for rendering a render object.
     * Rendering methods are determined based on the subclass of the render object implementation
     * @param r the render object
     */
    private synchronized void render(RenderObject r) {
        if (r instanceof ImageObject i) {
            renderImage(g, i.getImage(),
                    Game.scale().scaleX(i.getLocation().x),
                    Game.scale().scaleY(i.getLocation().y),
                    Game.scale().scaleX(i.getBounds().getBounds().width),
                    Game.scale().scaleY(i.getBounds().getBounds().height));
        } else if (r instanceof ShapeObject s) {
            renderShape(g, Game.scale().scaleShape(s.getBounds()), s.getBorderColor(), s.getFillColor(), s.getThickness());
        } else if (r instanceof LineObject l) {
            renderLine(g,
                    Game.scale().scalePoint(l.getP1()),
                    Game.scale().scalePoint(l.getP2()),
                    l.getColor(), l.getThickness());
        } else if (r instanceof TextObject t) {
            if (t.getHorizontalAlignment() != null && t.getVerticalAlignment() != null) {
                renderText(g, t.getText(), t.getColor(),
                        Game.scale().scaleFont(t.getFont()),
                        Game.scale().scaleX(t.getLocation().x),
                        Game.scale().scaleY(t.getLocation().y),
                        Game.scale().scaleX(t.getBounds().getBounds().width),
                        Game.scale().scaleY(t.getBounds().getBounds().height),
                        t.getHorizontalAlignment(),
                        t.getVerticalAlignment());
            } else {
                renderText(g, t.getText(), t.getColor(),
                        Game.scale().scaleFont(t.getFont()),
                        Game.scale().scaleX(t.getLocation().x),
                        Game.scale().scaleY(t.getLocation().y),
                        Game.scale().scaleX(t.getBounds().getBounds().width),
                        Game.scale().scaleY(t.getBounds().getBounds().height));
            }
        } else if (r instanceof HoverObject h) {
            renderShape(g, Game.scale().scaleShape(h.getBounds()), h.getHoverColor(), h.getHoverColor(), 1);
        } else if (r instanceof AnimationObject a) {
            if(a.isPaused()) {
                if(a.getCurFrame() >= a.getPauseFrames()) {
                    a.setCurFrame(0);
                    a.setPaused(false);
                } else {
                    a.setCurFrame(a.getCurFrame() + 1);
                }
            } else {
                if (a.isRandomize() && randomizer.nextInt(10000) > 9950) {
                    a.setPaused(true);
                } else {
                    renderAnimation(g,
                            a.getAnimation(),
                            Game.scale().scaleX(a.getLocation().x),
                            Game.scale().scaleY(a.getLocation().y),
                            Game.scale().scaleX(a.getBounds().getBounds().width),
                            Game.scale().scaleY(a.getBounds().getBounds().height));
                }
            }
        }
    }

    /**
     * Render the x,y coordinates of the mouse cursor to the screen, for easier graphics design in the future
     */
    private void showDesignLocationOnScreen(int x, int y) {
        if(!Game.config().isDebug()) {
            return;
        }
        renderText(g, "x: " + Game.scale().upscaleX(x), Bit8.RED, FontCollection.bit8FontMedium,
                15, 15, 200, 50);
        renderText(g, "y: " + Game.scale().upscaleX(y), Bit8.RED, FontCollection.bit8FontMedium,
                15, 65, 200, 50);
    }

    /**
     * Set the position of the mouse cursor coordinates that may be rendered (if debug enabled)
     * @param x: x-position of mouse
     * @param y: y-position of mouse
     */
    public void setCursorPosition(int x, int y) {
        this.cursorXPosition = x;
        this.cursorYPosition = y;
    }
}
