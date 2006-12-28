#pragma once

#include "Toy/Coordinate.h"
#include "Toy/Counted.h"
#include "Toy/AssertException.h"

namespace Toy {
    template<typename TYPE, unsigned int DIMENSIONS>
    class Block : public Counted
    {
    public:
        typedef Coordinate<unsigned int, DIMENSIONS> CoordinateType;
        typedef std::vector<TYPE> ContainerType;
        typedef typename ContainerType::const_iterator const_iterator;
        typedef typename ContainerType::iterator iterator;
        
        Block(const CoordinateType& size) : m_size(size), m_data(m_size.BlockVolume())
        {
        }
        
        const CoordinateType Size() const
        {
            return m_size;
        }
        
        const bool Contains(const CoordinateType& pos) const
        {
            return m_size.EntirelyContains(pos);
        }
        
        const const_iterator begin() const
        {
            return m_data.begin();
        }
        
        const const_iterator end() const
        {
            return m_data.end();
        }
        
        const iterator begin()
        {
            return m_data.begin();
        }
        
        const iterator end()
        {
            return m_data.end();
        }

        const const_iterator Lookup(const CoordinateType& pos) const
        {
            return begin() + Offset(pos);
        }
        
        const iterator Lookup(const CoordinateType& pos)
        {
            return begin() + Offset(pos);
        }
        
    protected:
        ~Block()
        {
        }
        
    private:
        const unsigned int Offset(const CoordinateType& pos) const
        {
            TOY_DEBUG_ASSERT(Contains(pos), "Coordinate is not inside block");
            typename CoordinateType::const_iterator i = m_size.begin();
            typename CoordinateType::const_iterator j = pos.begin();
            unsigned int result = 0;
            for (; i != m_size.end(); ++i, ++j) {
                result = result * (*i) + (*j);
            }
            return result;
        }
    
        const CoordinateType m_size;
        ContainerType m_data;
    };
    
    template<typename TYPE, unsigned int DIMENSIONS>
    std::ostream& operator <<(std::ostream& out, const Block<TYPE, DIMENSIONS>& block)
    {
        typedef Block<TYPE, DIMENSIONS> BlockType;
        typedef typename BlockType::CoordinateType CoordinateType;
        
        const CoordinateType size = block.Size();
        CoordinateType position;
        for (typename BlockType::const_iterator pixel = block.begin(); pixel != block.end(); ++pixel) {
            for (unsigned int i = DIMENSIONS-1; i >= 0 && position[i] == 0; --i) {
                out << "{";
            }

            out << (*pixel);
            
            if (++position[DIMENSIONS-1] == size[DIMENSIONS-1]) {
                for (unsigned int i = DIMENSIONS-1; i >= 0 && position[i] == size[i]; --i) {
                    out << "}";
                    position[i] = 0;
                    if (i > 0) {
                        ++position[i-1];
                    }
                }
            } else {
                out << " ";
            }
        }
        return out;
    }
}

