#pragma once
#include "BumpMap.h"

class TwirlyBump : public BumpMap {
public:
	Matrix CoordSysAt(const Point& uvCoords) const;
};
