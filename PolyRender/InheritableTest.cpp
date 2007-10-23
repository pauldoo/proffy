#include "stdafx.h"
#include "InheritableTest.h"

#include "DestructionAsserter.h"

std::string InheritableTest::Name() const {
	return "Inheritable Test";
}

void InheritableTest::Execute() {
	bool destroyed = false;
	const Inheritable* A = new DestructionAsserter(destroyed);
	delete A;
	Assert("DestructionAsserter was deleted", destroyed);
}
