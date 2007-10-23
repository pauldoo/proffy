#include "stdafx.h"

#include "AutoDeclarations.h"
#include "Auto.h"
#include "Utilities.h"

#include "DestructionAsserter.h"
#include "LinkCount.h"
#include "ColorSolid.h"
#include "TextureMap.h"
#include "Intersect.h"
#include "BumpMap.h"
#include "World.h"
#include "RenderInfo.h"
#include "RenderMask.h"
#include "Light.h"

template <typename Type>
Auto<Type>::Auto() : m_item(NULL)
{
};

template <typename Type>
Auto<Type>::Auto(Type* item) : m_item(item)
{
	m_item->IncrementCount();
};

template <typename Type>
Auto<Type>::Auto(const Auto& otherAuto)
:	m_item(otherAuto.m_item)
{
	m_item->IncrementCount();
};

template <typename Type>
Auto<Type>::~Auto()	{
	m_item->DecrementCount();
}

template <typename Type>
Type* Auto<Type>::operator->() {
	return m_item;
}

template <typename Type>
const Type* Auto<Type>::operator->() const {
	return m_item;
}

template <typename Type>
Type* Auto<Type>::Pointer() {
	return m_item;
}
	
template <typename Type>
const Type* Auto<Type>::Pointer() const {
	return m_item;
}

template <typename Type>
void Auto<Type>::operator=(Type* item) {
	item->IncrementCount();
	m_item->DecrementCount();
	m_item = item;
}

template class Auto<DestructionAsserter>;
template class Auto<DestructionAsserter const>;
template class Auto<LinkCount>;
template class Auto<LinkCount const>;
template class Auto<Solid const>;
template class Auto<TextureMap const>;
template class Auto<ColorSolid const>;
template class Auto<Intersect const>;
template class Auto<BumpMap const>;
template class Auto<World const>;
template class Auto<RenderInfo const>;
template class Auto<RenderMask const>;
template class Auto<Light const>;
