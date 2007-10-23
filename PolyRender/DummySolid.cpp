#include "stdafx.h"
#include "DummySolid.h"
#include "Intersect.h"
#include "Maybe.h"
#include "Exception.h"
#include "Point.h"
#include "Solid.h"
#include "Auto.h"

namespace {
	class DummyIntersect : public Intersect {
	public:
		DummyIntersect(const Point& position, const Point& normal)
		: m_position(position), m_normal(normal) 
		{
		}

		Real DistanceAt() const { 
			throw Exception("Not implemented"); 
		}

		Color Render(const Auto<const RenderInfo>&, RenderMemory&) const { 
			throw Exception("Not implemented"); 
		}

		Point NormalAt() const { 
			return m_normal; 
		}

		Point UVCoordsAt() const { 
			throw Exception("Not implemented"); 
		}
		
		Point PositionAt() const { 
			return m_position; 
		}
		
	private:
		const Point m_position;
		const Point m_normal;
	};

	class DummySolid : public Solid {
	public:
		DummySolid(const Point& position, const Point& normal) 
		:	m_intersect(new DummyIntersect(position, normal))
		{
		}

		// Solid
		Intersect00 DetermineClosestIntersectionPoint(const Line&, const eIntersectType, RenderMemory&) const {
			return m_intersect;
		};

		// Solid
		bool InsideQ(const Point&) const {
			throw Exception("Not implemented");
		}

	private:
		const Auto<const Intersect> m_intersect;
	};
}

Auto<const Solid> MakeDummySolid(const Point& position, const Point& normal) {
	return new DummySolid(position, normal);
}