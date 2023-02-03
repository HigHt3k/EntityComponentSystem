package com.graphics.render;

import com.Game;
import com.ecs.component.object.GraphicsObject;
import com.ecs.component.object.GraphicsObjectType;
import com.ecs.entity.Entity;
import com.ecs.component.GraphicsComponent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.regex.PatternSyntaxException;

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
    public static final int MARGIN = (int) (50 * scaleW); //px

    private Graphics2D g;

    public static void renderText(Graphics2D g, String text, Color color, Font font, int x, int y, int width, int height) {
        if(text.contains("@")) {
            String temp = text;
            int id = Integer.parseInt(temp.replace("@", ""));
            text = Game.res().language().getStringValue(id, Game.config().getLanguage());
        }

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                Game.config().renderConfiguration().getAliasingText());

        int textWidth = g.getFontMetrics().stringWidth(text);
        int widthCharacter = g.getFontMetrics().stringWidth("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789") / 36;
        int maxWidth = width - 2 * MARGIN;
        // detect the longest word in string and set the character per line size to maximum minus longest word.
        int charactersPerLine = maxWidth / widthCharacter - Objects.requireNonNull(Arrays.stream(text.split(" "))
                .max(Comparator.comparingInt(String::length))
                .orElse(null)).length();

        String regex = "(\\w{1,}.{1," + charactersPerLine + "})(\\s+|\\.)";
        try {
            if (textWidth > maxWidth) {
                text = text.replaceAll(regex, "$1\n");
            }
        } catch(PatternSyntaxException ex) {
            // HANDLE
        }
        TextRenderer.render(g, text, color, font, new Point(x + MARGIN,y));
    }

    /**
     * Render a shape (e.g. Rectangle, Ellipse2D, ...) to the graphics context. Can also be filled.
     * @param g: graphics context
     * @param shape Shape to render, e.g. Rectangle or Ellipse2D
     * @param borderColor: Color to draw border
     * @param fillColor: fill color, if null then not filled
     */
    public static void renderShape(Graphics2D g, Shape shape, Color borderColor, Color fillColor) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                Game.config().renderConfiguration().getAntialiasing());

        ShapeRenderer.render(g, shape, borderColor, fillColor);
    }

    /**
     * Render a Buffered Image to the screen at a given position and size in the graphics context.
     * @param g: Graphics2D context
     * @param img: Image to render
     * @param x: position x
     * @param y: position y
     * @param width: size in x direction (px)
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
        ArrayList<Entity> entities = (ArrayList<Entity>) Game.scene().current().getEntities().clone();

        renderImages(entities);
        renderShapes(entities);
        renderLines(entities);
        renderTexts(entities);
        renderHovered(entities);
        renderToolTips(entities);

        renderGraphicObjects(entities);
    }

    public void renderGraphicObjects(ArrayList<Entity> entities) {
        for(Entity e : entities) {
            GraphicsComponent gc = e.getComponent(GraphicsComponent.class);
            if(gc == null) {
                continue;
            }

            Collections.sort(gc.getGraphicsObjects(), new Comparator<GraphicsObject>() {
                @Override
                public int compare(GraphicsObject o1, GraphicsObject o2) {
                    if(o1.getLayer() == o2.getLayer())
                        return 0;
                    else if(o1.getLayer() < o2.getLayer()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });

            for(GraphicsObject go : gc.getGraphicsObjects()) {
                if(go.getType() == GraphicsObjectType.SHAPE) {
                    renderShape(g, go.getShape(), go.getBorderColor(), go.getColor());
                    if(go.isHovered()) {
                        renderShape(g, go.getShape(), go.getHoverColor(), go.getHoverColor());
                    }
                }
                else if(go.getType() == GraphicsObjectType.TEXT) {
                    renderText(g, go.getText(), go.getColor(), go.getFont(),
                            go.getShape().getBounds().x, go.getShape().getBounds().y,
                            go.getShape().getBounds().width, go.getShape().getBounds().height);
                }
                else if(go.getType() == GraphicsObjectType.IMAGE) {
                    renderImage(g, go.getImage(),
                            go.getShape().getBounds().x, go.getShape().getBounds().y,
                            go.getShape().getBounds().width, go.getShape().getBounds().height);
                }
            }
        }
    }

    /**
     * set the graphics context to render to
     * @param g: the graphics context
     */
    public void setGraphics(Graphics2D g) {
        this.g = g;
    }

    private void renderLines(ArrayList<Entity> entities) {
        for(Entity e : entities) {
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
    }

    private void renderShapes(ArrayList<Entity> entities) {
        for(Entity e : entities) {
            GraphicsComponent gc = e.getComponent(GraphicsComponent.class);
            if (gc != null) {
                // render based on the given content of the component
                if (gc.getShape() != null) {
                    renderShape(
                            g,
                            gc.getShape(),
                            gc.getBorderColor(),
                            gc.getFillColor()
                    );
                }

                if(gc.getShapes() != null) {
                    for(Shape s : gc.getShapes()) {
                        renderShape(
                                g,
                                s,
                                gc.getBorderColor(),
                                gc.getFillColor()
                        );
                    }
                }
            }
        }
    }

    private void renderImages(ArrayList<Entity> entities) {
        for(Entity e : entities) {
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
    }

    private void renderTexts(ArrayList<Entity> entities) {
        for(Entity e : entities) {
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
    }

    private void renderToolTips(ArrayList<Entity> entities) {
        for(Entity e : entities) {
            GraphicsComponent gc = e.getComponent(GraphicsComponent.class);
            if(gc != null) {
                if(gc.isHovered() ) {
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
                                gc.getToolTip().getToolTipColor()
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
    }

    private void renderHovered(ArrayList<Entity> entities) {
        for(Entity e : entities) {
            GraphicsComponent gc = e.getComponent(GraphicsComponent.class);
            if (gc != null) {
                if (gc.isHovered()) {
                    if (gc.getHoverColor() != null) {
                        renderShape(
                                g,
                                gc.getBounds(),
                                gc.getHoverColor(),
                                gc.getHoverColor()
                        );
                    }
                }
            }
        }
    }
}
