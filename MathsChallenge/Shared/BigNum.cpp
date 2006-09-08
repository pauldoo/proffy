#include "Shared/BigNum.h"

#include <iostream>

namespace
{
    const unsigned long CalculateBase(void)
    {
        unsigned long result = 1;
        while (result == (result * 10) / 10) {
            result *= 10;
        }
        std::cout << result << std::endl << (result * 10) << std::endl;
        return result;
    }
}

const unsigned long BigNum::s_base = CalculateBase();

const unsigned long BigNum::InternalBase()
{
    return s_base;
}

BigNum::BigNum(unsigned long v)
{
    while (v != 0) {
        m_digits.push_back(v % s_base);
        v = v / s_base;
    }
}

BigNum::BigNum(const BigNum& other) :
    m_positive(other.m_positive),
    m_digits(other.m_digits)
{
}

BigNum::~BigNum()
{
}

const BigNum::DigitsType& BigNum::Digits() const
{
    return m_digits;
}

std::ostream& operator <<(std::ostream& out, const BigNum& num)
{
    // TODO: Not finished
    for (DigitsType::const_iterator i = num.Digits().begin(); i != num.Digits().end(); ++i) {
        out << *i << ", ";
    }
    return out;
}

