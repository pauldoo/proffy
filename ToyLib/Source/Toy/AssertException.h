#pragma once

#define TOY_ASSERT(condition, message) \
{ \
    if (!(condition)) { \
        throw Toy::AssertException(std::string(__FILE__) + ":" + boost::lexical_cast<std::string>(__LINE__) +": " + (message)); \
    } \
}

#ifdef NDEBUG
#define TOY_DEBUG_ASSERT(condition, message)
#else
#define TOY_DEBUG_ASSERT(condition, message) TOY_ASSERT(condition, message)
#endif

namespace Toy {
    class AssertException : public std::exception
    {
    public:
        AssertException(const std::string& reason);
        
        ~AssertException() throw ();
        
        const char* what() const throw();
        
    private:
        const std::string m_reason;
    };
}

