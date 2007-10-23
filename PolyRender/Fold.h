#pragma once

template <typename R, typename T, typename T1, typename T2>
R Fold(R (*function)(T,T), T1 t1, T2 t2) {
	return (*function)(t1,t2);
}

template <typename R, typename T, typename T1, typename T2, typename T3>
R Fold(R (*function)(T,T), T1 t1, T2 t2, T3 t3) {
	return (*function)(Fold(function,t1,t2),t3);
}

template <typename R, typename T, typename T1, typename T2, typename T3, typename T4>
R Fold(R (*function)(T,T), T1 t1, T2 t2, T3 t3, T4 t4) {
	return (*function)(Fold(function,t1,t2,t3),t4);
}
