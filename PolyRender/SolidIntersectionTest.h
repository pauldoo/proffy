#pragma once

#include "ITestable.h"

class SolidIntersectionTest : public ITestable {
public:
    void Execute();
    std::string Name() const;
};