#include "Shared/Primes.h"

#include "Shared/Exception.h"

namespace MC {
    template class Primes<unsigned int>;
    
    template<typename T> Primes<T>::Primes() : m_next(T(2))
    {
    }
    
    template<typename T> Primes<T>::~Primes()
    {
    }
    
    template<typename T> void Primes<T>::Next()
    {
        while (IsDivisible(m_next)) {
            m_next++;
        }
        m_primes.push_back(m_next);
        m_next++;
    }

    template<typename T> const T& Primes<T>::Current() const
    {
        if (m_primes.empty()) {
            throw Exception("Current called before Next()");
        }
        return m_primes.back();
    }
    
    template<typename T> const bool Primes<T>::IsDivisible(const T& val) const
    {
        for (typename std::vector<T>::const_iterator i = m_primes.begin(); i != m_primes.end() && ((*i) * (*i)) <= val; ++i) {
            if (val % (*i) == T(0)) {
                return true;
            }
        }
        return false;
    }
}
