#ifndef MC_EXCEPTION_H
#define MC_EXCEPTION_H

#include <exception>
#include <string>

namespace MC {
    class Exception : public std::exception
    {
    public:
        Exception(const std::string& reason);
        
        virtual ~Exception() throw();
        
        virtual const char* what() const throw();
        
    private:
        const std::string m_reason;        
    };
}

#endif
