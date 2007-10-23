#include "stdafx.h"
#include "RenderMemory.h"
#include "Auto.h"
#include "TimingPool.h"

RenderMemory::RenderMemory()
{
}

Auto<LinkCount>& RenderMemory::Guess(const IntersectID& intersectID)
{
	TIMETHISFUNCTION;
	return m_guess[intersectID];
};
