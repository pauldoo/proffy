#pragma once

#include "ITestable.h"

class Vector3dTest : public ITestable {
public:
	void Execute();
	std::string Name() const;
};