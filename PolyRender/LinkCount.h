#pragma once
#include "Inheritable.h"

template <typename Type>
class Auto;

class LinkCount : public Inheritable {
	template <typename Type>
	friend class Auto;
	template <typename Type>
	friend class AutoC;
	
	template <typename Type>
	friend class Ref;
protected:
	LinkCount();

private:
	void IncrementCount() const;
	void DecrementCount() const;
	int Count() const;
	mutable int m_linkCount;
};
