#include "stdafx.h"
#include "TwirlyBump.h"
#include "Point.h"

Matrix TwirlyBump::CoordSysAt(const Point& uvCoords) const {
	return AlignCoordsSys(Point(1.5*sin(80*uvCoords.X()+80*uvCoords.Y()),0,1));
}