#pragma once
#include "Point.h"
#include "Matrix.h"

class Line;

class ProjectionPlane {
public:
	ProjectionPlane(const Point &projectionPoint = Point(0,0,0), 
					const Point &projectionPlaneDisplacement = Point(0,0,1),
					const Real zoomLevel = 1,
					const Real angleToHorizontal = 0);

	Line ProjectFromPixel(int i, int j) const;
	Point ProjectionPoint() const;

private:
	const Matrix m_rotationMatrix;
	const Point m_projectionPoint, m_projectionPlaneDisplacement;
	const Real m_zoomLevel, m_angleToHorizontal;
};
