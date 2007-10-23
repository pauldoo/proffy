#pragma once 
#include "MaybeDeclarations.h"

template <typename Type>
class Maybe {
public:
	Maybe();
	
	Maybe(const Type&);

	Maybe(const Maybe<Type>&);
	
	~Maybe();

	bool IsValid() const;
	void operator=(const Type&);
	void operator=(const Maybe<Type>&);

	Type Get() const;

private:
	const Type* m_item;
};
