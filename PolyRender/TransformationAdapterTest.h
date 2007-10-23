#pragma once

#include "ITestable.h"

class TransformationAdapterTest : public ITestable {
public:
    void Execute();
    std::string Name() const;
};