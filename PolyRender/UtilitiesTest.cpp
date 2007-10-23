#include "stdafx.h"
#include "UtilitiesTest.h"

#include "Utilities.h"
#include "Scalar.h"
#include "Norms.h"

std::string UtilitiesTest::Name() const {
	return "Utilities Test";
}


// TODO: Break out the Norms test... and maybe make it templated for the various kinds of NORMS
void  UtilitiesTest::Execute() {
	{
		std::vector<int> intVector;
		AssertEqual("ToString of empty vector bust", ToString(intVector), "[]");
		intVector.push_back(2);
		intVector.push_back(-3);
		AssertEqual("ToString bust", ToString(intVector), "[2, -3]");
	}
	{
		AssertEqual("ToString(int)", ToString(-3), "-3");
		AssertEqual("ToString(Scalar)", ToString(Scalar(0,1)), "i");
		AssertEqual("ToString(Scalar)", ToString(Scalar(0,0)), "0");
		AssertEqual("ToString(Scalar)", ToString(Scalar(-3,2)), "(-3+2i)");
		AssertEqual("ToString(Scalar)", ToString(Scalar(-3,-2)), "(-3-2i)");
		AssertEqual("ToString(Scalar)", ToString(Scalar(-3,0)), "-3");
	}
	{
		const int noOfSummands = 100000;
		Real sum = 0;
		for (int counter = 0 ; counter < noOfSummands ; counter++) {
			Assert("Norm(Random<Real>()) <= 1", Norm(Random<Real>()) <= 1);
			sum += Random<Real>();
		}
		AssertNearlyEqual("Average of Random Real is 0", sum/noOfSummands, 0);
	}
}


