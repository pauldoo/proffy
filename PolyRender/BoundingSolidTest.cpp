#include "stdafx.h"
#include "BoundingSolidTest.h"

#include "Auto.h"
#include "Solid.h"
#include "BoundingSolid.h"
#include "Plane.h"
#include "TransformationAdapter.h"
#include "Maybe.h"
#include "Line.h"
#include "Intersect.h"
#include "Exception.h"

std::string BoundingSolidTest::Name() const {
	return "BoundingSolid Test";
}

void BoundingSolidTest::Execute() {
	RenderMemory renderMemory;
	{
		// Two planes facing same direction
		const Auto<const Solid> solidIntersection = 
			MakeBoundingSolid(
				MakePlane(),
				MakeTransformationAdapter(
					MakePlane(),
					IdentityMatrix(),
					Point(0,0,1)
				)
			);
		{
			const Intersect00 intersect = solidIntersection->DetermineClosestIntersectionPoint(
				Line(Point(0,0,-1),Point(0,0,1)), eRender, renderMemory);
			AssertEqual("Hits back wall", intersect.Get()->PositionAt(), Point(0,0,1));
		}
		{
			const Intersect00 intersect = solidIntersection->DetermineClosestIntersectionPoint(
				Line(Point(0,0,.5),Point(0,0,1)), eRender, renderMemory);
			AssertEqual("Hits the plane from inside bound", intersect.Get()->PositionAt(), Point(0,0,1));
		}
	}
	{
		// Two planes facing same direction
		// This time bound doesen't contain the solid ... opps
		const Auto<const Solid> solidIntersection = 
			MakeBoundingSolid(
				MakeTransformationAdapter(
					MakePlane(),
					IdentityMatrix(),
					Point(0,0,1)
				),
				MakePlane()
			);
		THROWCHECK("", solidIntersection->DetermineClosestIntersectionPoint(
			Line(Point(0,0,-1),Point(0,0,1)), eRender, renderMemory));
	}
	{
		// Two planes perpendicular
		const Auto<const Solid> solidIntersection = 
			MakeBoundingSolid(
				MakePlane(),
				MakeTransformationAdapter(
					MakePlane(),
					Matrix(Point(1,0,0),Point(0,0,1),Point(0,1,0)),
					Point()
				)
			);
		{
			const Intersect00 intersect = solidIntersection->DetermineClosestIntersectionPoint(
				Line(Point(0,1,-1),Point(0,0,1)), eRender, renderMemory);
			Assert("Hits nothing", !intersect.IsValid());
		}
	}
}
