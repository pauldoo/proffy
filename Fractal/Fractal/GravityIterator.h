#ifndef Fractal_GravityIterator
#define Fractal_GravityIterator

#include "Iterator.h"

namespace Fractal { class Accumulator; }

namespace Fractal
{
    template< typename T >
    class GravityIterator : public Iterator<T>
    {
    public:
        typedef T Vector;
        typedef std::vector<Vector> VectorList;
        
        GravityIterator(
            Accumulator* accumulator00,
            const VectorList& masses,
            const double acc_step,
            const double damping,
            const double distance_cutoff
        );
        
        ~GravityIterator();
        
        const int IterateUntilTrapped();
    
        // Fractal::Iterator
        void Seed(const Vector& seed);

        // Fractal::Iterator
        const Vector& Value(void) const;

        // Fractal::Iterator
        const Vector& Iterate(void);
        
    private:
        // If the object is trapped, returns the index of the trapper
        // If the obect is not trapped, return -1
        const int Trapped(void) const;
    
        Accumulator* const m_accumulator00;
        const VectorList m_masses;
	const double m_acc_step;
        const double m_damping;
        const double m_distance_cutoff;
        
        Vector m_position;
        Vector m_velocity;
        
    };
}

#endif

