#pragma
#include "MatrixDeclarations.h"
#include "PointDeclarations.h"
#include "AutoDeclarations.h"

class Solid;

Auto<const Solid> MakeTransformationAdapter(
	const Auto<const Solid>& solid, 
	const Matrix& transformation, 
	const Point& translation
);