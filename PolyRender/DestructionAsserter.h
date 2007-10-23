#pragma once 

#include "LinkCount.h"

class DestructionAsserter : public LinkCount {
public:
	DestructionAsserter(bool& destroyed);
	
	~DestructionAsserter();

	bool IsDestroyed() const;
	
private:
	bool& m_destroyed;
};
