#ifndef MC_PRIMES_H
#define MC_PRIMES_H

#include <vector>

namespace MC {
    template<typename T> class Primes
    {
    public:
	Primes();
	~Primes();
	
	void Next();
	
	const T& Current() const;
	
    private:
        const bool IsDivisible(const T& value) const;
    
	T m_next;
	std::vector<T> m_primes;
    };
}

#endif
