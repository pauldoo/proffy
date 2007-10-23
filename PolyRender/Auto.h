#pragma once

#include "LinkCount.h"
#include "Exception.h"
#include "Console.h"

template <typename Type>
class Auto {
public:
	template <typename>
	friend class Auto;

	Auto();

	Auto(Type*);
	
	Auto(const Auto&);
	
	template <typename OtherType>
	Auto(const Auto<OtherType>& otherAuto)
	:	m_item(otherAuto.m_item)
	{
		m_item->IncrementCount();
	};

	~Auto();
	
	Type* operator->();
	
	const Type* operator->() const;

	Type* Pointer();
	
	const Type* Pointer() const;
	
	void operator=(Type*);

	template <typename OtherType> 
	void operator=(const Auto<OtherType>& otherAuto) {
		// have to be careful against self assignment here
		otherAuto.m_item->IncrementCount();
		m_item->DecrementCount();
		m_item = otherAuto.m_item;
	}	

private:
	Type* m_item;
};
