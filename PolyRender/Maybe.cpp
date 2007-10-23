#include "stdafx.h"
#include "Maybe.h"
#include "Exception.h"
#include "Precision.h"
#include "Auto.h"
#include "Scalar.h"

class Intersect;
template class Maybe<Real>;
template class Maybe<Auto<const Intersect> >;
template class Maybe<class Auto<class LinkCount> >;

template <typename T>
Maybe<T>::Maybe() : m_item(0) {}

template <typename T>
Maybe<T>::Maybe(const T& item) : m_item(new T(item)) {}

template <typename T>
Maybe<T>::Maybe(const Maybe<T>& otherMaybe) : m_item(otherMaybe.IsValid() ? new T(otherMaybe.Get()) : 0) {}

template <typename T>
Maybe<T>::~Maybe() {
	delete m_item;
}

template <typename T>
bool Maybe<T>::IsValid() const {
	return m_item != 0;
}

template <typename T>
T Maybe<T>::Get() const {
	if (m_item == 0) throw Exception("Get called on invalid Maybe");
	return *m_item;
}

template <typename T>
void Maybe<T>::operator=(const T& item) {
	if (m_item) delete m_item;
	m_item = new T(item);
}

template <typename T>
void Maybe<T>::operator=(const Maybe<T>& otherMaybe) {
	if (m_item) delete m_item;
	m_item = otherMaybe.IsValid() ? new T(otherMaybe.Get()) : 0;
}