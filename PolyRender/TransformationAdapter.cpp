#include "stdafx.h"
#include "TransformationAdapter.h"
#include "Line.h"
#include "Maybe.h"
#include "Solid.h"
#include "Auto.h"
#include "Point.h"
#include "IntersectForwarder.h"

#include "TimingPool.h"

namespace {
	class TransformationAdapter;

	class TransformationAdapterIntersect : public IntersectForwarder {
	public:
		TransformationAdapterIntersect(
			// TODO: this could be replaced by a transformation class or something
			const Matrix& transformation, 
			const Point& translation,
			const Auto<const Intersect>& intersect
		) :	m_transformation(transformation),
			m_translation(translation),
			IntersectForwarder(intersect) 
		{
		}

		// Intersect
		Point NormalAt() const {
			return MatrixProd(m_transformation, m_intersect->NormalAt());
		}

		// Intersect
		Point PositionAt() const {
			return MatrixProd(m_transformation, m_intersect->PositionAt()) + m_translation;
		}

	private:
		const Point m_translation;
		const Matrix m_transformation;
	};

	class TransformationAdapter : public Solid {
	public:
		TransformationAdapter(const Auto<const Solid>& solid, const Matrix& transformation, const Point& translation)
		:	m_solid(solid),
			m_transformation(transformation),
			m_translation(translation)
		{
		}

		// Solid
		Intersect00 DetermineClosestIntersectionPoint(
			const Line& lightRay,
			const eIntersectType intersectionType,
			RenderMemory& renderMemory) const 
		{
			TIMETHISFUNCTION;
			const Intersect00 intersect = m_solid->DetermineClosestIntersectionPoint(
				// TODO: no test for this behaviour
				Line(
					TransposeMatrixProd(m_transformation, lightRay.Start() - m_translation), 
					TransposeMatrixProd(m_transformation, lightRay.Direction())
				),
				intersectionType,
				renderMemory);
			
			if (intersect.IsValid())
				return Intersect00(new TransformationAdapterIntersect(m_transformation, m_translation, intersect.Get()));
			else 
				return intersect;
		}

		// Solid
		bool InsideQ(const Point& point) const  {
			return m_solid->InsideQ(TransposeMatrixProd(m_transformation, point - m_translation));
		}

	private:
		const Auto<const Solid> m_solid;
		const Point m_translation;
		const Matrix m_transformation;
	};	
}

Auto<const Solid> MakeTransformationAdapter(
	const Auto<const Solid>& solid, 
	const Matrix& transformation, 
	const Point& translation
) {
	return new TransformationAdapter(solid, transformation, translation);
}
