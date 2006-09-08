#ifndef BIGNUM_H
#define BIGNUM_H

#include <ostream>
#include <vector>

class BigNum
{
public:
    typedef unsigned long DigitType;
    typedef std::vector<DigitType> DigitsType;

    BigNum(unsigned long);
    BigNum(const BigNum&);
    ~BigNum();
    
    BigNum& operator =(const BigNum&);
    
    BigNum& operator +=(const BigNum&);
    
    const DigitsType& Digits() const;
    
    static const unsigned long InternalBase();
    
private:
    static const unsigned long s_base;

    bool m_positive;
    DigitsType m_digits;
};

std::ostream& operator <<(std::ostream&, const BigNum&);

#endif

