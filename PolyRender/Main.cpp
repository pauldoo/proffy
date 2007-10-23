#include "stdafx.h"
#include "DrawScreen.h"

#include <iostream>
#include <SDL.h>

int main(int, char* [])
{
    std::ios::sync_with_stdio(false);
    DrawScreen::DoIt();
    return EXIT_SUCCESS;
}
