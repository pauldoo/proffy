#include "stdafx.h"
#include "DestructionAsserter.h"

DestructionAsserter::DestructionAsserter(bool& destroyed) 
:	m_destroyed(destroyed) {
	m_destroyed = false;
}
	
DestructionAsserter::~DestructionAsserter() {
	m_destroyed = true;
}

bool DestructionAsserter::IsDestroyed() const {
	return m_destroyed;
}
