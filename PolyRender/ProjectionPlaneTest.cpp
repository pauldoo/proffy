#include "stdafx.h"
#include "ProjectionPlaneTest.h"

#include "Utilities.h"
#include "GlobalWindowSize.h"
#include "ProjectionPlane.h"
#include "Exception.h"
#include "Polynomial.h"
#include "Line.h"
#include "LineFuncs.h"

namespace {
	ParametricLine ProjectFromCorners(const ProjectionPlane& projectionPlane) {
		return 
			LineFuncs::ToParametricLine(projectionPlane.ProjectFromPixel(0,0)) +
			LineFuncs::ToParametricLine(projectionPlane.ProjectFromPixel(drawing_pane_width,0)) +
			LineFuncs::ToParametricLine(projectionPlane.ProjectFromPixel(0,drawing_pane_height)) + 
			LineFuncs::ToParametricLine(projectionPlane.ProjectFromPixel(drawing_pane_width,drawing_pane_height));
	}
}

std::string ProjectionPlaneTest::Name() const {
	return "Projection Plane Test";
}

void ProjectionPlaneTest::Execute() {
	
	THROWCHECK("Projection Plane Displacement Cannot be 0", ProjectionPlane(Point(0,0,0),Point(0,0,0)));
	
	const RPolynomial t = RPolynomial::MakeT();
	AssertNearlyEqual("Sum of the lines projected from 4-corners should point straight ahead", 
		ProjectFromCorners(ProjectionPlane()), 
		ParametricLine(Point(0,0,4))*t);
	{		
		const Point projectionPoint(RandomPoint());
		const Point projectionDirection(RandomPoint());

		const ProjectionPlane projectionPlane(
			projectionPoint,
			projectionDirection,
			Random<Real>(),
			Random<Real>()
		);

		AssertNearlyEqual("Sum of the lines projected from 4-corners should point straight ahead, regardless of parameters",
			t*ParametricLine(projectionDirection) + ParametricLine(projectionPoint), (1/4.0)*ProjectFromCorners(projectionPlane));
	}
	{
		const ProjectionPlane projectionPlane;
		const Point topLeftCorner = LineFuncs::EvaluateAt(ProjectionPlane().ProjectFromPixel(0,0), 1);
		Assert("Default ProjectionPlane faces the right direction (up & down)"   , topLeftCorner.Y() > 0);			
		Assert("Default ProjectionPlane faces the right direction (left & right)", topLeftCorner.X() < 0);
	}
}


