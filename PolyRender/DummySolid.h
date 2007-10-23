#pragma once
#include "PointDeclarations.h"
#include "AutoDeclarations.h"

class Solid;

Auto<const Solid> MakeDummySolid(const Point& position, const Point& normal);
