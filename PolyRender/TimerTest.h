#pragma once

#include "ITestable.h"

class TimerTest : public ITestable {
public:
	void Execute();
	std::string Name() const;
};