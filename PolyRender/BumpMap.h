#pragma once
#include "PointDeclarations.h"
#include "MatrixDeclarations.h"
#include "LinkCount.h"

class BumpMap : public LinkCount {
public: 
	virtual Matrix CoordSysAt(const Point& uvCoords) const = 0;
};

