#pragma once

#include "ITestable.h"

class UtilitiesTest : public ITestable {
public:
	void Execute();
	std::string Name() const;
};