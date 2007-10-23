#include "stdafx.h"
#include "BairstowPolySolve.h"
#include "Polynomial.h"
#include "Norms.h"
#include "Console.h"
#include "Utilities.h"
#include "Maybe.h"
#include "Exception.h"

#include "TimingPool.h"

namespace {
	class BairstowPolySolveLocal {
	public:
		BairstowPolySolveLocal(const Real& u, const Real& v, const RPolynomial& p)
		: u(u), v(v), p(p), n(static_cast<int>(p.Degree())), m_b(n+1), m_dbdu(n+1), m_dbdv(n+1) {}

		const std::pair<double,double> Iterate() {
			class Local {
			public:
				static void Show(int divergenceFailure, int toManyIterations) {
					Console::OutputString("divergenceFailure = " + ToString(divergenceFailure) + "\ntoManyIterations = " + ToString(toManyIterations));
				}
			};

			static int divergenceFailure = 0;
			static int toManyIterations = 0;

			// should assert that p[n-1] == 1
			int iterations = 0, numberDivergences = 0;
			Real prevIncu = std::numeric_limits<Real>::max();
			Real prevIncv = std::numeric_limits<Real>::max();
			while(true) {
				InitializeB();
				InitializeDBDU();
				InitializeDBDV();
				const Real 
					c    = p[1] - u * m_b[0] - v * m_b[1],
					d    = p[0] - v * m_b[0],
					dcdu = -u*m_dbdu[0] - v*m_dbdu[1] - m_b[0],
					dcdv = -u*m_dbdv[0] - v*m_dbdv[1] - m_b[1],
					dddu = -v*m_dbdu[0],
					dddv = -v*m_dbdv[0] - m_b[0],
					det = dcdu * dddv - dcdv * dddu,
					incu = (c*dddv - d*dcdv)/det,
					newu = u  - incu,// + Random<Real>() * 1.0e-5,
					incv = (d*dcdu - c*dddu)/det,
					newv = v - incv;// + Random<Real>() * 1.0e-5;
				
				if (NearlyZeroQ(10*sqrt((u-newu)*(u-newu) + (v-newv)*(v-newv)))) break;

				
				if (Norm(prevIncu) < Norm(incu) && Norm(prevIncv) < Norm(incv) && numberDivergences++ > 100)
				{	
					Local::Show(divergenceFailure++, toManyIterations);
					break;
				}
				
				u = newu;
				v = newv;
				if (iterations++ == 200) 
				{
					Local::Show(divergenceFailure, toManyIterations++);
					break;
				}
				prevIncu = incu;
				prevIncv = incv;
			}
			//out << ToString(iterations) << ",";
			return std::make_pair(u,v);
		}

		RPolynomial Factored() const {
			RPolynomial result(0, n-2);
			for (int i = 0 ; i <= n-2 ; ++i) {
				result[i] = m_b[i];
			}
			return result;
		}
	private:
		void InitializeB() {
			// means m_b.size() == n+1;
			m_b[n] = m_b[n - 1] = 0;
			for (int i = n - 2 ; i >= 0 ; i--) {
				m_b[i] = p[i + 2] - u*m_b[i + 1] - v*m_b[i + 2];
			}
		}

		void InitializeDBDU() {
			// means m_b.size() == n+1;
			m_dbdu[n] = m_dbdu[n - 1] = 0;
			for (int i = n - 2 ; i >= 0 ; i--) {
				m_dbdu[i] = -u*m_dbdu[i + 1] - m_b[i + 1] - v*m_dbdu[i + 2];
			}
		}

		void InitializeDBDV() {
			// means m_b.size() == n+1;
			m_dbdv[n] = m_dbdv[n - 1] = 0;
			for (int i = n - 2 ; i >= 0 ; i--) {
				m_dbdv[i] = -u*m_dbdv[i + 1] - m_b[i + 2] - v*m_dbdv[i + 2];
			}
		}

		Real u,v;
		const RPolynomial p;
		const int n;
		std::vector<Real> m_b, m_dbdu, m_dbdv;
	};
}

Maybe<Real> BairstowPolySolve::FindSmallestPositiveRoot(const RPolynomial& p) {
	std::vector<std::pair<Real, Real> > dummies;
	return FindSmallestPositiveRoot(p, dummies);
}

Maybe<Real> BairstowPolySolve::FindSmallestPositiveRoot(const RPolynomial& p, std::vector<std::pair<Real, Real> >& initial)
{
	TIMETHISFUNCTION;
	if (NumberOfSignChanges(p) == 0)
		return Maybe<Real>();
	
	Maybe<Real> smallestRoot;

	// make monic;
	RPolynomial q = p/p[p.Degree()];

	int i = 0;
	initial.resize(q.Degree(), std::make_pair(.2,.2));

	while (q.Degree() > 2) {
		std::pair<Real, Real>& quadraticCoeffiecients = initial[i++];
		BairstowPolySolveLocal polySolve(quadraticCoeffiecients.first,quadraticCoeffiecients.second,q);
		quadraticCoeffiecients = polySolve.Iterate();
		Maybe<Real> root = FindSmallestPositiveRootOfQuadratic(1, quadraticCoeffiecients.first, quadraticCoeffiecients.second);
		if (root.IsValid()) {
			if (smallestRoot.IsValid()) smallestRoot = std::min(smallestRoot.Get(), root.Get());
			else smallestRoot = root;
		}
		q = polySolve.Factored();
	}
	if (q.Degree() == 1) {
		if (q[1] != 1) throw Exception("shouldn't happen");
		if (q[0] <= 0) {
			if(smallestRoot.IsValid()) smallestRoot = std::min(smallestRoot.Get(), -q[0]);
			else smallestRoot = -q[0];
		}
	} else if (q.Degree() == 2) {
		Maybe<Real> root = FindSmallestPositiveRootOfQuadratic(1, q[1], q[0]);
		if (root.IsValid()) {
			if (smallestRoot.IsValid()) smallestRoot = std::min(smallestRoot.Get(), root.Get());
			else smallestRoot = root;
		}
	}
	return smallestRoot;
}
