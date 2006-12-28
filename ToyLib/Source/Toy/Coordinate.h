#pragma once

namespace Toy {
    template<
        typename TYPE,
        unsigned int DIMENSIONS>
    class Coordinate
    {
    public:
        typedef const TYPE* const_iterator;
        typedef TYPE* iterator;
    
        Coordinate()
        {
            std::fill(m_values, m_values + DIMENSIONS, TYPE(0));
        }
        
        template<typename OTHERTYPE>
        Coordinate(const Coordinate<OTHERTYPE, DIMENSIONS>& other)
        {
            typename Coordinate<OTHERTYPE, DIMENSIONS>::const_iterator j = other.begin();
            for (iterator i = begin(); i != end(); ++i, ++j) {
                (*i) = boost::numeric_cast<TYPE>(*j);
            }
        }
        
        const TYPE operator [] (const unsigned int index) const
        {
            return m_values[index];
        }
        
        TYPE& operator [] (const unsigned int index)
        {
            return m_values[index];
        }
        
        const const_iterator begin() const
        {
            return m_values;
        }
        
        const const_iterator end() const
        {
            return m_values + DIMENSIONS;
        }
        
        const iterator begin()
        {
            return m_values;
        }
        
        const iterator end()
        {
            return m_values + DIMENSIONS;
        }
        
        const TYPE BlockVolume() const
        {
            TYPE count = 1;
            for (const_iterator i = begin(); i != end(); ++i) {
                count *= *i;
            }
            return count;
        }

        const bool EntirelyContains(const Coordinate& other) const
        {
            const_iterator j = other.begin();
            for (const_iterator i = begin(); i != end(); ++i, ++j) {
                if (*j >= *i) {
                    return false;
                }
            }
            return true;
        }
        
        Coordinate& operator += (const Coordinate& other)
        {
            const_iterator j = other.begin();
            for (iterator i = begin(); i != end(); ++i, ++j) {
                (*i) += (*j);
            }
            return *this;
        }

        Coordinate& operator -= (const Coordinate& other)
        {
            const_iterator j = other.begin();
            for (iterator i = begin(); i != end(); ++i, ++j) {
                (*i) -= (*j);
            }
            return *this;
        }
        
        static const Coordinate Zero()
        {
            Coordinate result;
            return result;
        }
        
        static const Coordinate Ones()
        {
            Coordinate result;
            for (iterator i = result.begin(); i != result.end(); ++i) {
                *i = TYPE(1);
            }
            return result;
        }
        
    private:
        TYPE m_values[DIMENSIONS];
    };
    
    template<typename TYPE, unsigned int DIMENSIONS>
    const Coordinate<TYPE, DIMENSIONS> operator + (
        const Coordinate<TYPE, DIMENSIONS>& lhs,
        const Coordinate<TYPE, DIMENSIONS>& rhs)
    {
        Coordinate<TYPE, DIMENSIONS> result = lhs;
        result += rhs;
        return result;
    }

    template<typename TYPE, unsigned int DIMENSIONS>
    const Coordinate<TYPE, DIMENSIONS> operator - (
        const Coordinate<TYPE, DIMENSIONS>& lhs,
        const Coordinate<TYPE, DIMENSIONS>& rhs)
    {
        Coordinate<TYPE, DIMENSIONS> result = lhs;
        result -= rhs;
        return result;
    }
    
    template<typename TYPE, unsigned int DIMENSIONS>
    std::ostream& operator << (std::ostream& out, const Coordinate<TYPE, DIMENSIONS>& coord)
    {
        typedef Coordinate<TYPE, DIMENSIONS> CoordinateType;
        out <<  "(";
        for (typename CoordinateType::const_iterator i = coord.begin(); i != coord.end();) {
            out << *i;
            ++i;
            if (i != coord.end()) {
                out << " ";
            }
        }
        out << ")";
        return out;
    }
    
    template<typename TYPE>
    const Coordinate<TYPE, 2> MakeCoordinate(const TYPE x, const TYPE y)
    {
        Coordinate<TYPE, 2> result;
        result[0] = x;
        result[1] = y;
        return result;
    }

    template<typename TYPE>
    const Coordinate<TYPE, 3> MakeCoordinate(const TYPE x, const TYPE y, const TYPE z)
    {
        Coordinate<TYPE, 3> result;
        result[0] = x;
        result[1] = y;
        result[2] = z;
        return result;
    }

    template<typename TYPE>
    const Coordinate<TYPE, 5> MakeCoordinate(const TYPE v, const TYPE w, const TYPE x, const TYPE y, const TYPE z)
    {
        Coordinate<TYPE, 5> result;
        result[0] = v;
        result[1] = w;
        result[2] = x;
        result[3] = y;
        result[4] = z;
        return result;
    }
}

