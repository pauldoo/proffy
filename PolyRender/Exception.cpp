#include "stdafx.h"
#include "Exception.h"

Exception::Exception(const std::string& message) 
:	m_message(message)
{
}

std::string Exception::Message() const {
	return m_message;
}
