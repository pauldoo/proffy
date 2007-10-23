#pragma once
#include "PointDeclarations.h"
#include "ParametricLineDeclarations.h"
#include "Point.h"
#include "NonCreatable.h"

class Line;

class LineFuncs : public NonCreatable {
public:
	static ParametricLine ToParametricLine(const Line& line);
	static Point EvaluateAt(const Line &line, double t);
};
