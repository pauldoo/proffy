#include "stdafx.h"
#include "LinkCount.h"

LinkCount::LinkCount() 
:	m_linkCount(0)
{
}

void LinkCount::IncrementCount() const {
	if (this) {
		m_linkCount++;
	}
}

void LinkCount::DecrementCount() const {
	if (this) {
		m_linkCount--;
		if (Count() == 0) delete this;
	}
}

int LinkCount::Count() const {
	return m_linkCount;
}
