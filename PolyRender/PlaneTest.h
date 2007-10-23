#pragma once

#include "ITestable.h"

class PlaneTest : public ITestable {
public:
    void Execute();
    std::string Name() const;
};