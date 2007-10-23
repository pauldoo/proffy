#pragma once

#include "ITestable.h"

class SolidUnionTest : public ITestable {
public:
    void Execute();
    std::string Name() const;
};
