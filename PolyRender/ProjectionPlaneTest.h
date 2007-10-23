#pragma once

#include "ITestable.h"

class ProjectionPlaneTest : public ITestable {
public:
	void Execute();
	std::string Name() const;
};