#pragma once
#include "LinkCount.h"
#include <map>
class Solid;

enum eIntersectType {
	eRender,
	eShadow
};

typedef std::pair<eIntersectType, const Solid*> IntersectID;

class RenderMemory : public LinkCount {
public:
	RenderMemory();

	Auto<LinkCount>& Guess(const IntersectID& intersectID);
private:
	std::map<IntersectID, Auto<LinkCount> > m_guess;
};