#pragma once
#include "ITestable.h"

#include "ScalarDeclarations.h"

class PolynomialTest : public ITestable {
public:
	void Execute();
	std::string Name() const;
private:
	template <typename RootFinder>
	void SolveFor(const RootFinder& rootFinder, const std::string &error_header, const ScalarList& roots);

	template <class RootFinder>
	void FindRootsTest(const RootFinder& rootFinder);

	template <typename RootFinder> 
	void TestFindSmallestPositiveRoot();

	template <typename RootFinder>
	void BruteForceTesting();
};
