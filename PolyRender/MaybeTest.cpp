#include "stdafx.h"
#include "MaybeTest.h"
#include "Exception.h"

std::string MaybeTest::Name() const {
	return "Maybe Test";
}

void MaybeTest::AssertValid(const Maybe<Real>& x) {
	Assert("Maybe should be valid", x.IsValid());
	AssertEqual("Maybe should have value ten", x.Get(), 10);
}

void MaybeTest::AssertInValid(const Maybe<Real>& x) {
	Assert("Maybe should be invalid", !x.IsValid());
	THROWCHECK("Accessing an invalid maybe", x.Get());
}

void MaybeTest::Execute() {
	// Default is invalid
	AssertInValid(Maybe<Real>());

	// implicit conversion causes validity
	AssertValid(Maybe<Real>(10));
	
	Maybe<Real> x;
	{
		// Copy constructor maintains invalidity
		Maybe<Real> y(x);
		AssertInValid(y);
	}
	{
		// Assignment maintains invalidity
		Maybe<Real> y;	
		y = x;
		AssertInValid(y);
	}
	
	// Assignment causes validity
	x = 10;
	AssertValid(x);
	{
		// copy construction of valid is valid
		Maybe<Real> y(x);
		AssertValid(y);
	}
	{
		// assignment to validity
		Maybe<Real> y;
		y = x;
		AssertValid(y);
	}
}