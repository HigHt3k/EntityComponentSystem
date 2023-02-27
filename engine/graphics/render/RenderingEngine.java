package engine.graphics.render;

import engine.Game;
import engine.ecs.Query;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.*;
import engine.ecs.entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * The Rendering Engine is the backbone of the graphics generation. It contains different methods which access sub-rendering engines
 * implemented for a special purpose each:
 * TextRenderer: renders Strings with formatting
 * ShapeRenderer: renders different shapes with given color and size
 * ImageRenderer: renders a buffered image at a position and size
 */
public class RenderingEngine {
    private static final float scaleW = Game.config().renderConfiguration().getScaleWidth();
    private static final float scaleH = Game.config().renderConfiguration().getScaleHeight();
    public static final int MARGIN = (int) (15 * scaleW); //px

    private ArrayList<Entity> renderEntities;

    private Graphics2D g;

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
    public static void renderText(Graphics2D g, String text, Color color, Font font, int x, int y, int width, int height,
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
    public static void renderText(Graphics2D g, String text, Color color, Font font, int x, int y, int width, int height) {
        if (text == null) {
            return;
        }
        if (text.contains("@")) {
            String temp = text;
            int id = Integer.parseInt(temp.replace("@", ""));
            text = Game.res().language().getStringValue(id, Game.config().getLanguage());
        }

        if (text.contains("\n")) {
            TextRenderer.render(g, text, color, font, new Point(x + MARGIN, y));
            return;
        }

        text = text.trim().replaceAll(" +", " ");
        text = text.replaceAll("\n ", "\n");

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                Game.config().renderConfiguration().getAliasingText());

        g.setFont(font);

        int maxWidth = width - 2 * MARGIN;

        // reformat the string
        StringBuilder tempLine = new StringBuilder();
        String tempWord = "";
        StringBuilder newText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            tempWord += text.charAt(i);
            if (Character.isWhitespace(text.charAt(i)) || Character.isSpaceChar(text.charAt(i))) {
                if (g.getFontMetrics().stringWidth(String.valueOf(tempLine)) + g.getFontMetrics().stringWidth(tempWord) > maxWidth) {
                    newText.append(tempLine).append("\n");
                    tempLine = new StringBuilder();
                } else {
                    tempLine.append(tempWord);
                    tempWord = "";
                }
            }
        }

        if (g.getFontMetrics().stringWidth(String.valueOf(tempLine)) + g.getFontMetrics().stringWidth(tempWord) > maxWidth) {
            newText.append(tempLine).append("\n").append(tempWord);
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
    public static void renderShape(Graphics2D g, Shape shape, Color borderColor, Color fillColor, int thickness) {
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
    public static void renderImage(Graphics2D g, BufferedImage img, int x, int y, int width, int height) {
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
    public static void renderLine(Graphics2D g, Point p1, Point p2, Color color, int thickness) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                Game.config().renderConfiguration().getAntialiasing());

        LineRenderer.render(g, p1, p2, color, thickness);
    }

    public static void renderAnimation(Graphics2D g, Image animation, int x, int y, int width, int height) {
        AnimationRenderer.renderAnimation(g, animation, x, y, width, height);
    }

    /**
     * Collects all entities and detects if they have a @{@link RenderComponent} which should be rendered
     */
    public void collectAndRenderEntities() {
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
    }

    /**
     * set the graphics context to render to
     *
     * @param g: the graphics context
     */
    public void setGraphics(Graphics2D g) {
        this.g = g;
    }

    private void renderLayer(Layer layer) {
        for (RenderObject r : collectRenderObjects(layer)) {
            if (r.isHidden()) {
                continue;
            }
            render(r);
        }
    }

    public void recollectEntities() {
        renderEntities = Query.getEntitiesWithComponent(RenderComponent.class);
    }

    private ArrayList<RenderObject> collectRenderObjects(Layer layer) {
        ArrayList<RenderObject> renderObjects = new ArrayList<>();
        for (Entity e : renderEntities) {
            renderObjects.addAll(e.getComponent(RenderComponent.class).getObjectsAtLayer(layer));
        }
        return renderObjects;
    }

    private void render(RenderObject r) {
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
        }
    }
}
