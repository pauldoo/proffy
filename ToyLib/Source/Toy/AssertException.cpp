#include "Toy/PrecompiledDeclarations.h"
#include "Toy/AssertException.h"

namespace Toy {
    AssertException::AssertException(const std::string& reason) : m_reason(reason)
    {
    }
    
    AssertException::~AssertException() throw ()
    {
    }
    
    const char* AssertException::what() const throw ()
    {
        return m_reason.c_str();
    }
}

