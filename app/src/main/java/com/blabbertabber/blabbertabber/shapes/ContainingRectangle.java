package com.blabbertabber.blabbertabber.shapes;

import com.blabbertabber.blabbertabber.ShapePair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;

/**
 * Created by brendancunnie on 5/22/16.
 */
public class ContainingRectangle {
    private Box2D box;

    public ContainingRectangle(Box2D box) {
        this.box = box;
    }

    public Collection<Stack<Circle2D>> placeFirstCircle(List<Double> radii) {
        if (radii.size() == 0) {
            return new ArrayList<Stack<Circle2D>>(0);
        }
        Stack<ShapePair> shapePairs = new Stack<>();
        List<Line> lines = ShapeFactory.makeLines(box);
        Line lastLine = lines.get(3);
        for (Line line : lines) {
            shapePairs.push(new ShapePair(lastLine, line));
            lastLine = line;
        }
        double firstRadius = radii.remove(0);
        Circle2D firstCircle = new Circle2D(firstRadius, firstRadius, firstRadius);
        Circle firstCircleShape = ShapeFactory.makeCircle(firstCircle);
        shapePairs.push(new ShapePair(firstCircleShape, lines.get(0))); // bottom line
        shapePairs.push(new ShapePair(firstCircleShape, lines.get(3))); // left line
        ArrayList<Stack<Circle2D>> solutions = new ArrayList<>();
        Stack<Circle2D> partialSolution = new Stack<>();
        partialSolution.push(firstCircle);
        PlaceRemainingCircles(solutions, partialSolution, shapePairs, radii);
        return solutions;
    }

    private void PlaceRemainingCircles(ArrayList<Stack<Circle2D>> solutions, Stack<Circle2D> partialSolution, List<ShapePair> shapePairs, List<Double> remainingRadii) {
        if (remainingRadii.size() == 0) {
            solutions.add((Stack<Circle2D>) partialSolution.clone());
            return;
        }
        for (ShapePair shapePair : shapePairs) {
            for (double radius : remainingRadii) {
                for (Point2D point2d : shapePair.positionsForNewCircle(radius)) {
                    Circle2D newCircle = new Circle2D(point2d, radius);
                    if (!fitsInCorner(newCircle, partialSolution)) {
                        break;
                    }
                    List<Double> remainingRadiiCopy = new ArrayList<>(remainingRadii);
                    remainingRadiiCopy.remove(radius);
                    partialSolution.push(newCircle);
                    List<ShapePair> newShapePairs = new ArrayList<>(shapePair.newShapePairs(new Circle(newCircle)));
                    newShapePairs.addAll(shapePairs);
                    PlaceRemainingCircles(solutions, partialSolution, newShapePairs, remainingRadiiCopy);
                    partialSolution.pop();
                }
            }
        }
    }

    private boolean fitsInCorner(Circle2D newCircle, Stack<Circle2D> partialSolution) {
        if (!this.box.containsBounds(newCircle)) {
            return false;
        }
        for (Circle2D circle : partialSolution) {
            if (newCircle.contains(circle.center()) || newCircle.intersections(circle).size() > 0) {
                return false;
            }
        }
        return true;
    }
}
