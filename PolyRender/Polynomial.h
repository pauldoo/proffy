#pragma once

#include "Precision.h"
#include "MaybeDeclarations.h"
#include "RPolynomialDeclarations.h"
#include "VectorAdapter.h"
#include "NonCreatable.h"

// The following class represents a single variable (t) polynomial.
template <typename Type>
class Polynomial : VectorAdapter<Type, Polynomial<Type> >
{
public: 
	// TYPEDEFS //////////////////////////////////////////////////////////////////////////////

	typedef typename std::vector<Type>::size_type DegreeType;

	// CONSTRUCTORS & CONVERSIONS ////////////////////////////////////////////////////////////
	
	// This can be used to convert Scalars to Polynomials and is default constructor
	Polynomial(const Type& t = 0, const size_t degree = 0) {
		m_coefficentList.push_back(t);
		m_coefficentList.resize(degree + 1);
	}

	template <typename Type2>
	Polynomial(const Polynomial<Type2>& P) : m_coefficentList(P.CoefficentList().begin(),P.CoefficentList().end()) {}

	// OPERATORS /////////////////////////////////////////////////////////////////////////////
	
	Polynomial& operator=(const Polynomial&);
	
	// for vector extensions
	bool operator==(const Polynomial&) const;
	Polynomial& operator*=(const Polynomial&);
	Polynomial& operator+=(const Polynomial&);

	Polynomial& operator*=(const Type&);
	Polynomial& operator+=(const Type&);
	Polynomial& operator-=(const Type&);
	Polynomial operator*(const Polynomial& P) const;
	Polynomial operator+(const Type&) const;
	Polynomial operator-(const Type&) const;

	const Type& operator[](const DegreeType indent) const {return m_coefficentList[indent];}
	Type& operator[](const DegreeType indent) {return m_coefficentList[indent];}

	// METHODS ///////////////////////////////////////////////////////////////////////

	// How to calculate the value of the polynomial for a particular value of t.
	template <typename Type2>
	Type2 EvaluateAt(const Type2& t) const
	{
		Type2 sum = 0;
		for(typename std::vector<Type>::const_reverse_iterator indent = m_coefficentList.rbegin() ; indent != m_coefficentList.rend() ; indent++) {
			sum = sum * t + *indent;
		}
		return sum;
	}

	// The Degree of the polynomial
	DegreeType Degree() const {return m_coefficentList.size() - 1;}

	// This can serve as your 1*t + 0 vector which can be very useful.
	static Polynomial MakeT();

	// How to Differentiate the polynomial with respect to t NOTE :: We assume that the Degree > 0
	Polynomial Differentiate() const;

	const std::vector<Type>& CoefficentList() const;

	// How to remove a root from a polynomial. It's essentially just synthetic division
	// TODO should throw if the remander isn't zero.
	Polynomial RemoveRoot(const Type&) const;

private:
	std::vector<Type> m_coefficentList;
};

template <typename Type>
std::string ToString(const Polynomial<Type> &P);

template <typename Type>
Real NormSquared(const Polynomial<Type> &P);

// there dont need to be here
namespace {
	int Sign(const Real a)
	{
		return (a >= 0 ? 1 : -1);
	}

	int NumberOfSignChanges(const RPolynomial& p)
	{
		if (p.Degree() == 0)
			return 0;
		int result = 0;
		int sign = Sign(p[0]);
		for (int i = 1 ; i <= (int)p.Degree() ; i++)
		{
			if (sign != Sign(p[i])) {
				result++;
				sign = Sign(p[i]);
			}
		}
		return result;
	}
}
