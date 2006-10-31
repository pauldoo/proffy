#include "Shared/Exception.h"

namespace MC {
    Exception::Exception(const std::string& reason) : m_reason(reason)
    {
    }

    Exception::~Exception() throw()
    {
    }

    const char* Exception::what() const throw()
    {
	return m_reason.c_str();
    }
}

