#include "stdafx.h"
#include "SolidIntersectionTest.h"

#include "TransformationAdapter.h"
#include "SolidIntersection.h"
#include "Plane.h"
#include "Solid.h"
#include "Auto.h"
#include "Maybe.h"
#include "Line.h"
#include "Intersect.h"

std::string SolidIntersectionTest::Name() const {
	return "SolidIntersection Test";
}

void SolidIntersectionTest::Execute() {
	RenderMemory renderMemory;
	{
		// Two planes facing same direction
		const Auto<const Solid> solidIntersection = 
			MakeSolidIntersection(
				MakeTransformationAdapter(
					MakePlane(),
					IdentityMatrix(),
					Point(0,0,1)
				),
				MakePlane()
			);
		{
			const Intersect00 intersect = solidIntersection->DetermineClosestIntersectionPoint(
				Line(Point(0,0,-1),Point(0,0,1)), eRender, renderMemory);
			AssertEqual("Hits back wall", intersect.Get()->PositionAt(), Point(0,0,1));
		}
		{
			const Intersect00 intersect = solidIntersection->DetermineClosestIntersectionPoint(
				Line(Point(0,0,1),Point(0,0,-1)), eRender, renderMemory);
			AssertEqual("Hits front wall", intersect.Get()->PositionAt(), Point(0,0,1));
		}
	}
	{
		// Two planes facing same direction
		// Same test...changing around A and B
		const Auto<const Solid> solidIntersection = 
			MakeSolidIntersection(
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
				Line(Point(0,0,1),Point(0,0,-1)), eRender, renderMemory);
			AssertEqual("Hits front wall", intersect.Get()->PositionAt(), Point(0,0,1));
		}
	}
	{
		// Two planes facing each other
		const Auto<const Solid> solidIntersection = 
			MakeSolidIntersection(
				MakePlane(),
				MakeTransformationAdapter(
					MakePlane(),
					Matrix(Point(1,0,0),Point(0,-1,0),Point(0,0,-1)),
					Point(0,0,1)
				)
			);
		{
			const Intersect00 intersect = solidIntersection->DetermineClosestIntersectionPoint(
				Line(Point(0,0,-1),Point(0,0,1)), eRender, renderMemory);
			AssertEqual("Hits front wall", intersect.Get()->PositionAt(), Point(0,0,0));
		}
		{
			const Intersect00 intersect = solidIntersection->DetermineClosestIntersectionPoint(
				Line(Point(0,0,1),Point(0,0,-1)), eRender, renderMemory);
			AssertEqual("Hits front wall", intersect.Get()->PositionAt(), Point(0,0,1));
		}
	}
	{
		// Two planes facing opposite directions
		const Auto<const Solid> solidIntersection = 
			MakeSolidIntersection(
				MakePlane(),
				MakeTransformationAdapter(
					MakePlane(),
					Matrix(Point(1,0,0),Point(0,-1,0),Point(0,0,-1)),
					Point(0,0,-1)
				)
			);
		{
			const Intersect00 intersect = solidIntersection->DetermineClosestIntersectionPoint(
				Line(Point(0,0,-1),Point(0,0,1)), eRender, renderMemory);
			Assert("Hits nothing", !intersect.IsValid());
		}
		{
			const Intersect00 intersect = solidIntersection->DetermineClosestIntersectionPoint(
				Line(Point(0,0,1),Point(0,0,-1)), eRender, renderMemory);
			Assert("Hits nothing", !intersect.IsValid());
		}
	}
	{
		// Two planes perpendicular
		const Auto<const Solid> solidIntersection = 
			MakeSolidIntersection(
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
			AssertEqual("Hits wall", intersect.Get()->PositionAt(), Point(0,1,0));
		}
	}
}
