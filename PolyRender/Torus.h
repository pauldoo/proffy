#pragma once
#include "Precision.h"
#include "AutoDeclarations.h"

class Solid;

Auto<const Solid> MakeTorus(const Real innerRadius = 1, const Real outerRadius = 1);
