#include "stdafx.h"
#include "Vector3d.h"

#include "Polynomial.h"
#include "CPolynomialDeclarations.h"
#include "Exception.h"
#include "Norms.h"

#include "TimingPool.h"

Real DotProd(const Point &a, const Point &b) {
	return a.X() * b.X() + a.Y() * b.Y() + a.Z() * b.Z();
}

Point MatrixProd(const Matrix &A, const Point &b) {
	//TIMETHISBLOCK("MatrixProd Type1");
	return Point(DotProd(A.X(),b),DotProd(A.Y(),b),DotProd(A.Z(),b));
}

Point TransposeMatrixProd(const Matrix &a, const Point &b)
{
	return a.X() * b.X() + a.Y() * b.Y() + a.Z() * b.Z();
}

Matrix MatrixProd(const Matrix &A, const Matrix &B) {
	TIMETHISBLOCK("MatrixProd Type2");
	Matrix C(Transpose(B));
	return Transpose(Matrix(MatrixProd(A,C.X()),MatrixProd(A,C.Y()),MatrixProd(A,C.Z())));
}

Point Normalize(const Point& point) {
	return point/Norm(point);
}

Matrix Transpose(const Matrix &A) {
	return Matrix(
		Point(A.X().X(), A.Y().X(), A.Z().X()),
		Point(A.X().Y(), A.Y().Y(), A.Z().Y()),
		Point(A.X().Z(), A.Y().Z(), A.Z().Z())
	);
}

Matrix RotationMatrix(const Point &axis, const Real angle) {
	
	if (Norm(axis) == 0) 
		throw Exception("0 length axis vector given.");

	Point normedAxis = Normalize(axis);
    
	Real 
		s = std::sin(angle/2), 
		w = std::cos(angle/2),
		x = s*normedAxis.X(), 
		y = s*normedAxis.Y(), 
		z = s*normedAxis.Z();

	//if (!NearlyZeroQ(s*s + w*w - 1)) throw std::domain_error("dfd");   


	return	Matrix(
				Point(1 - 2 * ( y*y + z*z ),     2 * ( x*y - w*z ),     2 * ( x*z + w*y )),
				Point(    2 * ( x*y + w*z ), 1 - 2 * ( x*x + z*z ),     2 * ( y*z - w*x )),
				Point(    2 * ( x*z - w*y ),     2 * ( y*z + w*x ), 1 - 2 * ( x*x + y*y ))
			);
}

Point RandomPoint() {
    return Point(Random<Real>(), Random<Real>(), Random<Real>());
}

Matrix RandomMatrix() {
    return Matrix(RandomPoint(), RandomPoint(), RandomPoint());
}

Matrix AlignCoordsSys(const Point& Zdirection, const Point& inYZplane) {
	// This needs testing
	
	const Point Xdirection = CrossProd(inYZplane, Zdirection);
	//if (NearlyZeroQ(Xdirection)) throw std::logic_error("parallel vectors supplied to AlignCoordsSys");
	
	const Point X = Normalize(Xdirection);
	const Point Z = Normalize(Zdirection);
	const Point Y = CrossProd(Z,X);

	return Matrix(X,Y,Z);
}

Matrix IdentityMatrix() {
	return Matrix(Point(1,0,0), Point(0,1,0), Point(0,0,1));
}

Point CrossProd(const Point& a, const Point& b) {
	return Point(
		a.Y() * b.Z() - a.Z() * b.Y(), 
		a.Z() * b.X() - a.X() * b.Z(),
		a.X() * b.Y() - a.Y() * b.X()
	);
}

Real Square(const Real x) {
	return x*x;
}
