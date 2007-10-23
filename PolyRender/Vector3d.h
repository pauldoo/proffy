#pragma once
#include "Utilities.h"
#include "PointDeclarations.h"
#include "MatrixDeclarations.h"
#include "VectorAdapter.h"
#include "Norms.h"

template <typename T, typename K>
class Vector3d : VectorAdapter<K, Vector3d<T, K> > {
public:	
	
	explicit Vector3d(T x = T(), T y = T(), T z = T()) : x(x), y(y), z(z) {}
	
	template <class T1, class V1>
	explicit Vector3d(const Vector3d<T1, V1>& v) : x(T(v.X())), y(T(v.Y())), z(T(v.Z())) {}
	typedef T Type;

	T X() const {return x;}
	T Y() const {return y;}
	T Z() const {return z;}

	Vector3d& operator+=(const Vector3d& v) {
		x += v.x; y += v.y; z += v.z; return *this;
	}

	Vector3d& operator*=(const K& t) {
		x *= t; y *= t; z *= t; return *this;
	};

	bool operator==(const Vector3d& v) const {
		return x == v.x && y == v.y && z == v.z;
	}

private:
	// TODO: why are these public?
	T x,y,z;
};

Real DotProd(const Point &a, const Point &b);

Point MatrixProd(const Matrix &a, const Point &b);

// Technically a DotProduct
Point TransposeMatrixProd(const Matrix &a, const Point &b);

Matrix MatrixProd(const Matrix &a, const Matrix &b);

Matrix MatrixProd(const Point &a, const Matrix &b);

Matrix Transpose(const Matrix &a);

Matrix RotationMatrix(const Point &axis, const Real angle);

Real Square(const Real x);

template <typename T, typename K> 
Real NormSquared(const Vector3d<T, K> &V) {
	return NormSquared(V.X()) + NormSquared(V.Y()) + NormSquared(V.Z());
}

Point Normalize(const Point& point);

Point CrossProd(const Point& a, const Point& b);

Point RandomPoint();

Matrix RandomMatrix();

Matrix AlignCoordsSys(const Point& Zdirection, const Point& inYZplane = Point(0,1,0));

Matrix IdentityMatrix();

template <typename T, typename K>
std::string ToString(const Vector3d<T, K> &V) {
	return "(" + ToString(V.X()) + ", " + ToString(V.Y()) + ", " + ToString(V.Z()) + ")";
}
