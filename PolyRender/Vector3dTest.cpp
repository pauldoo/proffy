#include "stdafx.h"
#include "Vector3dTest.h"
#include "Utilities.h"
#include "Vector3d.h"
#include "Timer.h"


std::string Vector3dTest::Name() const {
	return "Vector3d Test";
}

void  Vector3dTest::Execute() {
	const Point a(1,2,3);
	const Point b(4,5,6);
	const Point c(7,8,9);
	const Matrix A(a,b,c);
	const Matrix identity(Point(1,0,0),Point(0,1,0),Point(0,0,1));

	AssertEqual("Identiy Matrix", identity, IdentityMatrix());
	AssertEqual("Vector summation bust", a + b, Point(5,7,9) );
	AssertEqual("Vector subtraction bust",a - b, Point(-3,-3,-3));
	AssertEqual("Dot Product (vector-vector) bust", DotProd(a,b), 32);
	AssertEqual("Dot Product (matrix-vector) bust", MatrixProd(A,a), Point(14,32,50));
	AssertEqual("Matrix Transpose bust",Transpose(A), Matrix(Point(1,4,7),Point(2,5,8),Point(3,6,9)));

	AssertEqual("Matrix-Matrix multiplication bust", 
		MatrixProd(A,Matrix( Point(1,0,-1),Point(0,1,0),Point(0,0,1) ) ), 
		Matrix( Point(1,2,2),Point(4,5,2),Point(7,8,2) ) 
	);

	AssertNearlyEqual("Norm bust", Norm(a)*Norm(a), 14);
	AssertNearlyEqual("Normalize bust", Norm(Normalize(a)), 1);
	AssertEqual("Normalize bust", DotProd(a, Normalize(a)), Norm(a));
    
	const int noOfTests = 10;
    for (int indent = 0 ; indent < noOfTests && TestPassing() ; indent++)
	{
		const Point p = RandomPoint();
        const Matrix R(Transpose(RotationMatrix(p,Random<Real>())));
        AssertNearlyEqual("RotationMatrix doesen't produce orthogonal axes", MatrixProd(R,Transpose(R)), identity);
		AssertNearlyEqual("RotationMatrix rotates a vector parallel to it's own axis", MatrixProd(R,p), p);
	}
	{
		const int noOfSummands = 100000;
		Point sum(0,0,0);
		for (int counter = 0 ; counter < noOfSummands ; counter++) {
			Assert("Norm(Random<Real>()) <= 1", Norm(RandomPoint()) <= std::sqrt(3.));
			sum += RandomPoint();
		}
		AssertNearlyEqual("Average of RandomPoint is (0,0,0)", sum/noOfSummands/10, Point(0,0,0)/10);
	}
	AssertEqual("Cross Product works", CrossProd(Point(1,0,0), Point(0,1,0)), Point(0,0,1));
	{
		const int noOfTests = 100;
		for (int counter = 0 ; counter < noOfTests && TestPassing(); counter++) {
			Point randomPoint1 = RandomPoint();
			Point randomPoint2 = RandomPoint();
			Point result = CrossProd(randomPoint1, randomPoint2);
			AssertNearlyEqual("Cross Product(a,b) is perpendicular to both vectors", DotProd(result, randomPoint1), 0);
			AssertNearlyEqual("Cross Product(a,b) is perpendicular to both vectors", DotProd(result, randomPoint2), 0);

		}
	}
	AssertEqual("Aligning a coordinate system on up and right gives the identity matrix", 
		AlignCoordsSys(Point(0,0,1)), identity);
	{
		const int noOfTests = 100;
		for (int counter = 0 ; counter < noOfTests && TestPassing(); counter++) {
			Point randomPoint1 = RandomPoint();
			Point randomPoint2 = RandomPoint();
			if (Norm(CrossProd(randomPoint1, randomPoint2)) != 0) {
				Matrix R = AlignCoordsSys(randomPoint1, randomPoint2);
				AssertNearlyEqual("Cross Product(a,b) is perpendicular to both vectors", MatrixProd(R,Transpose(R)), identity);
			}
		}
	}
	// timing test
	//{
	//	const Point initialPoint = RandomPoint();
	//	const Matrix matrix = RandomMatrix();
	//	Point point = initialPoint;
	//	const Timer timer = Timer().Start();
	//	for (int i = 0 ; i < 1 ; i++)
	//	{
	//		point = TransposeMatrixProd(matrix, point);
	//	}
	//	PersistentConsole::OutputString("Transpose Timing was: " + ToString(timer.CurrentTime()) + ": " + ToString(point) + "\n");
	//}
}
