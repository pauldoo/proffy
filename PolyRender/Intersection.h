#pragma once

class IntersectionList;

class Intersection {
public:
    virtual void Refine(IntersectionList&) {};
};