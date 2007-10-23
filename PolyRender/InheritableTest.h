#pragma once

#include "ITestable.h"

class InheritableTest : public ITestable {
public:
	void Execute();
	std::string Name() const;
};
