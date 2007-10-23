#include "stdafx.h"
#include "NewtonPolySolve.h"
#include "Maybe.h"
#include "CPolynomialDeclarations.h"
#include "Polynomial.h"
#include "Scalar.h"
#include "Norms.h"
#include "Utilities.h"
#include "Exception.h"

namespace {
	template<typename Type>
	Type NextRoot(const Polynomial<Type>& poly, Type x, bool* converged)
	{
		*converged = true;
		const Polynomial<Type> derivative = poly.Differentiate();
		const int maximumNewTonRaphsonCycles = 2000;
		// WARNING: potential divide by zero
		// Keep iterating until the magnitude h is less than maxAcceptableError
		for (int indent = 0 ; indent < maximumNewTonRaphsonCycles ; ++indent) {
			// Adding a squarely random complex number results in faster times
			x += Random<Type>() * 1.0e-9;
			const Type derivativeAtX = derivative.EvaluateAt(x);
			if (derivativeAtX == Type(0)) throw Exception("divide by zero when finding root.");
			const Type dx = poly.EvaluateAt<Type>(x)/derivativeAtX;
			x -= dx;
			// TODO: write square function
			if (NormSquared(dx) < NormSquared(maxAcceptableErrorSquared)) break;
			if (indent == maximumNewTonRaphsonCycles - 1)
				*converged = false;
		}	
		return x;
	}
}

ScalarList NewtonPolySolve::FindRoots(const CPolynomial& poly, const ScalarList& guessListIn) {
	
	if (poly.Degree() == 0) {
		if (poly[0] == Scalar(0))
			return ScalarList(1,0);
		else 
			throw Exception("No solution");
	}
		
	Scalar x(0);
	
	ScalarList guessList = guessListIn;

	if (!guessList.empty()) {
		x = guessList.back();
		guessList.pop_back();
	}
	
	bool converged;
	x = NextRoot(poly, x, &converged);
	
	ScalarList answers;
	if (poly.Degree() > 1)
		ScalarList answers = FindRoots(poly.RemoveRoot(x), guessList);

	answers.push_back(x);
	return answers;
}

ScalarList NewtonPolySolve::FindRoots(const RPolynomial& poly, const ScalarList& guessList) {
	return FindRoots(CPolynomial(poly), guessList);
}

namespace {
	bool IsReal(const Scalar& q) {
		return NearlyZeroQ(q.imag());
	}
}

Maybe<Real> NewtonPolySolve::FindSmallestPositiveRoot(const RPolynomial& p, const Real /*lastSmallestPositive*/) {
	if (NumberOfSignChanges(p) == 0)
		return Maybe<Real>();

	const CPolynomial q(p);
	//{
	//	bool converged;
	//	RPolynomial t = RPolynomial::MakeT();
	//	Scalar x = NextRoot(q, Scalar(lastSmallestPositive), &converged);
	//	if (converged && IsReal(x) && x.real() > 0 && NumberOfSignChanges(p.EvaluateAt(t + x.real() + .1)) == 0)
	//		return x.real();

	//	/*if (converged && )
	//		return x;*/
	//}

	
	const ScalarList roots = FindRoots(q);
	Maybe<Real> result;
	for (int i = 0 ; i < (int)roots.size() ; i++) {
		if (IsReal(roots[i]) && roots[i].real() > 0) {
			if (result.IsValid()) {
				result = std::min(result.Get(), roots[i].real());
			} else {
				result = roots[i].real();
			}
		}
	}
	return result;
}