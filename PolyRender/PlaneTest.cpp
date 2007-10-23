#include "stdafx.h"
#include "PlaneTest.h"

#include "Plane.h"
#include "Solid.h"
#include "Exception.h"
#include "Line.h"
#include "Maybe.h"
#include "Auto.h"
#include "Intersect.h"

std::string PlaneTest::Name() const {
    return "Plane Test";
}

void PlaneTest::Execute() {
	RenderMemory renderMemory;
	const Auto<const Solid> plane = MakePlane();
	AssertEqual("Normal pointing in the right direction", 
		plane->DetermineClosestIntersectionPoint(
			Line(Point(0,0,-1), Point(0,0,1)), eRender, renderMemory).Get()->NormalAt(), 
		Point(0,0,1)
	);
	AssertEqual("Normal pointing in the right direction", 
		plane->DetermineClosestIntersectionPoint(
			Line(Point(1,2,-3), Point(0,0,1)), eRender, renderMemory).Get()->UVCoordsAt(),
		Point(1,2,0)
	);
	{
		AssertEqual("Distance to the plane should be 10", 
			plane->DetermineClosestIntersectionPoint(
				Line(Point(0,0,-10),Point(0,1,1)), eRender, renderMemory).Get()->DistanceAt(),
			10
		);
	}
	Assert("Point is inside", plane->InsideQ(Point(0,0,2)));
	Assert("Point is outside", !plane->InsideQ(Point(0,0,-2)));
}
