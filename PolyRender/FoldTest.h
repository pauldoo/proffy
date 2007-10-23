#pragma once

#include "ITestable.h"

class FoldTest : public ITestable {
public:
    void Execute();
    std::string Name() const;
};
