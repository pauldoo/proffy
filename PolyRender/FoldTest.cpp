#include "stdafx.h"
#include "FoldTest.h"
#include "Fold.h"

#include "SolidUnion.h"
#include "Auto.h"
#include "Plane.h"

std::string FoldTest::Name() const {
	return "Fold Test";
}

int Plus(int a, int b) {
	return a + b;
}

void FoldTest::Execute() {
	AssertEqual("Fold(Plus,1,2) == 3", Fold(Plus,1,2), 3);
	AssertEqual("Fold(Plus,1,2,3) == 2", Fold(Plus,1,2,3), 6);
	AssertEqual("Fold(Plus,1,2,3,4) == 2", Fold(Plus,1,2,3,4), 10);

	Fold(MakeSolidUnion, MakePlane(), MakePlane());
}


