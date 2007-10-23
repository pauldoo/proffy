#include "stdafx.h"
#include "TransformationAdapterTest.h"

#include "TransformationAdapter.h"
#include "Line.h"
#include "Intersect.h"
#include "Maybe.h"
#include "DummySolid.h"
#include "Plane.h"
#include "Auto.h"
#include "Solid.h"

std::string TransformationAdapterTest::Name() const {
	return "TransformationAdapter Test";
}

void TransformationAdapterTest::Execute() 
{
	RenderMemory renderMemory;

	const Line dummyLight(Point(1,2,3), Point());
	const Auto<const Solid> dummySolid = MakeDummySolid(Point(1,2,3), Point(0,0,1));
	// Simple case
	{
		const Auto<const Solid> trans = MakeTransformationAdapter(dummySolid, IdentityMatrix(), Point());
		const Auto<const Intersect> intersect = trans->DetermineClosestIntersectionPoint(
			dummyLight, eRender, renderMemory).Get();
		AssertEqual("Identity TransformationAdapter does nothing to position", intersect->PositionAt(), Point(1,2,3));
		AssertEqual("Identity TransformationAdapter does nothing to normal", intersect->NormalAt(), Point(0,0,1));
	}
	// Translated by (0,0,1)
	{
		const Auto<const Solid> trans = 
			MakeTransformationAdapter(dummySolid, IdentityMatrix(), Point(0,0,1));
		const Auto<const Intersect> intersect = trans->DetermineClosestIntersectionPoint(
			dummyLight, eRender, renderMemory).Get();
		AssertEqual("TransformationAdapter moves position", intersect->PositionAt(), Point(1,2,4));
		AssertEqual("TransformationAdapter does nothing to normal", intersect->NormalAt(), Point(0,0,1));
	}
	// Rotated from XY to XZ
	{
		const Auto<const Solid> trans = 
			MakeTransformationAdapter(dummySolid, Matrix(Point(1,0,0), Point(0,0,1), Point(0,1,0)), Point());
		const Auto<const Intersect> intersect = trans->DetermineClosestIntersectionPoint(
			dummyLight, eRender, renderMemory).Get();
		AssertEqual("TransformationAdapter moves position", intersect->PositionAt(), Point(1,3,2));
		AssertEqual("TransformationAdapter rotates normal", intersect->NormalAt(), Point(0,1,0));
	}
	// Now both together
	{
		const Auto<const Solid> trans = 
			MakeTransformationAdapter(dummySolid, Matrix(Point(1,0,0), Point(0,0,1), Point(0,1,0)), Point(0,0,1));
		const Auto<const Intersect> intersect = trans->DetermineClosestIntersectionPoint(
			dummyLight, eRender, renderMemory).Get();
		AssertEqual("TransformationAdapter moves position", intersect->PositionAt(), Point(1,3,3));
		AssertEqual("TransformationAdapter rotates normal", intersect->NormalAt(), Point(0,1,0));
	}
	// Now Lets test 30 degree rotation around X
	{
		const double sin30 = 1/2.;
		const double cos30 = sqrt(3.)/2;
		const Auto<const Solid> trans = 
			MakeTransformationAdapter(
				MakeDummySolid(Point(0,0,2), Point(0,0,1)),
				Matrix(Point(1,0,0), Point(0,cos30,-sin30), Point(0,sin30,cos30)), 
				Point()
			);
		const Auto<const Intersect> intersect = trans->DetermineClosestIntersectionPoint(
			dummyLight, eRender, renderMemory).Get();
		AssertEqual("TransformationAdapter moves position", intersect->PositionAt(), 2*Point(0,-sin30,cos30));
		AssertEqual("TransformationAdapter rotates normal", intersect->NormalAt(), Point(0,-sin30,cos30));
	}
	// InsideQ works
	{
		const Auto<const Solid> plane = MakeTransformationAdapter(
			MakePlane(),
			IdentityMatrix(),
			Point(0,0,10)
		);
		Assert("Should be inside", plane->InsideQ(Point(0,0,11)));
		Assert("Shouldn't be inside", !plane->InsideQ(Point(0,0,9)));
	}
}
