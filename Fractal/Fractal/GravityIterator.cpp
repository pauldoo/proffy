#include "GravityIterator.h"

#include "Accumulator.h"
#include "Geometry.h"

namespace Fractal
{
    template<typename T>
    GravityIterator<T>::GravityIterator(
        Accumulator* accumulator00,
        const std::vector<Vector>& masses,
        const double acc_step,
        const double damping,
        const double distance_cutoff
    ) :
        m_accumulator00(accumulator00),
        m_masses(masses),
        m_acc_step(acc_step),
        m_damping(damping),
        m_distance_cutoff(distance_cutoff)
    {
    }
    
    template<typename T>
    GravityIterator<T>::~GravityIterator()
    {
    }
    
    template<typename T>
    void GravityIterator<T>::Seed(const T& seed)
    {
        m_position = seed;
        m_velocity.clear();
    }

    template<typename T>
    const T& GravityIterator<T>::Value(void) const
    {
        return m_position;
    }

    template<typename T>
    const T& GravityIterator<T>::Iterate(void)
    {
        Vector acceleration;
        acceleration.clear();
        for (typename VectorList::const_iterator i = m_masses.begin(); i != m_masses.end(); ++i) {
            const Vector displacement = (*i) - m_position;
            const double distance = norm_2(displacement);
            const Vector displacement_unit_vec = displacement / distance;
            const Vector force = displacement_unit_vec / (distance * distance);
            acceleration += force;
        }
	const double time_step = m_acc_step / norm_2(acceleration);
        m_velocity *= pow(1.0 - m_damping, time_step);
	m_velocity += acceleration * time_step;
        m_position += m_velocity * time_step;
	if (m_accumulator00) {
	    m_accumulator00->Accumulate( Fractal::Geometry::Vector2ToVector4(Value()), time_step );
	}
        return Value();
    }
    
    template<typename T>
    const int GravityIterator<T>::IterateUntilTrapped()
    {
        while (true) {
            const int result = Trapped();
            if (result != -1) {
                return result;
            }
            Iterate();
        }
    }
    
    template<typename T>
    const int GravityIterator<T>::Trapped(void) const
    {
        for (typename VectorList::const_iterator i = m_masses.begin(); i != m_masses.end(); ++i) {
            const Vector displacement = (*i) - m_position;
            const double distance = norm_2(displacement);
            if (distance <= m_distance_cutoff) {
                return i - m_masses.begin();
            }
        }
        return -1;
    }
    
    template class GravityIterator< Fractal::Vector2 >;
}

