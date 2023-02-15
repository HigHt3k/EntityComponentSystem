package engine.graphics.render;

import engine.Game;
import engine.ecs.Query;
import engine.ecs.component.graphics.GraphicsObject;
import engine.ecs.component.graphics.GraphicsObjectType;
import engine.ecs.entity.Entity;
import engine.ecs.component.graphics.GraphicsComponent;

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

    private Graphics2D g;

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

    public static void renderLine(Graphics2D g, Point p1, Point p2, Color color, int thickness) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                Game.config().renderConfiguration().getAntialiasing());

        LineRenderer.render(g, p1, p2, color, thickness);
    }

    /**
     * Collects all entities and detects if they have a @{@link GraphicsComponent} which should be rendered
     */
    public void collectAndRenderEntities() {
        ArrayList<Entity> entities = Query.getEntitiesWithComponent(GraphicsComponent.class);

        Collections.sort(entities, (o1, o2) -> {
            if (o1.getComponent(GraphicsComponent.class).getLayer() == o2.getComponent(GraphicsComponent.class).getLayer())
                return 0;
            else if (o1.getComponent(GraphicsComponent.class).getLayer() < o2.getComponent(GraphicsComponent.class).getLayer()) {
                return -1;
            } else {
                return 1;
            }
        });

        for (Entity e : entities) {
            renderImages(e);
            renderLines(e);
            renderShapes(e);
            renderTexts(e);
            renderHovered(e);
            renderToolTips(e);
            renderGraphicObjects(e);
        }
    }

    public void renderGraphicObjects(Entity e) {

        GraphicsComponent gc = e.getComponent(GraphicsComponent.class);

        Collections.sort(gc.getGraphicsObjects(), (o1, o2) -> {
            if (o1.getLayer() == o2.getLayer())
                return 0;
            else if (o1.getLayer() < o2.getLayer()) {
                return -1;
            } else {
                return 1;
            }
        });

        for (GraphicsObject go : gc.getGraphicsObjects()) {
            if (go.getType() == GraphicsObjectType.SHAPE) {
                renderShape(g, go.getShape(), go.getBorderColor(), go.getColor(), 1);
                if (go.isHovered()) {
                    renderShape(g, go.getShape(), go.getHoverColor(), go.getHoverColor(), 1);
                }
            } else if (go.getType() == GraphicsObjectType.TEXT) {
                renderText(g, go.getText(), go.getColor(), go.getFont(),
                        go.getShape().getBounds().x, go.getShape().getBounds().y,
                        go.getShape().getBounds().width, go.getShape().getBounds().height);
            } else if (go.getType() == GraphicsObjectType.IMAGE) {
                renderImage(g, go.getImage(),
                        go.getShape().getBounds().x, go.getShape().getBounds().y,
                        go.getShape().getBounds().width, go.getShape().getBounds().height);
            }
        }

    }

    /**
     * set the graphics context to render to
     *
     * @param g: the graphics context
     */
    public void setGraphics(Graphics2D g) {
        this.g = g;
    }

    private void renderLines(Entity e) {

        GraphicsComponent gc = e.getComponent(GraphicsComponent.class);
        if (gc != null) {
            if (gc.getLineStart() != null && gc.getLineEnd() != null) {
                renderLine(g,
                        gc.getLineStart(),
                        gc.getLineEnd(),
                        gc.getLineColor(),
                        gc.getThickness()
                );
            }

        }
    }

    private void renderShapes(Entity e) {

        GraphicsComponent gc = e.getComponent(GraphicsComponent.class);
        if (gc != null) {
            // render based on the given content of the component
            if (gc.getShape() != null) {
                renderShape(
                        g,
                        gc.getShape(),
                        gc.getBorderColor(),
                        gc.getFillColor(),
                        gc.getThickness()
                );
            }

            if (gc.getShapes() != null) {
                for (Shape s : gc.getShapes()) {
                    renderShape(
                            g,
                            s,
                            gc.getBorderColor(),
                            gc.getFillColor(),
                            gc.getThickness()
                    );
                }

            }
        }
    }

    private void renderImages(Entity e) {

        GraphicsComponent gc = e.getComponent(GraphicsComponent.class);
        if (gc != null) {
            // render based on the given content of the component
            if (gc.getImage() != null) {
                renderImage(
                        g,
                        gc.getImage(),
                        gc.getBounds().x,
                        gc.getBounds().y,
                        gc.getBounds().width,
                        gc.getBounds().height
                );
            }
        }

    }

    private void renderTexts(Entity e) {

        GraphicsComponent gc = e.getComponent(GraphicsComponent.class);
        if (gc != null) {
            if (!gc.getTexts().isEmpty() && !gc.getLocations().isEmpty()) {
                for (int i = 0; i < gc.getTexts().size(); i++) {
                    renderText(
                            g,
                            gc.getTexts().get(i),
                            gc.getTextColor(),
                            gc.getFont(),
                            (int) gc.getLocations().get(i).getX(),
                            (int) gc.getLocations().get(i).getY(),
                            gc.getBounds().width,
                            gc.getBounds().height
                    );
                }

            }
        }
    }

    private void renderToolTips(Entity e) {
        GraphicsComponent gc = e.getComponent(GraphicsComponent.class);
        if (gc != null) {
            if (gc.isHovered()) {
                if (gc.getToolTip() != null) {
                    renderShape(
                            g,
                            new Rectangle(
                                    gc.getBounds().x + 80,
                                    gc.getBounds().y + 50,
                                    200,
                                    150
                            ),
                            gc.getToolTip().getBorderColor(),
                            gc.getToolTip().getToolTipColor(),
                            gc.getThickness()
                    );
                    renderText(
                            g,
                            gc.getToolTip().getText(),
                            gc.getToolTip().getTextColor(),
                            gc.getToolTip().getFont(),
                            gc.getBounds().x + 80,
                            gc.getBounds().y + 50,
                            200,
                            150
                    );
                }
            }
        }

    }

    private void renderHovered(Entity e) {

        GraphicsComponent gc = e.getComponent(GraphicsComponent.class);
        if (gc != null) {
            if (gc.isHovered()) {
                if (gc.getHoverColor() != null) {
                    renderShape(
                            g,
                            gc.getBounds(),
                            gc.getHoverColor(),
                            gc.getHoverColor(),
                            gc.getThickness()
                    );
                }
            }
        }
    }
}
