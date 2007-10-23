// DrawScreen.cpp : Main work house of the code. Entry Point.
//
#include "stdafx.h"
#include "GlobalMFC.h"
#include "GlobalWindowSize.h"
#include "DrawScreen.h"
#include "World.h"
#include "Plane.h"
#include "Sphere.h"
#include "Solid.h"
#include "Test.h"
#include "ProjectionPlane.h"
#include "CheckerMap.h"
#include "ApplyTexture.h"
#include "ColorMap.h"
#include "TransformationAdapter.h"
#include "Torus.h"
#include "JuliaSet.h"
#include "Line.h"
#include "Intersect.h"
#include "Maybe.h"
#include "TwirlyBump.h"
#include "BumpMapAdapter.h"
#include "SolidUnion.h"
#include "Fold.h"
#include "RenderInfo.h"
#include "Light.h"
#include "StandardRenderMask.h"
#include "Timer.h"
#include "TimingPool.h"
#include "ColorFuncs.h"

#include <iostream>
#include <fstream>
#include <SDL.h>


namespace {
	/*	This Function Takes the CoOrdinates of the pixel and renders it. It does this by First Projecting a 
	line through the pixel and seeing where it intersects with the solids in the universe and then rendering 
	that particular point. The handle must also be passed to give it the ability to draw pixels to it */

#ifdef WIN32
	// standard windows procedure
	bool CheckForMessages()
	{
		MSG msg;
		if (PeekMessage(&msg, NULL, 0, 0, PM_REMOVE)) { 
			// test if this is a quit
			if (msg.message == WM_QUIT) {
				PostQuitMessage(0);
				return true;
			}
	
			// translate any accelerator keys
			TranslateMessage(&msg);

			// send the message to the window proc
			DispatchMessage(&msg);
		}
		return false;
	}

	void SetAPixel(const int i, const int j, const Color& color) {
		SetPixelV(hdc, i, j, ColorFuncs::ToCOLORREF(color));
	}
#else
    SDL_Surface *surface = NULL;

