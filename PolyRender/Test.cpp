#include "stdafx.h"
#include "Test.h"
#include "PolynomialTest.h"
#include "ProjectionPlaneTest.h"
#include "Vector3DTest.h"
#include "ColorTest.h"
#include "UtilitiesTest.h"
#include "AutoTest.h"
#include "InheritableTest.h"
#include "SphereTest.h"
#include "TransformationAdapterTest.h"
#include "PlaneTest.h"
#include "MaybeTest.h"
#include "SolidIntersectionTest.h"
#include "BoundingSolidTest.h"
#include "SolidUnionTest.h"
#include "FoldTest.h"
#include "TimerTest.h"

void Test() {
	TimerTest().ExecuteTest();
	FoldTest().ExecuteTest();
	SolidUnionTest().ExecuteTest();
	BoundingSolidTest().ExecuteTest();
	SolidIntersectionTest().ExecuteTest();
	MaybeTest().ExecuteTest();
	PlaneTest().ExecuteTest();
	TransformationAdapterTest().ExecuteTest();
	SphereTest().ExecuteTest();
	InheritableTest().ExecuteTest();
	AutoTest().ExecuteTest();
	UtilitiesTest().ExecuteTest();
	PolynomialTest().ExecuteTest();
	ProjectionPlaneTest().ExecuteTest();
	Vector3dTest().ExecuteTest();
	ColorTest().ExecuteTest();
}
