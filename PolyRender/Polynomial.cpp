#include "stdafx.h"
#include "Polynomial.h"
#include "Exception.h"
#include "Utilities.h"
#include "Maybe.h"
#include "CPolynomialDeclarations.h"
#include "Scalar.h"
#include "Norms.h"

#include "Console.h" //TODO: remove me
#include <limits>

template class Polynomial<Real>;
template class Polynomial<Scalar>;

template <typename Type> 
Polynomial<Type> Polynomial<Type>::MakeT() {
	Polynomial<Type> t;
	t.m_coefficentList.push_back(1);
	return t;
}

template <typename Type> 
Polynomial<Type>& Polynomial<Type>::operator=(const Polynomial<Type> &P) {
	m_coefficentList = P.m_coefficentList; 
	return *this;
}

template <typename Type>
Polynomial<Type>& Polynomial<Type>::operator*=(const Polynomial<Type> &Q) {
	Polynomial<Type>& P = *this;
	Polynomial<Type> temp;
	if (P == Polynomial<Type>(0) || Q == Polynomial<Type>(0)) 
	{
		*this = Polynomial<Type>(0);
		return *this;
	}
	temp.m_coefficentList.resize(Q.Degree() + P.Degree() + 1);

	for (typename Polynomial<Type>::DegreeType indentx = 0 ; indentx <= Q.Degree() ; indentx++)
		for (typename Polynomial<Type>::DegreeType indenty = 0 ; indenty <= P.Degree() ; indenty++)
			temp[indentx + indenty] += Q[indentx] * P[indenty];

	*this = temp;
	return *this;
}

template <typename Type>
Polynomial<Type>& Polynomial<Type>::operator+=(const Polynomial<Type> &Q) {
	// A will be the polynomial with largest  Degree.
	// B will be the polynomial with smallest Degree.
	const Polynomial<Type> &P = *this;
	Polynomial<Type> A = (Q.Degree() > P.Degree() ? Q : P);
	const Polynomial<Type> &B = (Q.Degree() > P.Degree() ? P : Q);

	for (typename Polynomial<Type>::DegreeType indent = 0 ; indent <= B.Degree() ; ++indent) {
		A[indent] += B[indent];
	}

	// remove leading zeros
	for (typename Polynomial<Type>::DegreeType indent = A.Degree() ; indent > 0 && A[indent] == Type(0);  --indent) {
		A.m_coefficentList.pop_back();
	}

	*this = A;
	return *this;
}

template <typename Type>
Polynomial<Type>& Polynomial<Type>::operator*=(const Type &t) {
	if (t == 0.0) {
		m_coefficentList = std::vector<Type>(1,0);
	}
		
	for (size_t counter = 0 ; counter <= Degree() ; counter++) {
		m_coefficentList[counter] *= t;
	}
	return *this;
}

template <typename Type>
Polynomial<Type>& Polynomial<Type>::operator+=(const Type &t) {
	m_coefficentList[0] += t;
	return *this;
}

template <typename Type>
Polynomial<Type>& Polynomial<Type>::operator-=(const Type &t) {
	m_coefficentList[0] -= t;
	return *this;
}

template <typename Type>
Polynomial<Type> Polynomial<Type>::operator+(const Type &t) const {
	return Polynomial(*this) += t;
}

template <typename Type>
Polynomial<Type> Polynomial<Type>::operator-(const Type &t) const {
	return Polynomial(*this) -= t;
}

template <typename Type>
bool Polynomial<Type>::operator==(const Polynomial<Type> &Q) const {
	return m_coefficentList == Q.m_coefficentList;
}

template <typename Type>
Polynomial<Type> Polynomial<Type>::operator*(const Polynomial<Type>& P) const {
	return Polynomial(*this) *= P;
}

//template <typename Type>
//Type Polynomial<Type>::EvaluateAt<Type2>(const Type2 &t) const {
//	Type sum = 0;
//	for(std::vector<Type>::const_reverse_iterator indent = m_coefficentList.rbegin() ; indent != m_coefficentList.rend() ; indent++) {
//		sum = sum * t + *indent;
//	}
//	return sum;
//}

template <typename Type>
Polynomial<Type> Polynomial<Type>::Differentiate() const {
	Polynomial<Type> temp;
	
	if (Degree() == 0) return temp;

	temp.m_coefficentList.resize(Degree());
	for (DegreeType indent = 1 ; indent <= Degree() ; indent++)
		temp[indent - 1] = (*this)[indent] * Type(indent);

	return temp;
}

template <typename Type>
Polynomial<Type> Polynomial<Type>::RemoveRoot(const Type &root) const {	
	Polynomial<Type> temp;		
	temp.m_coefficentList.resize(Degree());

	if (temp[Degree() - 1] != Type(0)) throw Exception("You suck!");

	temp[Degree() - 1] = (*this)[Degree()];
	for (DegreeType indent = Degree() - 1 ; indent > 0 ; indent--)
		temp[indent - 1] = temp[indent] * root + (*this)[indent];

	return temp;
}


template <typename Type>
const std::vector<Type>& Polynomial<Type>::CoefficentList() const {
	return m_coefficentList;
}

template <typename Type>
Real NormSquared(const Polynomial<Type> &P) {
	return NormSquared(P.CoefficentList());
}

template Real NormSquared(const CPolynomial&);
template Real NormSquared(const RPolynomial&);

template <typename Type>
std::string ToString(const Polynomial<Type> &P) {
    std::string returnString;
    for (size_t indent = P.Degree() ; indent > 1 ; --indent) {
        returnString += ToString(P[indent]) + " t^" + ToString(indent) + " + ";
    }
    if (P.Degree() >= 1) {
        returnString += ToString(P[1]) + " t" + " + ";
    }
    return returnString += ToString(P[0]);
}

template std::string ToString<Real>(const RPolynomial &P);
template std::string ToString<Scalar>(const CPolynomial &P);

