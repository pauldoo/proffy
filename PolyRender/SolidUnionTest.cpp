#include "stdafx.h"
#include "SolidUnionTest.h"

#include "SolidUnion.h"
#include "Sphere.h"
#include "Solid.h"
#include "TransformationAdapter.h"
#include "Maybe.h"
#include "Line.h"
#include "Intersect.h"
#include "Auto.h"

std::string SolidUnionTest::Name() const {
	return "SolidUnion Test";
}

void SolidUnionTest::Execute() {
	{
		// Two spheres a distance apart
		const Auto<const Solid> solidUnion = 
			MakeSolidUnion(
				MakeTransformationAdapter(
					MakeSphere(1),
					IdentityMatrix(),
					Point(0,-2,0)
				),
				MakeTransformationAdapter(
					MakeSphere(1),
					IdentityMatrix(),
					Point(0,2,0)
				)
			);

		RenderMemory renderMemory;
		{
			const Intersect00 intersect = solidUnion->DetermineClosestIntersectionPoint(
				Line(Point(0,-2,-2),Point(0,0,1)),eRender,renderMemory);
			AssertEqual("Hits left sphere", intersect.Get()->PositionAt(), Point(0,-2,-1));
		}
		{
			const Intersect00 intersect = solidUnion->DetermineClosestIntersectionPoint(
				Line(Point(0,2,-2),Point(0,0,1)),eRender,renderMemory);
			AssertEqual("Hits right sphere", intersect.Get()->PositionAt(), Point(0,2,-1));
		}
		{
			const Intersect00 intersect = solidUnion->DetermineClosestIntersectionPoint(
				Line(Point(0,-4,0),Point(0,1,0)),eRender,renderMemory);
			AssertEqual("Hits first sphere", intersect.Get()->PositionAt(), Point(0,-3,0));
		}
		Assert("InsideQ Works", solidUnion->InsideQ(Point(0,2,0)));
		Assert("InsideQ Works", solidUnion->InsideQ(Point(0,-2,0)));
	}
}
