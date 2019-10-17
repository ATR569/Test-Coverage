package example;

import target.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Assertions;

public class TriangleTest { 
	 
    @Test 
    public void equilateralTriangleHaveEqualSides() throws Exception { 
        Triangle triangle = new Triangle(2, 2, 2); 
        assertEquals(TriangleKind.EQUILATERAL, triangle.getKind()); 
    } 
 
    @Test 
    public void largerEquilateralTrianglesAlsoHaveEqualSides() throws Exception { 
        Triangle triangle = new Triangle(10, 10, 10); 
 
        assertEquals(TriangleKind.EQUILATERAL, triangle.getKind()); 
    } 
 
    @Test 
    public void isoscelesTrianglesHaveLastTwoSidesEqual() throws Exception { 
        Triangle triangle = new Triangle(3, 4, 4); 
 
        assertEquals(TriangleKind.ISOSCELES, triangle.getKind()); 
    } 
 
    @Test 
    public void isoscelesTrianglesHaveFirstAndLastSidesEqual() throws Exception { 
        Triangle triangle = new Triangle(4, 3, 4); 
 
        assertEquals(TriangleKind.ISOSCELES, triangle.getKind()); 
    } 
 
    @Test 
    public void isoscelesTrianglesHaveTwoFirstSidesEqual() throws Exception { 
        Triangle triangle = new Triangle(4, 4, 3); 
 
        assertEquals(TriangleKind.ISOSCELES, triangle.getKind()); 
    } 
 
    @Test 
    public void isoscelesTrianglesHaveInFactExactlyTwoSidesEqual() throws Exception { 
        Triangle triangle = new Triangle(10, 10, 2); 
 
        assertEquals(TriangleKind.ISOSCELES, triangle.getKind()); 
    } 
 
    @Test 
    public void scaleneTrianglesHaveNoEqualSides() throws Exception { 
        Triangle triangle = new Triangle(3, 4, 5); 
 
        assertEquals(TriangleKind.SCALENE, triangle.getKind()); 
    } 
 
    @Test 
    public void scaleneTrianglesHaveNoEqualSidesAtLargerScaleEither() throws Exception { 
        Triangle triangle = new Triangle(10, 11, 12); 
 
        assertEquals(TriangleKind.SCALENE, triangle.getKind()); 
    } 
 
    @Test 
    public void scaleneTrianglesHaveNoEqualSidesInDescendingOrderEither() throws Exception { 
        Triangle triangle = new Triangle(5, 4, 2); 
 
        assertEquals(TriangleKind.SCALENE, triangle.getKind()); 
    } 
 
    @Test 
    public void verySmallTrianglesAreLegal() throws Exception { 
        Triangle triangle = new Triangle(0.4, 0.6, 0.3); 
 
        assertEquals(TriangleKind.SCALENE, triangle.getKind()); 
    } 
    
    
    @Test 
    public void trianglesWithNoSizeAreIllegal() throws TriangleException { 
    	 Assertions.assertThrows(TriangleException.class, () -> new Triangle(0, 0, 0)); 
    } 
 
    @Test 
    public void trianglesWithNegativeSidesAreIllegal() throws Exception { 
    	Assertions.assertThrows(TriangleException.class, () -> new Triangle(3, 4, -5));
    } 
 
    @Test 
    public void trianglesViolatingTriangleInequalityAreIllegal() throws Exception {
    	Assertions.assertThrows(TriangleException.class, () -> new Triangle(1, 1, 3));
    } 
 
    @Test 
    public void trianglesViolatingTriangleInequalityAreIllegal2() { 
    	Assertions.assertThrows(TriangleException.class, () -> new Triangle(2, 4, 2));
    } 
 
    @Test 
    public void trianglesViolatingTriangleInequalityAreIllegal3() { 
    	Assertions.assertThrows(TriangleException.class, () -> new Triangle(7, 3, 2)); 
    } 
 
    
}