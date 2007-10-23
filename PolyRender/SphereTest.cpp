#include "stdafx.h"
#include "SphereTest.h"
#include "Point.h"
#include "Sphere.h"
#include "Solid.h"
#include "Auto.h"
#include "Line.h"
#include "Maybe.h"
#include "Intersect.h"
#include "Norms.h"

std::string SphereTest::Name() const {
    return "Sphere Test";
}

void SphereTest::Execute() {
	RenderMemory renderMemory;
	{
		const int noOfTests = 100;
		for(int counter = 0 ; counter < noOfTests && TestPassing() ; counter++) {
			const Real radius = Norm(Random<Real>());
			const Auto<const Solid> sphere = MakeSphere(radius);
			const Point randomPoint = Normalize(RandomPoint()) * radius;
			
			const Point normal = sphere->DetermineClosestIntersectionPoint(
				Line(Point(),randomPoint), eRender, renderMemory).Get()->NormalAt();

			AssertNearlyEqual("The Length of normal should be 1", Norm(normal), 1);
			AssertNearlyEqual("Normal pointing in the right direction", normal, Normalize(randomPoint));
		}
	}
	{
		const int noOfTests = 100;
		const Real pi = 2*atan2(1.,0.);
		for(int counter = 0 ; counter < noOfTests && TestPassing() ; counter++) {
			const Real radius = Norm(Random<Real>());
			if (radius != 0) {
				const Auto<const Solid> sphere = MakeSphere(radius);
				AssertNearlyEqual(
					"UV map on sphere works correctly: Special case ... North pole.",
					sphere->DetermineClosestIntersectionPoint(
						Line(Point(),Point(0,+radius,0)), eRender, renderMemory).Get()->UVCoordsAt(), 
					Point(0,pi/2,0)
				);
				AssertNearlyEqual(
					"UV map on sphere works correctly. Special case ... South pole.",
					sphere->DetermineClosestIntersectionPoint(
						Line(Point(),Point(Point(0,-radius,0))), eRender, renderMemory).Get()->UVCoordsAt(),
					Point(0,-pi/2,0)
				);
				AssertNearlyEqual(
					"UV map on sphere works correctly. Left.",
					sphere->DetermineClosestIntersectionPoint(
						Line(Point(),Point(Point(+radius,0,0))), eRender, renderMemory).Get()->UVCoordsAt(),
					Point(0,0,0)
				);
				AssertNearlyEqual(
					"UV map on sphere works correctly. Right.", 
					sphere->DetermineClosestIntersectionPoint(
						Line(Point(),Point(Point(-radius,0,0))), eRender, renderMemory).Get()->UVCoordsAt(),
					Point(pi,0,0)
				);
				AssertNearlyEqual(
					"UV map on sphere works correctly. In.",
					sphere->DetermineClosestIntersectionPoint(
						Line(Point(),Point(Point(0,0,+radius))), eRender, renderMemory).Get()->UVCoordsAt(),
					Point(pi/2,0,0)
				);
				AssertNearlyEqual(
					"UV map on sphere works correctly. Out.",  
					sphere->DetermineClosestIntersectionPoint(
						Line(Point(),Point(Point(0,0,-radius))), eRender, renderMemory).Get()->UVCoordsAt(),
					Point(-pi/2,0,0)
				);
				if (!TestPassing()) {
					PersistentConsole::OutputString("\nradius = " + ToString(radius) + "\n");
				}
			}
		}
	}
	{
		const Auto<const Solid> sphere = MakeSphere(.5);
		Assert("Point is inside", sphere->InsideQ(Point(.5, 0, 0)));
		Assert("Point is inside", sphere->InsideQ(Point(.25, .25, 0)));
		Assert("Point isn't inside", !sphere->InsideQ(Point(1,1,0)));
	}
}
