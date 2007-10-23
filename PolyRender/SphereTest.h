#pragma once

#include "ITestable.h"

class SphereTest : public ITestable {
public:
    void Execute();
    std::string Name() const;
};