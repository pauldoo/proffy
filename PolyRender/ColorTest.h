#pragma once

#include "ITestable.h"

class ColorTest : public ITestable {
public:
    void Execute();
    std::string Name() const;
};
