#include "stdafx.h"
#include "PolynomialTest.h"

#include "CPolynomial.h"
#include "Exception.h"
#include "Maybe.h"
#include "Scalar.h"
#include "NewtonPolySolve.h"
#include "BairstowPolySolve.h"

#include <numeric>
#include <functional>
#include <algorithm>
#include <sstream>

/////// HELPERS ///////////////////////////////////////////////////////////////////

namespace {
	template <typename Type>
	std::vector<Type> RandomList(const int maxSize) {
		std::vector<Type> randomList;
		std::generate_n(std::back_inserter(randomList), std::rand() % maxSize, Random<Type>);
		return randomList;
	}

	template <typename Type>
	Polynomial<Type> PolyWithTheseRoots(const std::vector<Type>& rootList) {
		static Polynomial<Type> t = Polynomial<Type>::MakeT();
		Polynomial<Type> product(1);

		std::vector<Type>::const_iterator root;
		for (root = rootList.begin() ; root != rootList.end() ; root++ ) {
			product = product * (t - *root);
		}
		return product;
	}

	template <int N>
	ScalarList ToVector(const Scalar (&scalarList)[N]) {
		ScalarList scalarVector;
		std::copy(scalarList, scalarList + N, std::back_inserter(scalarVector));
		return scalarVector;
	}

	Maybe<Real> FindSmallestPositive(const std::vector<Real>& v) {
		std::vector<Real> result;
		std::vector<Real>::const_iterator it = v.begin();

		while ((it = std::find_if(it, v.end(), std::bind1st(std::less_equal<Real>(), 0))) != v.end()) 
		{
			result.push_back(*it);
			it++;
		}
		if (result.empty()) 
			return Maybe<Real>();
		else 
			return *std::min_element(result.begin(), result.end());
	}

	ScalarList operator-(const ScalarList &A, const ScalarList &B) {
		if (A.size() != B.size()) throw Exception("Trying to subtract ScalarLists of equal length");
		ScalarList C;
		std::transform(A.begin(),A.end(),B.begin(),std::back_inserter(C),std::minus<Scalar>());
		return C;
	}

	ScalarList Sort(const ScalarList &A) {
		class ComplexRelation {
		public:
			bool operator()(const Scalar &A, const Scalar &B) const {
				return std::make_pair(A.real(), A.imag()) > std::make_pair(B.real(), B.imag());
			}
		};
		ScalarList B(A);
		std::sort(B.begin(),B.end(),ComplexRelation());
		return B;
	}
}

///////////////////////////////////////////////////////////////////////////////////

template <typename RootFinder>
void PolynomialTest::FindRootsTest(const RootFinder& rootFinder) {
	// This checks to make sure that the find roots method is working
	{	
		const Scalar rootsList[] = {1,2,3,7,100,i};
		SolveFor("Simple Case", ToVector(rootsList));
	}
	// Brute Force testing
	{
		const int noOfPolysToSolve = 1000;
		const int maxDegree = 10;
		for (int indent = 0 ; indent < noOfPolysToSolve && TestPassing() ; indent++)	{
			SolveFor("Random Case", RandomList<Scalar>(maxDegree + 1));
			Console::OutputString("\nTesting in Progress\n" + ToString((indent+1)*100/noOfPolysToSolve) +  "% complete");
		}
	}
}

template <class RootFinder>
void PolynomialTest::SolveFor(const RootFinder& rootFinder, const std::string &error_header, const ScalarList& roots) {
	ScalarList foundRoots = rootFinder(PolyWithTheseRoots(roots));

	if (AssertEqual(error_header + ": Too Many/Not Enough roots found", foundRoots.size(), roots.size())) {
		AssertNearlyEqual(error_header + ": Root is wrong/not close enough", Sort(roots), Sort(foundRoots));
	} 
}

std::string PolynomialTest::Name() const{
	return "Polynomial Test";
}

template <typename RootFinder> 
void PolynomialTest::TestFindSmallestPositiveRoot() {
	const RPolynomial t = RPolynomial::MakeT();
	const RPolynomial sample = (t - 1)*(t + 2)*(t*t + 1);
	const Real smallestPositiveRoot = RootFinder::FindSmallestPositiveRoot(sample).Get();
	AssertNearlyEqual("Fast: Root is wrong/not close enough", smallestPositiveRoot, 1);
}

template <typename RootFinder> 
void PolynomialTest::BruteForceTesting() {
	const int noOfPolysToSolve = 400;
	const int maxDegree = 10;
	for (int indent = 0 ; indent < noOfPolysToSolve && TestPassing() ; indent++)	{
		const std::vector<Real> roots = RandomList<Real>(maxDegree + 1);
		Console::OutputString("\nTesting in Progress\n" + ToString((indent+1)*100/noOfPolysToSolve) +  "% complete");
		const Maybe<Real>
			actualRoot = FindSmallestPositive(roots),
			foundRoot = RootFinder::FindSmallestPositiveRoot(PolyWithTheseRoots(roots));

		// Todo ... check the roots.
		Assert(
			"Fast: Brute: Root should/should not of been found:\nActual Root = " + 
			(actualRoot.IsValid() ? ToString(actualRoot.Get()) : "n/a") + "\nFound Root = " +
			(foundRoot.IsValid() ? ToString(foundRoot.Get()) : "n/a"), 
			actualRoot.IsValid() == foundRoot.IsValid());
		if (actualRoot.IsValid())
			AssertNearlyEqual("Fast: Brute: Root " + ToString(indent) + " is wrong/not close enough", .1*actualRoot.Get(), .1*foundRoot.Get());
	}
}

void PolynomialTest::Execute() {
	// This is a arithmetic sanity check.
	const CPolynomial z = CPolynomial::MakeT();
	{
		const Scalar i(0,1);

		// compilation check;
		z + 1.0 * i;
		const CPolynomial 
			lhs = (3*z*z*3 + (z+1.)*(z-1.) - z * i - z*z*z*z + z*z*z*z).Differentiate(),
			rhs = z*18 + z*2 - i;
		AssertEqual("Basic arithmetic error: Degree differs", lhs.Degree(), unsigned int(1));
		AssertEqual("Basic arithmetic error", lhs, rhs);
	}
	// Zero multiplication test
	{
		AssertEqual("0*P(z) == 0 for all P", CPolynomial(0), z*0);
	}
	
	TestFindSmallestPositiveRoot<NewtonPolySolve>();
	TestFindSmallestPositiveRoot<BairstowPolySolve>();
	BruteForceTesting<NewtonPolySolve>();
	BruteForceTesting<BairstowPolySolve>();
}