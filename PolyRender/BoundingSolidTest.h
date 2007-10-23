#pragma once

#include "ITestable.h"

class BoundingSolidTest : public ITestable {
public:
    void Execute();
    std::string Name() const;
};