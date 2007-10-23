#include "stdafx.h"
#include "ProjectionPlane.h"
#include "Utilities.h"
#include "Exception.h"
#include "Line.h"
#include "GlobalWindowSize.h"

ProjectionPlane::ProjectionPlane(
	const Point& projectionPoint,
	const Point& projectionPlaneDisplacement,
	const Real zoomLevel,
	const Real angleToHorizontal) 
 :	m_rotationMatrix(RotationMatrix(projectionPlaneDisplacement,angleToHorizontal)),
    m_projectionPoint(projectionPoint), 
	m_projectionPlaneDisplacement(projectionPlaneDisplacement),
	m_zoomLevel(zoomLevel), 
	m_angleToHorizontal(angleToHorizontal)
{
}	

Line ProjectionPlane::ProjectFromPixel(const int i, const int j) const {
	// First Convert the Screen coordinates to the coordinates on the projection plane.
	const Real normFactor = m_zoomLevel * std::max(drawing_pane_height,drawing_pane_height);
	const Point planeCoords = (Point(i,-j,0) + Point(-drawing_pane_width,drawing_pane_height,0)/2)/normFactor;

	return Line(m_projectionPoint, MatrixProd(m_rotationMatrix,planeCoords) + m_projectionPlaneDisplacement);
}

Point ProjectionPlane::ProjectionPoint() const {
	return m_projectionPoint;
}
