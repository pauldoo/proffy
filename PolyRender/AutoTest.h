#pragma once

#include "ITestable.h"

class AutoTest : public ITestable {
public:
	void Execute();
	std::string Name() const;
};