    bool CheckForMessages() {
        SDL_Event event;
    
        while (SDL_PollEvent(&event)) {
            switch (event.type) {
                case SDL_QUIT:
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    void SetAPixel(const int x, const int y, const Color& c) {
        if (x >= 0 && x < surface->w && y >= 0 && y < surface->h) {
            int colorref = ColorFuncs::ToCOLORREF(c);
            Uint32 color = SDL_MapRGB(
                surface->format,
                (colorref >> 16) & 0xff,
                (colorref >> 8) & 0xff,
                colorref & 0xff
            );
            if ( SDL_MUSTLOCK(surface) ) {
                if ( SDL_LockSurface(surface) < 0 ) {
                    std::cerr << "SDL_LockSurface failed\n";
                    exit(EXIT_FAILURE);
                }
            }
            switch (surface->format->BytesPerPixel) {
                case 4:
                    {
                        Uint32 *bufp;
            
                        bufp = (Uint32 *)surface->pixels + y*surface->pitch/4 + x;
                        *bufp = color;
                    }
                    break;
                default:
                    std::cerr << "Unexpected surface format\n";
                    exit(EXIT_FAILURE);
            }
            if ( SDL_MUSTLOCK(surface) ) {
                SDL_UnlockSurface(surface);
            }

            static int counter = 0;
            if (++counter % 10000 == 0) {
                SDL_UpdateRect(surface, 0, 0, surface->w, surface->h);
            }
        } else {
            std::cout << __PRETTY_FUNCTION__ << ": Called for (" << x << ", " << y << ") which is out of bounds\n";
        }
    }
#endif

	void RenderPixel(const int x, const int y, const Auto<const World>& world, RenderMemory& renderMemory) {
		
		const Line lightRay = world->GetProjectionPlane().ProjectFromPixel(x,y);
		
		Intersect00 closestIntersect;
		{
			TIMETHISBLOCK("Initial Ray Casting");
			closestIntersect = 
				world->GetSolid()->DetermineClosestIntersectionPoint(
					lightRay,
					eRender,
					renderMemory);
		}
		
		if (closestIntersect.IsValid()) {
			TIMETHISBLOCK("Actual Rendering");
			const Color color = closestIntersect.Get()->Render(
				new RenderInfo(closestIntersect.Get(), world, lightRay), renderMemory);
			SetAPixel(x, y, color);
		} else {
			SetAPixel(x, y, Color(1,1,4));
		}
	}
}

void DrawScreen::DoIt() {
	// First Create the World.

#ifdef WIN32
	Test();
#endif
	TimingPool::ClearAllTimers();

	const Auto<const Solid> shapes =
		Fold(MakeSolidUnion,
			MakeTransformationAdapter(
				MakeApplyTexture(
					MakePlane(),
					MakeStandardRenderMask(new CheckerMap())
				),
				Matrix(Point(1,0,0),Point(0,0,1),Point(0,1,0)),
				Point(0,-1,0)
			),
			MakeTransformationAdapter(
				MakeApplyTexture(
					MakeSphere(.25),
					MakeStandardRenderMask(new ColorMap(Color(2,0,0)))
				),
				IdentityMatrix(),
				Point(-1,0,4)
			),
			MakeTransformationAdapter(
				MakeApplyTexture(
					MakeSphere(.25),
					MakeStandardRenderMask(new ColorMap(Color(0,2,0)))
				),
				IdentityMatrix(),
				Point(1,0,4)
			),
			MakeTransformationAdapter(
				MakeBumpMapAdapter(
					MakeApplyTexture(
						MakeTorus(.8, 1),
						MakeStandardRenderMask(new JuliaSet())
					),
					new TwirlyBump()
				),
				AlignCoordsSys(Point(-1,1,-1)),
				Point(0,1,6)
			)
		);

	World::LightList lights;
	lights.push_back(new Light(Point(-4,1,4), Color(1,.1,.1) * .6));
	lights.push_back(new Light(Point(2,1,1), Color(.1,1,.1) * .6));
	lights.push_back(new Light(Point(-2,7,1), Color(1,1,1) * .4));

	const Auto<const World> world = new World(
		shapes, 
		ProjectionPlane(), 
		lights);

	// The height and width of the drawing area (window size minus stuff on the side).
    int width  = drawing_pane_width; 
    int height = drawing_pane_height; 
	

	// The x and y denote the coordinates of the pixel we are drawing (starting position = top left corner).
	int x = 0, y = 0;	

	// Instead of a regular left-right pan I chose a loop which moves it's way around the 
	// drawing area in a square spiral.
	// This loop has the advantage that consecutive pixels are adjacent 
	// which means that we can use the t for one as a initial guess for the next.
	// NOTE: Code must take account of the fact that the screen may not be square
	// (causes problems at the end).
#ifdef WIN32
	hdc = GetDC(hWnd);	
#else
    if (SDL_Init(SDL_INIT_VIDEO) != 0) {
        std::cerr << "SDL_Init failed\n";
        exit(EXIT_FAILURE);
    }
    atexit(SDL_Quit);
    surface = SDL_SetVideoMode(width, height, 32, SDL_SWSURFACE);
    if (surface == NULL) {
        std::cerr << "SDL_SetVideoMode failed\n";
        exit(EXIT_FAILURE);
    }
#endif
	const Timer time = Timer().Start();
	{ TIMETHISBLOCK("Total Timing");
	do																
	{	
		RenderMemory renderMemory;
		if(width) {
			for (int dx = 0 ; dx < width ; dx++, x++) {				// Move left across the screen
				RenderPixel(x,y,world,renderMemory);
			}
			if (CheckForMessages()) return;
			width--;
		}
		if(height) {
			for (int dy = 0 ; dy < height  ; dy++, y++) {			// Move down across the screen
				RenderPixel(x,y,world,renderMemory);	
			}
			if (CheckForMessages()) return;
			height--;
		}
		if(width) {
			for (int dx = 0 ; dx < width ; dx++, x--) {				// Move right across the screen
				RenderPixel(x,y,world,renderMemory);		
			}
			if (CheckForMessages()) return;
			width--;
		}
		if(height) {
			for (int dy = 0 ; dy < height ; dy++, y--) {			// Move up across the screen
				RenderPixel(x,y,world,renderMemory);	
			}
			if (CheckForMessages()) return;
			height--;
		}
	} while(width || height); }
	PersistentConsole::OutputString("Total time was " + ToString(time.CurrentTime()) + "\n");
	std::ofstream out("timing.txt");
	out << TimingPool::TimingSummary();

#ifdef WIN32
	ReleaseDC(hWnd,hdc);
#else
    SDL_UpdateRect(surface, 0, 0, surface->w, surface->h);

    SDL_Event event;

    while (SDL_WaitEvent(&event)) {
        switch (event.type) {
            case SDL_QUIT:
                return;
            default:
                break;
        }
    }
#endif
}
