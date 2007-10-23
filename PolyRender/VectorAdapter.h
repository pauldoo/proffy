#pragma once

template <typename ScalarType, typename Field>
class VectorAdapter {
public:
	
	/*friend Field& operator/=(Field& a, const ScalarType& s) {
		a *= (1/s);
		return a;
	}*/

	friend Field& operator-=(Field& a, const Field& b) {
		a += -1*b;
		return a;
	}

	friend Field operator+(const Field&a, const Field& b) {
		Field result = a;
		result += b;
		return result;
	}

	friend Field operator*(const Field&a, const ScalarType& s) {
		Field result = a;
		result *= s;
		return result;
	}

	friend Field operator-(const Field& a, const Field& b) {
		return a + -1*b;
	}

	friend Field operator/(const Field& a, const ScalarType& s) {
		return a * (1/s);
	}

	friend bool operator!=(const Field& a, const Field& b) {
		return !(a == b);
	}

	friend Field operator*(const ScalarType& t, const Field& v) {
		return v*t;
	}

	friend Field operator+(const Field& a) {
		return a;
	}
	
	friend Field operator-(const Field& a) {
		return (-1)*a;
	}
};
