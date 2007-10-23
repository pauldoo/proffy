#pragma once

#include "ITestable.h"
#include "Maybe.h"
#include "Precision.h"

class MaybeTest : public ITestable {
public:
	void Execute();
	std::string Name() const;

private:
	void AssertValid(const Maybe<Real>& x);
	void AssertInValid(const Maybe<Real>& x);
};
